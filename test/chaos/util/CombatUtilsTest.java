package chaos.util;

import static chaos.util.CombatUtils.COMBAT_FAILED;
import static chaos.util.CombatUtils.COMBAT_OK;
import static chaos.util.CombatUtils.COMBAT_SUCCEEDED;
import static chaos.util.CombatUtils.INVULNERABLE;
import static chaos.util.CombatUtils.NORMAL;
import static chaos.util.CombatUtils.RANGED;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Mountable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.growth.Earthquake;
import chaos.common.inanimate.MagicGlass;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.Pit;
import chaos.common.inanimate.WaspNest;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.DreadElf;
import chaos.common.monster.EarthElemental;
import chaos.common.monster.GiantBeetle;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.Manticore;
import chaos.common.monster.Orc;
import chaos.common.monster.Phoenix;
import chaos.common.monster.StoneGolem;
import chaos.common.monster.Thundermare;
import chaos.common.monster.Troll;
import chaos.common.monster.WoodElf;
import chaos.common.spell.PoisonDagger;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CombatUtilsTest extends TestCase {

  private static class MyListener implements EventListener {

    private int mCount = 0;

    @Override
    public void update(final Event event) {
      if (event instanceof TextEvent) {
        switch (mCount++) {
          case 0:
            Assert.assertEquals("Lion: Special Combat", event.toString());
            break;
          case 1:
            Assert.assertEquals("Eye for an eye!", event.toString());
            break;
          default:
            Assert.fail();
            break;
        }
      }
    }
  }

  public void testSpecialCombat() {
    final World w = new World(3, 3);
    final WizardManager wm = w.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.LIFE, 10);
    wm.setWizard(1, new Wizard1());
    wm.getWizard(1).setState(State.ACTIVE);
    wm.setWizard(2, wiz);
    wm.setWizard(3, new Wizard1());
    wm.getWizard(3).setState(State.ACTIVE);
    wm.setWizard(4, new Wizard1());
    wm.getWizard(4).setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    assertEquals(2, wiz.getOwner());
    final Lion l1 = new Lion();
    l1.setOwner(2);
    l1.set(Attribute.LIFE, 1);
    w.getCell(1).push(l1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    w.getCell(2).push(l2);
    final Lion l3 = new Lion();
    l3.setState(State.ASLEEP);
    l3.setOwner(0);
    l3.set(Attribute.LIFE, 1);
    w.getCell(3).push(l3);
    final Lion l4 = new Lion();
    l4.setOwner(3);
    l4.setState(State.ACTIVE);
    l4.set(Attribute.SPECIAL_COMBAT, 5);
    l4.setSpecialCombatApply(Attribute.LIFE);
    w.getCell(4).push(l4);
    final MyListener listener = new MyListener();
    w.getCell(4).register(listener);
    final Lion l5 = new Lion();
    l5.setOwner(3);
    l5.set(Attribute.LIFE, 1);
    w.getCell(5).push(l5);
    final Lion l6 = new Lion();
    l6.setOwner(4);
    l6.set(Attribute.LIFE, 1);
    w.getCell(6).push(l6);
    final Lion l8 = new Lion();
    l8.setOwner(2);
    l8.set(Attribute.LIFE, 90);
    w.getCell(8).push(l8);
    CombatUtils.performSpecialCombat(w, new MoveMaster(w));
    w.getCell(4).deregister(listener);
    assertEquals(State.DEAD, l1.getState());
    assertEquals(State.DEAD, l3.getState());
    assertEquals(State.DEAD, l6.getState());
    assertEquals(wiz, w.actor(0));
    assertEquals(5, wiz.get(Attribute.LIFE));
    assertEquals(l2, w.actor(2));
    assertEquals(l8, w.actor(8));
    assertEquals(l6, w.actor(6));
    assertEquals(85, l8.get(Attribute.LIFE));
  }

  public void testSpecialCombatDistance2() {
    final World w = new World(5, 5);
    final MoveMaster mm = new MoveMaster(w);
    final Lion lion = new Lion();
    lion.setOwner(1);
    w.getCell(0).push(lion);
    final int l = lion.get(Attribute.LIFE);
    final WaspNest wn = new WaspNest();
    wn.setOwner(2);
    w.getCell(2).push(wn);
    CombatUtils.performSpecialCombat(w, mm);
    assertEquals(l - wn.get(Attribute.SPECIAL_COMBAT), lion.get(Attribute.LIFE));
  }

  public void testSpecialCombatOnPit() {
    final World w = new World(5, 5);
    final MoveMaster mm = new MoveMaster(w);
    final Pit pit = new Pit();
    pit.setOwner(1);
    w.getCell(0).push(pit);
    final int l = pit.get(Attribute.LIFE);
    final WaspNest wn = new WaspNest();
    wn.setOwner(2);
    w.getCell(1).push(wn);
    CombatUtils.performSpecialCombat(w, mm);
    assertEquals(l, pit.get(Attribute.LIFE));
  }

  public void testSpecialCombatOnNuked() {
    final World world = new World(5, 5);
    final MoveMaster mm = new MoveMaster(world);
    final Nuked nuked = new Nuked();
    nuked.setOwner(1);
    world.getCell(0).push(nuked);
    final int l = nuked.get(Attribute.LIFE);
    final WaspNest wn = new WaspNest();
    wn.setOwner(2);
    world.getCell(1).push(wn);
    CombatUtils.performSpecialCombat(world, mm);
    assertEquals(l, nuked.get(Attribute.LIFE));
  }

  public void testSpecialMounted() {
    final World world = new World(1, 2);
    final MoveMaster mm = new MoveMaster(world);
    final Orc orc = new Orc();
    orc.setOwner(1);
    orc.set(Attribute.LIFE, 6);
    world.getCell(0).push(orc);
    final MagicWood magicWood = new MagicWood();
    magicWood.setOwner(2);
    final Wizard2 wizard = new Wizard2();
    wizard.setSpecialCombatApply(Attribute.LIFE);
    wizard.set(Attribute.SPECIAL_COMBAT, 15);
    wizard.setOwner(2);
    wizard.setState(State.ACTIVE);
    magicWood.setMount(wizard);
    world.getCell(1).push(magicWood);
    CombatUtils.performSpecialCombat(world, mm);
    assertEquals(State.DEAD, orc.getState());
  }

  public void testRangedCombatOnPit() {
    final World world = new World(5, 5);
    final Pit pit = new Pit();
    pit.setOwner(1);
    world.getCell(0).push(pit);
    final int l = pit.get(Attribute.LIFE);
    final RedDragon wn = new RedDragon();
    wn.setOwner(2);
    world.getCell(1).push(wn);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(world, null, wn, world.getCell(1), world.getCell(0), RANGED));
    assertEquals(l, pit.get(Attribute.LIFE));
  }

  private static class MagicGlassListener implements EventListener {
    boolean mSeen = false;
    boolean mSawRedraw = false;
    @Override
    public void update(final Event event) {
      if (event instanceof PolycellAttackEvent) {
        final Collection<Cell> cells = ((PolycellAttackEvent) event).getCells();
        Assert.assertEquals(1, cells.size());
        for (final Cell c : cells) {
          assertEquals(2, c.getCellNumber()); // There is only one cell in this set
        }
        mSeen = true;
      } else if (event instanceof PolycellEffectEvent) {
        mSawRedraw = true;
      }
    }
  }

  // Test that special combat avoids drawing graphical effects for cells that cannot
  // actually be affected by that special combat.
  public void testSpecialCombatMagicGlass() {
    final World world = new World(3, 3);
    final MagicGlassListener listener = new MagicGlassListener();
    world.register(listener);
    final MoveMaster mm = new MoveMaster(world);
    final Pit pit = new Pit();
    pit.setOwner(1);
    world.getCell(0).push(pit);
    final int lifePit = pit.get(Attribute.LIFE);
    final WeakWall weakWall = new WeakWall();
    weakWall.setOwner(1);
    world.getCell(1).push(weakWall);
    final GiantBeetle giantBeetle = new GiantBeetle();
    giantBeetle.setOwner(1);
    giantBeetle.set(Attribute.LIFE, 50);
    giantBeetle.set(Attribute.INTELLIGENCE, 1);
    world.getCell(2).push(giantBeetle);
    final MagicGlass magicGlass = new MagicGlass();
    magicGlass.setOwner(2);
    assertEquals(State.ACTIVE, magicGlass.getState());
    world.getCell(4).push(magicGlass);
    CombatUtils.performSpecialCombat(world, mm);
    assertEquals(lifePit, pit.get(Attribute.LIFE));
    assertEquals(weakWall, world.actor(1));
    assertEquals(giantBeetle, world.actor(2));
    assertEquals(0, giantBeetle.get(Attribute.INTELLIGENCE));
    assertEquals(State.DEAD, giantBeetle.getState());
    assertTrue(listener.mSeen);
    assertTrue(listener.mSawRedraw);
  }

  public void testRangedCombatOnDeadPhoenix() {
    final World world = new World(5, 5);
    final Phoenix phoenix = new Phoenix();
    phoenix.setOwner(1);
    phoenix.set(Attribute.LIFE, 0);
    phoenix.setState(State.DEAD);
    phoenix.setMoved(true);
    world.getCell(1).push(phoenix);
    final Manticore mc = new Manticore();
    mc.setOwner(2);
    world.getCell(2).push(mc);
    final RedDragon wn = new RedDragon();
    wn.setOwner(2);
    world.getCell(0).push(wn);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(world, null, wn, world.getCell(0), world.getCell(1), NORMAL));
    assertEquals(State.DEAD, phoenix.getState());
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(world, null, mc, world.getCell(2), world.getCell(1), RANGED));
    assertEquals(State.DEAD, phoenix.getState());
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(world, null, wn, world.getCell(0), world.getCell(1), RANGED));
    assertEquals(State.ACTIVE, phoenix.getState());
    assertFalse(phoenix.isMoved());
    assertEquals(phoenix.getDefault(Attribute.LIFE), phoenix.get(Attribute.LIFE));
  }

  public void testPoisonDagger() {
    final World world = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    wiz.setState(State.ACTIVE);
    world.getCell(5).push(wiz);
    final Troll t = new Troll();
    t.setOwner(1);
    world.getCell(0).push(t);
    final PoisonDagger pd = new PoisonDagger();
    pd.cast(world, wiz, world.getCell(0), world.getCell(5));
    assertEquals(5, t.get(Attribute.COMBAT));
    assertEquals(Attribute.LIFE_RECOVERY, t.getCombatApply());
    final Lion l = new Lion();
    l.setOwner(2);
    world.getCell(1).push(l);
    final int lr = l.get(Attribute.LIFE_RECOVERY);
    CombatUtils.performCombat(world, wiz, t, world.getCell(0), world.getCell(1), NORMAL);
    assertEquals(lr - 5, l.get(Attribute.LIFE_RECOVERY));
  }

  public void testReflect() {
    final World world = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.REFLECT, 1);
    world.getCell(1).push(wiz);
    final WoodElf t = new WoodElf();
    t.setOwner(2);
    t.setState(State.ACTIVE);
    world.getCell(0).push(t);
    final Wizard1 wiz2 = new Wizard1();
    wiz2.setOwner(2);
    wiz2.setState(State.ACTIVE);
    world.getCell(2).push(wiz2);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(world, wiz2, t, world.getCell(0), world.getCell(1), RANGED));
    assertEquals(wiz.getDefault(Attribute.LIFE), wiz.get(Attribute.LIFE));
    assertFalse(t.getDefault(Attribute.LIFE) + " " + t.get(Attribute.LIFE), t.getDefault(Attribute.LIFE) == t.get(Attribute.LIFE));
  }

  public void testPromotion1() {
    final World world = new World(5, 5);
    final WoodElf elf = new WoodElf();
    elf.set(Attribute.COMBAT, 15);
    elf.setOwner(1);
    final Cell ec = world.getCell(0);
    ec.push(elf);
    final Cell tc = world.getCell(1);
    for (int k = 0; k < 5; ++k) {
      final Horse h = new Horse();
      h.setOwner(2);
      tc.push(h);
      assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(world, null, elf, ec, tc, NORMAL));
      assertEquals(State.DEAD, h.getState());
      assertEquals(0, h.get(Attribute.LIFE));
      if (k == 4) {
        assertEquals(k + 1, elf.getKillCount());
        assertEquals(State.ACTIVE, elf.getState());
        final Actor a = world.actor(0);
        assertTrue(a instanceof DreadElf);
        assertEquals(1, a.getOwner());
        assertEquals(State.ACTIVE, a.getState());
      } else {
        assertEquals(State.ACTIVE, elf.getState());
        assertEquals(k + 1, elf.getKillCount());
        assertEquals(elf, world.actor(0));
      }
    }
  }

  public void testPromotion2() {
    final World world = new World(5, 5);
    final WoodElf elf = new WoodElf();
    elf.set(Attribute.RANGED_COMBAT, 15);
    elf.setOwner(1);
    final Cell ec = world.getCell(0);
    ec.push(elf);
    final Cell tc = world.getCell(1);
    for (int k = 0; k < 5; ++k) {
      final Horse h = new Horse();
      h.setOwner(2);
      tc.push(h);
      assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(world, null, elf, ec, tc, RANGED));
      assertEquals(State.DEAD, h.getState());
      assertEquals(0, h.get(Attribute.LIFE));
      if (k == 4) {
        assertEquals(k + 1, elf.getKillCount());
        assertEquals(State.ACTIVE, elf.getState());
        final Actor a = world.actor(0);
        assertTrue(a instanceof DreadElf);
        assertEquals(1, a.getOwner());
        assertEquals(State.ACTIVE, a.getState());
      } else {
        assertEquals(State.ACTIVE, elf.getState());
        assertEquals(k + 1, elf.getKillCount());
        assertEquals(elf, world.actor(0));
      }
    }
  }

  public void testPromotion3() {
    final World world = new World(5, 5);
    final Horse elf = new Horse();
    elf.set(Attribute.COMBAT, 15);
    elf.setOwner(1);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    elf.setMount(wiz);
    final Cell ec = world.getCell(0);
    ec.push(elf);
    final Cell tc = world.getCell(1);
    final Horse h = new Horse();
    h.setOwner(2);
    tc.push(h);
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(world, null, elf, ec, tc, NORMAL));
    assertEquals(State.DEAD, h.getState());
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(1, elf.getKillCount());
    assertEquals(State.ACTIVE, elf.getState());
    final Actor a = world.actor(0);
    assertTrue(a instanceof Thundermare);
    assertEquals(1, a.getOwner());
    assertEquals(State.ACTIVE, a.getState());
    assertEquals(wiz, ((Mountable) a).getMount());
  }

  public void testNukedCombat() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Cell s = new Cell(0);
    s.push(w);
    final Cell t = new Cell(1);
    final Nuked n = new Nuked();
    n.setOwner(w.getOwner() + 1);
    t.push(n);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, RANGED));
    n.setOwner(w.getOwner());
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, RANGED));
  }

  public void testVariousCombat() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Cell s = new Cell(0);
    s.push(w);
    final Cell t = new Cell(1);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    t.push(l);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, RANGED));
    w.set(PowerUps.INVULNERABLE, 1);
    assertEquals(INVULNERABLE, CombatUtils.performCombat(null, w, w, s, s, RANGED));
    w.set(PowerUps.INVULNERABLE, 0);
    w.set(PowerUps.REFLECT, 1);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, s, RANGED));
    l.setState(State.ACTIVE);
    try {
      CombatUtils.performCombat(null, w, w, s, t, 100);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    w.set(Attribute.COMBAT, 6);
    l.set(Attribute.LIFE, 12);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(PowerUps.BATTLE_CRY, 1);
    l.set(Attribute.LIFE, 12);
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(PowerUps.BATTLE_CRY, 0);
    w.set(Attribute.COMBAT, 6);
    l.set(PowerUps.CLOAKED, 1);
    l.set(Attribute.LIFE, 6);
    l.setState(State.ACTIVE);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.setCombatApply(Attribute.INTELLIGENCE);
    w.set(Attribute.COMBAT, 6);
    l.setState(State.ACTIVE);
    l.set(PowerUps.CLOAKED, 0);
    l.set(Attribute.LIFE, 1);
    l.set(Attribute.INTELLIGENCE, 11);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(PowerUps.BATTLE_CRY, 1);
    l.set(Attribute.INTELLIGENCE, 11);
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(Attribute.COMBAT, 6);
    w.set(PowerUps.BATTLE_CRY, 0);
    l.set(PowerUps.CLOAKED, 1);
    l.set(Attribute.INTELLIGENCE, 5);
    l.setState(State.ACTIVE);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(Attribute.COMBAT, 6);
    w.setCombatApply(Attribute.MAGICAL_RESISTANCE);
    assertEquals(120, w.getScore());
    l.setState(State.ACTIVE);
    l.set(PowerUps.CLOAKED, 0);
    l.set(Attribute.LIFE, 1);
    l.set(Attribute.MAGICAL_RESISTANCE, 11);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(Attribute.COMBAT, 6);
    w.set(PowerUps.BATTLE_CRY, 1);
    l.set(Attribute.MAGICAL_RESISTANCE, 11);
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(Attribute.COMBAT, 6);
    w.set(PowerUps.BATTLE_CRY, 0);
    l.set(PowerUps.CLOAKED, 1);
    l.set(Attribute.MAGICAL_RESISTANCE, 5);
    l.setState(State.ACTIVE);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    w.set(Attribute.COMBAT, 6);
    l.setState(State.ACTIVE);
    l.set(PowerUps.CLOAKED, 0);
    l.set(Attribute.LIFE, 1);
    l.set(Attribute.MOVEMENT, 6);
    l.set(Attribute.RANGE, 4);
    l.set(Attribute.LIFE_RECOVERY, 2);
    l.set(Attribute.COMBAT, 0);
    l.set(Attribute.RANGED_COMBAT, 6);
    l.set(Attribute.SPECIAL_COMBAT, 6);
    w.set(Attribute.COMBAT, -3);
    w.setCombatApply(Attribute.COMBAT);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(3, l.get(Attribute.COMBAT));
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(6, l.get(Attribute.MOVEMENT));
    assertEquals(4, l.get(Attribute.RANGE));
    assertEquals(6, l.get(Attribute.COMBAT));
    assertEquals(6, l.get(Attribute.RANGED_COMBAT));
    assertEquals(6, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.MOVEMENT);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(4, l.get(Attribute.RANGE));
    assertEquals(6, l.get(Attribute.COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.RANGE);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(7, l.get(Attribute.RANGE));
    assertEquals(6, l.get(Attribute.COMBAT));
    assertEquals(9, l.get(Attribute.RANGED_COMBAT));
    assertEquals(6, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.COMBAT);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(7, l.get(Attribute.RANGE));
    assertEquals(9, l.get(Attribute.COMBAT));
    assertEquals(9, l.get(Attribute.RANGED_COMBAT));
    assertEquals(6, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.RANGED_COMBAT);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(10, l.get(Attribute.RANGE));
    assertEquals(9, l.get(Attribute.COMBAT));
    assertEquals(12, l.get(Attribute.RANGED_COMBAT));
    assertEquals(6, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.SPECIAL_COMBAT);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(10, l.get(Attribute.RANGE));
    assertEquals(9, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(2, l.get(Attribute.LIFE_RECOVERY));
    w.setCombatApply(Attribute.LIFE_RECOVERY);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(9, l.get(Attribute.MOVEMENT));
    assertEquals(10, l.get(Attribute.RANGE));
    assertEquals(5, l.get(Attribute.LIFE_RECOVERY));
  }

  public void testHorror() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Cell s = new Cell(0);
    s.push(w);
    final Cell t = new Cell(1);
    final Lion l = new Lion();
    t.push(l);
    l.set(Attribute.LIFE, 5);
    l.set(PowerUps.HORROR, 1);
    w.set(Attribute.COMBAT, 8);
    assertEquals(COMBAT_FAILED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(1, l.get(Attribute.LIFE));
    assertEquals(0, l.get(PowerUps.HORROR));
  }

  public void testNoReinstate() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Cell s = new Cell(0);
    s.push(w);
    final Cell t = new Cell(1);
    final EarthElemental l = new EarthElemental();
    t.push(l);
    l.set(Attribute.LIFE, 5);
    w.set(Attribute.COMBAT, 8);
    assertEquals(COMBAT_OK, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertTrue(t.peek() instanceof Earthquake);
  }

  public void testBonus() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Cell s = new Cell(0);
    s.push(w);
    final Cell t = new Cell(1);
    final StoneGolem l = new StoneGolem();
    t.push(l);
    l.set(Attribute.LIFE, 5);
    w.set(Attribute.COMBAT, 8);
    assertEquals(COMBAT_SUCCEEDED, CombatUtils.performCombat(null, w, w, s, t, NORMAL));
    assertEquals(1, w.getBonusSelect());
  }

  public void testSpecialCombatRange() {
    final World w = new World(3, 1);
    final WizardManager wm = w.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.LIFE, 1);
    wm.setWizard(1, wiz);
    w.getCell(0).push(wiz);
    final Lion l1 = new Lion();
    l1.setOwner(2);
    l1.set(Attribute.LIFE, 1);
    w.getCell(1).push(l1);
    final Lion l2 = new Lion();
    l2.setOwner(2);
    l2.set(Attribute.LIFE, 1);
    w.getCell(2).push(l2);
    wiz.set(Attribute.SPECIAL_COMBAT, 5);
    wiz.setSpecialCombatApply(Attribute.LIFE);
    CombatUtils.performSpecialCombat(w, new MoveMaster(w));
    assertEquals(State.DEAD, l1.getState());
    assertEquals(State.ACTIVE, l2.getState());
    assertEquals(State.ACTIVE, wiz.getState());
    assertEquals(wiz, w.actor(0));
    assertEquals(l2, w.actor(2));
    assertEquals(1, l2.get(Attribute.LIFE));
    assertTrue(wiz.get(Attribute.LIFE) <= 5);
    assertTrue(wiz.get(Attribute.LIFE) >= 1);
  }

  public void testLifeLeech() {
    final World w = new World(3, 1);
    final WizardManager wm = w.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.LIFE, 1);
    wm.setWizard(1, wiz);
    w.getCell(1).push(wiz);
    final Lion l1 = new Lion();
    l1.setOwner(2);
    l1.set(Attribute.COMBAT, 10);
    w.getCell(0).push(l1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    w.getCell(2).push(l2);
    wiz.set(PowerUps.LIFE_LEECH, 1);
    CombatUtils.performCombat(w, wm.getWizard(2), l1, w.getCell(0), w.getCell(1), NORMAL);
    assertEquals(State.ACTIVE, l2.getState());
    assertEquals(State.ACTIVE, wiz.getState());
    assertEquals(wiz, w.actor(1));
    assertEquals(l2, w.actor(2));
    assertEquals(l2.getDefault(Attribute.LIFE) - 10, l2.get(Attribute.LIFE));
    assertEquals(1, wiz.get(Attribute.LIFE));
  }

  public void testLifeLeechNoRescue() {
    final World w = new World(3, 1);
    final WizardManager wm = w.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.LIFE, 1);
    wm.setWizard(1, wiz);
    w.getCell(1).push(wiz);
    final Lion l1 = new Lion();
    l1.setOwner(2);
    l1.set(Attribute.COMBAT, 10);
    w.getCell(0).push(l1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    l2.set(PowerUps.LIFE_LEECH, 1);
    w.getCell(2).push(l2);
    wiz.set(PowerUps.LIFE_LEECH, 1);
    CombatUtils.performCombat(w, wm.getWizard(2), l1, w.getCell(0), w.getCell(1), NORMAL);
    assertEquals(State.ACTIVE, l2.getState());
    assertEquals(State.DEAD, wiz.getState());
    assertEquals(-9, wiz.get(Attribute.LIFE));
  }
}
