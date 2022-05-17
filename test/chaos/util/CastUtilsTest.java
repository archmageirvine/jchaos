package chaos.util;

import java.util.HashSet;

import chaos.common.inanimate.Generator;
import junit.framework.Assert;
import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Conveyance;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.dragon.RedDragon;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.ManaBattery;
import chaos.common.inanimate.Rock;
import chaos.common.inanimate.WaspNest;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Eagle;
import chaos.common.monster.EarthElemental;
import chaos.common.monster.FireDemon;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.Marid;
import chaos.common.monster.MindFlayer;
import chaos.common.monster.Robot;
import chaos.common.monster.Shadow;
import chaos.common.spell.MassMorph;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class CastUtilsTest extends TestCase {

  private static class MyBool {
    boolean mCast = false;
    boolean mWeapon = false;
    boolean mRedraw = false;
  }

  private static class MyListener implements EventListener {
    final MyBool mState = new MyBool();
    @Override
    public void update(final Event e) {
      if (e instanceof WeaponEffectEvent) {
        Assert.assertEquals(WeaponEffectType.TREE_CAST_EVENT, ((WeaponEffectEvent) e).getEventType());
        Assert.assertFalse(mState.mWeapon);
        mState.mWeapon = true;
      } else if (e instanceof CellEffectEvent) {
        if (((CellEffectEvent) e).getEventType() == CellEffectType.MONSTER_CAST_EVENT) {
          Assert.assertEquals(1, ((CellEffectEvent) e).getCellNumber());
          Assert.assertFalse(mState.mCast);
          mState.mCast = true;
        } else {
          Assert.assertEquals(CellEffectType.REDRAW_CELL, ((CellEffectEvent) e).getEventType());
          Assert.assertFalse(mState.mRedraw);
          mState.mRedraw = true;
        }
      }
    }
    MyBool getState() {
      return mState;
    }
  }

  public void testTreeCasting() {
    final Castable c = new MagicWood();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, c.getCastFlags());
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.register(listen);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertTrue(state.mWeapon);
    assertTrue(state.mCast);
    assertTrue(state.mRedraw);
    world.deregister(listen);
    // check that null parameters do not cause an exception
    c.cast(null, w, world.getCell(1), world.getCell(0));
    c.cast(world, null, world.getCell(1), world.getCell(0));
    c.cast(world, w, null, world.getCell(0));
    c.cast(world, w, world.getCell(1), null);
  }

  public void testCastOnGrowth() {
    final Castable c = new MagicWood();
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final Lion lion = new Lion();
    world.getCell(1).push(lion);
    final GooeyBlob gb = new GooeyBlob();
    world.getCell(1).push(gb);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(c, world.actor(1));
    world.getCell(1).reinstate();
    assertNull(world.actor(1));
  }

  public void testCastOnGrowth2() {
    final Castable c = new WeakWall();
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final Lion lion = new Lion();
    world.getCell(1).push(lion);
    final GooeyBlob gb = new GooeyBlob();
    world.getCell(1).push(gb);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(c, world.actor(1));
    world.getCell(1).reinstate();
    assertNull(world.actor(1));
  }

  public void testScore() {
    assertEquals(0, CastUtils.score((Cell) null));
    assertEquals(0, CastUtils.score(new Cell(0)));
    final Cell c = new Cell(0);
    c.push(new Lion());
    assertEquals(38, CastUtils.score(c));
    c.push(new RedDragon());
    assertEquals(56, CastUtils.score(c));
    c.push(new MindFlayer());
    assertEquals(14, CastUtils.score(c));
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    c.push(w);
    assertEquals(80, CastUtils.score(c));
  }

  public void testKeepFriends() {
    final Team team = new Team();
    final World world = new World(2, 1, team);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(1);
    final HashSet<Cell> h = new HashSet<>();
    final int t = team.getTeam(1);
    assertEquals(0, CastUtils.keepFriends(h, t, team).size());
    h.add(new Cell(5));
    assertEquals(0, CastUtils.keepFriends(h, t, team).size());
    final Lion l = new Lion();
    l.setOwner(2);
    final Cell c0 = world.getCell(0);
    c0.push(l);
    h.add(c0);
    assertEquals(0, CastUtils.keepFriends(h, t, team).size());
    l.setOwner(1);
    h.add(c0);
    assertEquals(1, CastUtils.keepFriends(h, t, team).size());
    assertTrue(h.contains(c0));
    l.setState(State.DEAD);
    assertEquals(0, CastUtils.keepFriends(h, t, team).size());
  }

  public void testKeepEnemies() {
    final Team team = new Team();
    final World world = new World(2, 1, team);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(1);
    final int t = team.getTeam(1);
    final HashSet<Cell> h = new HashSet<>();
    assertEquals(0, CastUtils.keepEnemies(h, t, team).size());
    h.add(new Cell(5));
    assertEquals(0, CastUtils.keepEnemies(h, t, team).size());
    final Lion l = new Lion();
    l.setOwner(1);
    final Cell c0 = world.getCell(0);
    c0.push(l);
    h.add(c0);
    assertEquals(0, CastUtils.keepEnemies(h, t, team).size());
    l.setOwner(2);
    h.add(c0);
    assertEquals(1, CastUtils.keepEnemies(h, t, team).size());
    assertTrue(h.contains(c0));
    l.setState(State.DEAD);
    assertEquals(0, CastUtils.keepEnemies(h, t, team).size());
  }

  public void testKeepHighestScoring() {
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, CastUtils.keepHighestScoring(t).size());
    final Cell cn = new Cell(23);
    t.add(cn);
    assertEquals(1, CastUtils.keepHighestScoring(t).size());
    assertTrue(t.contains(cn));
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    c.push(l);
    t.add(c);
    assertEquals(1, CastUtils.keepHighestScoring(t).size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    c2.push(l2);
    t.add(c2);
    assertEquals(2, CastUtils.keepHighestScoring(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.add(new Cell(42));
    assertEquals(2, CastUtils.keepHighestScoring(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Horse h = new Horse();
    h.setState(State.DEAD);
    final Cell c3 = new Cell(3);
    c3.push(h);
    t.add(c3);
    assertEquals(2, CastUtils.keepHighestScoring(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }

  public void testKeepAwake() {
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, CastUtils.keepAwake(t).size());
    final Cell cn = new Cell(23);
    t.add(cn);
    assertEquals(0, CastUtils.keepAwake(t).size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    c.push(l);
    t.add(c);
    assertEquals(1, CastUtils.keepAwake(t).size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    c2.push(l2);
    t.add(c2);
    assertEquals(1, CastUtils.keepAwake(t).size());
    assertTrue(t.contains(c));
    final Horse h = new Horse();
    h.setState(State.ASLEEP);
    final Cell c3 = new Cell(3);
    c3.push(h);
    t.add(c3);
    assertEquals(1, CastUtils.keepAwake(t).size());
    assertTrue(t.contains(c));
  }

  public void testKeepFastest() {
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, CastUtils.keepFastest(t).size());
    t.add(new Cell(23));
    assertEquals(0, CastUtils.keepFastest(t).size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    c.push(l);
    t.add(c);
    assertEquals(1, CastUtils.keepFastest(t).size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    c2.push(l2);
    t.add(c2);
    assertEquals(2, CastUtils.keepFastest(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.add(new Cell(42));
    assertEquals(2, CastUtils.keepFastest(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Eagle h = new Eagle();
    final Cell c3 = new Cell(3);
    c3.push(h);
    t.add(c3);
    assertEquals(1, CastUtils.keepFastest(t).size());
    assertTrue(t.contains(c3));
    h.set(Attribute.MOVEMENT, 0);
    assertEquals(1, CastUtils.keepFastest(t).size());
    assertTrue(t.contains(c3));
  }

  public void testPreferAnimates() {
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, CastUtils.preferAnimates(t).size());
    t.add(new Cell(23));
    assertEquals(1, CastUtils.preferAnimates(t).size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    c.push(l);
    t.add(c);
    assertEquals(1, CastUtils.preferAnimates(t).size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    c2.push(l2);
    t.add(c2);
    assertEquals(2, CastUtils.preferAnimates(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.add(new Cell(42));
    assertEquals(2, CastUtils.preferAnimates(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(3);
    c3.push(new GooeyBlob());
    t.add(c3);
    assertEquals(2, CastUtils.preferAnimates(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c4 = new Cell(4);
    c4.push(new MagicWood());
    final Cell c5 = new Cell(5);
    c5.push(new Rock());
    t.add(c4);
    t.add(c5);
    assertEquals(2, CastUtils.preferAnimates(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }

  public void testKeepClosest() {
    final World w = new World(5, 5);
    final HashSet<Cell> t = new HashSet<>();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    wiz.setState(State.ACTIVE);
    w.getCell(6).push(wiz);
    assertEquals(0, CastUtils.keepClosest(t, wiz, w).size());
    t.add(w.getCell(17));
    assertEquals(1, CastUtils.keepClosest(t, wiz, w).size());
    t.add(w.getCell(0));
    assertEquals(1, CastUtils.keepClosest(t, wiz, w).size());
    assertTrue(t.contains(w.getCell(0)));
    t.add(w.getCell(12));
    assertEquals(2, CastUtils.keepClosest(t, wiz, w).size());
    assertTrue(t.contains(w.getCell(0)));
    assertTrue(t.contains(w.getCell(12)));
    t.add(w.getCell(1));
    assertEquals(1, CastUtils.keepClosest(t, wiz, w).size());
    assertTrue(t.contains(w.getCell(1)));
    w.getCell(6).pop();
    t.add(w.getCell(12));
    assertEquals(2, CastUtils.keepClosest(t, wiz, w).size());
    assertTrue(t.contains(w.getCell(1)));
    assertTrue(t.contains(w.getCell(12)));
  }

  private static class MyListener2 implements EventListener {
    final MyBool mState = new MyBool();
    @Override
    public void update(final Event e) {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        Assert.assertEquals(CellEffectType.BONUS, ce.getEventType());
        Assert.assertEquals(42, ce.getCellNumber());
        Assert.assertFalse(mState.mCast);
        mState.mCast = true;
      } else {
        Assert.fail();
      }
    }
    MyBool getState() {
      return mState;
    }
  }

  public void testHandleScoreAndBonus() {
    CastUtils.handleScoreAndBonus(new Lion(), new Lion(), null);
    final Wizard1 w = new Wizard1();
    CastUtils.handleScoreAndBonus(w, new Lion(), null);
    final int d = new Lion().getDefault(Attribute.LIFE);
    assertEquals(d, w.getScore());
    assertEquals(0, w.getBonusSelect());
    CastUtils.handleScoreAndBonus(w, null, null);
    assertEquals(d, w.getScore());
    assertEquals(0, w.getBonusSelect());
    CastUtils.handleScoreAndBonus(w, new Robot(), null);
    assertEquals(d + new Robot().getDefault(Attribute.LIFE), w.getScore());
    assertEquals(1, w.getBonusSelect());
    final Cell cell = new Cell(42);
    final MyListener2 listen = new MyListener2();
    final MyBool state = listen.getState();
    cell.register(listen);
    CastUtils.handleScoreAndBonus(w, new Robot(), cell);
    assertEquals(d + 2 * new Robot().getDefault(Attribute.LIFE), w.getScore());
    assertEquals(2, w.getBonusSelect());
    assertTrue(state.mCast);
    cell.deregister(listen);
    final int o = w.getBonusCount();
    final Wizard2 w2 = new Wizard2();
    w2.addBonus(2, 3);
    CastUtils.handleScoreAndBonus(w, w2, cell);
    assertEquals(5, w.getBonusSelect());
    assertTrue(w.getBonusCount() > o + 3);
  }

  private void checkSubversionValue(final Cell c, final int v) {
    int s = 0;
    for (int k = 0; k < 100; ++k) {
      s += CastUtils.subversionScore(c);
    }
    s += 5;
    s /= 100;
    //System.out.println("s=" + s);
    assertTrue(Math.abs(s - v) < 4);
  }

  public void testSubversionScore() {
    assertEquals(0, CastUtils.subversionScore((Cell) null));
    assertEquals(0, CastUtils.subversionScore(new Cell(0)));
    final Cell c = new Cell(0);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    c.push(w);
    assertEquals(0, CastUtils.subversionScore(c));
    c.push(new Horse());
    checkSubversionValue(c, 4);
    ((Conveyance) c.peek()).setMount(w);
    assertEquals(0, CastUtils.subversionScore(c));
    c.push(new Lion());
    checkSubversionValue(c, 53);
    c.push(new Marid());
    checkSubversionValue(c, 108);
    c.push(new MindFlayer());
    checkSubversionValue(c, 44);
    c.push(new WaspNest());
    checkSubversionValue(c, 28);
    c.push(new Shadow());
    checkSubversionValue(c, 92);
    c.push(new EarthElemental());
    checkSubversionValue(c, 178);
    c.push(new FireDemon());
    checkSubversionValue(c, 114);
    final ChaosProperties p = ChaosProperties.properties();
    try {
      p.setProperty("ai.subversion.combat.scale", "10");
      p.setProperty("ai.subversion.mr.scale", "10");
      p.setProperty("ai.subversion.randomize", "1");
      p.setProperty("ai.subversion.inanimate.scale", "0.6");
      p.setProperty("ai.subversion.undead.scale", "3.4");
      p.setProperty("ai.subversion.caster.scale", "8");
      p.setProperty("ai.subversion.nonmaterial.scale", "2.4");
      p.setProperty("ai.subversion.lowcombat.scale", "0.4");
      assertEquals(1503, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(1360, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(270, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(270, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(378, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(2828, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(340, CastUtils.subversionScore(c));
      c.pop();
      assertEquals(0, CastUtils.subversionScore(c));
    } finally {
      ChaosProperties.reset();
    }
  }

  public void testKeepHighestSubversionScoring() {
    final ChaosProperties p = ChaosProperties.properties();
    try {
      p.setProperty("ai.subversion.randomize", "1");
      final HashSet<Cell> t = new HashSet<>();
      assertEquals(0, CastUtils.keepHighestSubversionScoring(t).size());
      final Cell cn = new Cell(23);
      t.add(cn);
      assertEquals(1, CastUtils.keepHighestSubversionScoring(t).size());
      assertTrue(t.contains(cn));
      final Cell c = new Cell(0);
      final Lion l = new Lion();
      l.setState(State.DEAD);
      c.push(l);
      t.add(c);
      assertEquals(1, CastUtils.keepHighestSubversionScoring(t).size());
      assertTrue(t.contains(c));
      final Cell c2 = new Cell(1);
      final Lion l2 = new Lion();
      l2.setState(State.DEAD);
      c2.push(l2);
      t.add(c2);
      assertEquals(2, CastUtils.keepHighestSubversionScoring(t).size());
      assertTrue(t.contains(c));
      assertTrue(t.contains(c2));
      t.add(new Cell(42));
      assertEquals(2, CastUtils.keepHighestSubversionScoring(t).size());
      assertTrue(t.contains(c));
      assertTrue(t.contains(c2));
      final Horse h = new Horse();
      h.setState(State.DEAD);
      final Cell c3 = new Cell(3);
      c3.push(h);
      t.add(c3);
      assertEquals(2, CastUtils.keepHighestSubversionScoring(t).size());
      assertTrue(t.contains(c));
      assertTrue(t.contains(c2));
    } finally {
      ChaosProperties.reset();
    }
  }

  public void testKeepSickest() {
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, CastUtils.keepSickest(t).size());
    t.add(new Cell(23));
    assertEquals(0, CastUtils.keepSickest(t).size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    c.push(l);
    t.add(c);
    assertEquals(1, CastUtils.keepSickest(t).size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    c2.push(l2);
    t.add(c2);
    assertEquals(2, CastUtils.keepSickest(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.add(new Cell(42));
    assertEquals(2, CastUtils.keepSickest(t).size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    l2.set(Attribute.LIFE, 1);
    assertEquals(1, CastUtils.keepSickest(t).size());
    assertTrue(t.contains(c2));
  }

  public void testKeepMostFrequentOwner() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final MassMorph x = new MassMorph();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(2);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(2);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(3);
    final Lion l3 = new Lion();
    l3.setOwner(Actor.OWNER_INDEPENDENT);
    c3.push(l3);
    t.add(c3);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c4 = new Cell(4);
    final Lion l4 = new Lion();
    l4.setOwner(Actor.OWNER_INDEPENDENT);
    c4.push(l4);
    t.add(c3);
    t.add(c4);
    x.filter(t, wiz, w);
    assertEquals(4, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    assertTrue(t.contains(c3));
    assertTrue(t.contains(c4));
    final Cell c5 = new Cell(5);
    final Lion l5 = new Lion();
    l5.setOwner(Actor.OWNER_INDEPENDENT);
    c5.push(l5);
    t.add(c5);
    x.filter(t, wiz, w);
    assertEquals(3, t.size());
    assertTrue(t.contains(c3));
    assertTrue(t.contains(c4));
    assertTrue(t.contains(c5));
    t.add(c2);
    t.add(c);
    w.getTeamInformation().setTeam(Actor.OWNER_INDEPENDENT, 1);
    final int h = w.getTeamInformation().getTeam(1);
    assertEquals(h, w.getTeamInformation().getTeam(Actor.OWNER_INDEPENDENT));
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.remove(c);
    t.add(c5);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
    assertNotNull(CastUtils.keepMostFrequentOwner(t, w, 0));
  }

  public void testManaBattery() {
    final World w = new World(3, 3);
    final ManaBattery mb = new ManaBattery();
    mb.setOwner(1);
    w.getCell(3).push(mb);
    final Wizard wiz2 = w.getWizardManager().getWizard(2);
    wiz2.setState(State.ACTIVE);
    w.getCell(1).push(wiz2);
    final Lion l = new Lion();
    l.setOwner(3);
    l.setState(State.ASLEEP);
    w.getCell(4).push(l);
    final Lion m = new Lion();
    m.setOwner(4);
    w.getCell(6).push(m);
    final MoveMaster mm = new MoveMaster(w);
    CombatUtils.performSpecialCombat(w, mm);
    final Wizard[] wiz = w.getWizardManager().getWizards();
    assertEquals(0, wiz[1].getBonusCount());
    assertEquals(0, wiz[1].getBonusSelect());
    assertEquals(1, wiz[2].getBonusCount());
    assertEquals(1, wiz[2].getBonusSelect());
    assertEquals(0, wiz[3].getBonusCount());
    assertEquals(0, wiz[3].getBonusSelect());
    assertEquals(1, wiz[4].getBonusCount());
    assertEquals(1, wiz[4].getBonusSelect());
  }

  public void testIsMutatable() {
    assertFalse(CastUtils.isMutatable(null));
    final Cell cell = new Cell(0);
    assertFalse(CastUtils.isMutatable(cell));
    final Lion lion = new Lion();
    cell.push(lion);
    assertTrue(CastUtils.isMutatable(cell));
    lion.setState(State.DEAD);
    assertFalse(CastUtils.isMutatable(cell));
    cell.pop();
    cell.push(new Generator());
    assertTrue(CastUtils.isMutatable(cell));
    cell.pop();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    cell.push(wiz);
    assertFalse(CastUtils.isMutatable(cell));
    cell.push(new Generator());
    assertFalse(CastUtils.isMutatable(cell));  // Dead Revenge style generator
    cell.pop();
    cell.pop();
    final Horse horse = new Horse();
    cell.push(horse);
    assertTrue(CastUtils.isMutatable(cell));
    horse.setMount(wiz);
    assertFalse(CastUtils.isMutatable(cell));
  }

  public void testMutate() {
    final Cell cell = new Cell(0);
    final Lion lion = new Lion();
    cell.push(lion);
    assertTrue(CastUtils.mutate(cell));
    assertTrue(cell.peek() != null);
    assertFalse(cell.peek() instanceof Lion);
  }
}
