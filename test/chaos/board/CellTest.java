package chaos.board;

import chaos.Chaos;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.dragon.ShadowDragon;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.Generator;
import chaos.common.inanimate.MagicCastle;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Rock;
import chaos.common.inanimate.ShadowWood;
import chaos.common.inanimate.Tempest;
import chaos.common.monster.AirElemental;
import chaos.common.monster.BrownBear;
import chaos.common.monster.Centaur;
import chaos.common.monster.GiantBeetle;
import chaos.common.monster.Goblin;
import chaos.common.monster.GoblinBomb;
import chaos.common.monster.HiddenHorror;
import chaos.common.monster.Horse;
import chaos.common.monster.Hydra;
import chaos.common.monster.Lion;
import chaos.common.monster.Manticore;
import chaos.common.monster.Orc;
import chaos.common.monster.ShapeChanger;
import chaos.common.monster.Skeleton;
import chaos.common.monster.Spider;
import chaos.common.monster.Wraith;
import chaos.common.monster.Zombie;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class CellTest extends TestCase {

  public void testNumbering() {
    for (int i = -10; i < 10; ++i) {
      assertEquals(i, new Cell(i).getCellNumber());
    }
  }

  public void testPPP() {
    final Cell c = new Cell(0);
    assertEquals(null, c.peek());
    assertEquals(null, c.pop());
    assertEquals(null, c.peek());
    assertEquals(null, c.pop());
    final Lion lion = new Lion();
    c.push(lion);
    assertEquals(lion, c.peek());
    assertEquals(lion, c.pop());
    assertEquals(null, c.peek());
    assertEquals(null, c.pop());
    c.push(lion);
    final Hydra hydra = new Hydra();
    c.push(hydra);
    c.push(lion);
    assertEquals(lion, c.peek());
    assertEquals(lion, c.pop());
    assertEquals(hydra, c.peek());
    assertEquals(hydra, c.pop());
    assertEquals(lion, c.peek());
    assertEquals(lion, c.pop());
    assertEquals(null, c.peek());
    assertEquals(null, c.pop());
  }

  public void testPushNull() {
    final Cell c = new Cell(0);
    try {
      c.push(null);
      fail("Allowed null push");
    } catch (final RuntimeException e) {
      // ok
    }
  }

  public void testReinstateEmpty() {
    final Cell c = new Cell(0);
    assertTrue(c.reinstate());
    assertEquals(null, c.pop());
  }

  public void testReinstateCorpse() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    assertTrue(c.reinstate());
    assertEquals(null, c.pop());
  }

  public void testReinstateLion() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    c.push(lion);
    assertTrue(c.reinstate());
    assertEquals(lion, c.pop());
    assertEquals(State.DEAD, lion.getState());
    assertEquals(0, lion.get(Attribute.LIFE));
    assertEquals(0, lion.get(Attribute.LIFE_RECOVERY));
    assertEquals(0, lion.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, lion.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(0, lion.get(Attribute.INTELLIGENCE));
    assertEquals(0, lion.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(0, lion.get(Attribute.COMBAT));
    assertEquals(0, lion.get(Attribute.COMBAT_RECOVERY));
    assertEquals(0, lion.get(Attribute.RANGED_COMBAT));
    assertEquals(0, lion.get(Attribute.RANGED_COMBAT_RECOVERY));
    assertEquals(0, lion.get(Attribute.SPECIAL_COMBAT));
    assertEquals(0, lion.get(Attribute.SPECIAL_COMBAT_RECOVERY));
    assertEquals(0, lion.get(Attribute.AGILITY));
    assertEquals(0, lion.get(Attribute.AGILITY_RECOVERY));
    assertEquals(0, lion.get(Attribute.RANGE));
    assertEquals(0, lion.get(Attribute.RANGE_RECOVERY));
    assertEquals(0, lion.get(Attribute.MOVEMENT));
    assertEquals(0, lion.get(Attribute.MOVEMENT_RECOVERY));
  }

  public void testContrivedSpecialCase1() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    c.push(lion);
    final Skeleton skeleton = new Skeleton();
    c.push(skeleton);
    assertFalse(c.reinstate());
  }

  public void testContrivedSpecialCase2() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    c.push(lion);
    final GoblinBomb goblinBomb = new GoblinBomb();
    c.push(goblinBomb);
    assertFalse(c.reinstate());
  }

  public void testContrivedSpecialCase3() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    c.push(lion);
    final Horse horse = new Horse();
    horse.setState(State.DEAD);
    c.push(horse);
    assertFalse(c.reinstate());
  }

  public void testContrivedSpecialCase4() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    c.push(lion);
    final Wizard wizard = new Wizard1();
    wizard.setState(State.ACTIVE);
    c.push(wizard);
    assertFalse(c.reinstate());
  }

  public void testReinstateLionAsleep() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.ASLEEP);
    c.push(lion);
    assertTrue(c.reinstate());
    assertEquals(lion, c.pop());
    assertEquals(State.DEAD, lion.getState());
    assertEquals(null, c.pop());
  }

  public void testReinstateWraith() {
    final Cell c = new Cell(0);
    final Wraith wraith = new Wraith();
    c.push(wraith);
    assertTrue(c.reinstate());
    assertEquals(null, c.pop());
  }

  public void testReinstateMWEmpty() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final MagicWood mw = new MagicWood();
    c.push(mw);
    assertTrue(c.reinstate());
    assertEquals(lion, c.pop());
  }

  public void testReinstateMWOccupied() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final MagicWood mw = new MagicWood();
    final Orc orc = new Orc();
    mw.setMount(orc);
    c.push(mw);
    assertFalse(c.reinstate());
    assertEquals(orc, c.pop());
    assertEquals(lion, c.pop());
  }

  public void testReinstateMountNotMounted() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final Horse horse = new Horse();
    c.push(horse);
    c.reinstate();
    assertEquals(horse, c.pop());
    assertNull(c.pop());
  }

  public void testReinstateMountMounted() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final Horse horse = new Horse();
    final Orc orc = new Orc();
    horse.setMount(orc);
    c.push(horse);
    assertFalse(c.reinstate());
    assertEquals(orc, c.pop());
    assertEquals(lion, c.pop());
  }

  public void testReinstateRideableNotMounted() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final RedDragon redDragon = new RedDragon();
    c.push(redDragon);
    assertTrue(c.reinstate());
    assertEquals(redDragon, c.pop());
    assertNull(c.pop());
  }

  public void testReinstateRideableMounted() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final RedDragon redDragon = new RedDragon();
    final Orc orc = new Orc();
    redDragon.setMount(orc);
    c.push(redDragon);
    assertFalse(c.reinstate());
    assertEquals(orc, c.pop());
    assertEquals(lion, c.pop());
  }

  public void testReinstateReincarnation() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.set(PowerUps.REINCARNATE, 1);
    lion.setOwner(3);
    c.push(lion);
    assertFalse(c.reinstate());
    Actor a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof ShapeChanger);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof BrownBear);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof Goblin);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof Orc);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof Spider);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof GiantBeetle);
    assertEquals(3, a.getOwner());
    assertEquals(lion.getState(), a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
    assertTrue(!(a.is(PowerUps.REINCARNATE)));
    assertTrue(c.reinstate());
    a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof GiantBeetle);
    assertEquals(3, a.getOwner());
    assertEquals(State.DEAD, a.getState());
    assertEquals(lion.getRealm(), a.getRealm());
  }

  public void testReinstateReincarnationUnmountedMount() {
    final Cell c = new Cell(0);
    final Horse horse = new Horse();
    horse.set(PowerUps.REINCARNATE, horse.reincarnation() != null ? 1 : 0);
    horse.setOwner(3);
    c.push(horse);
    assertFalse(c.reinstate());
    final Actor a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof Spider);
    assertEquals(3, a.getOwner());
    assertEquals(horse.getState(), a.getState());
    assertEquals(horse.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
  }

  public void testReinstateReincarnationMountedMount() {
    final Cell c = new Cell(0);
    final Centaur centaur = new Centaur();
    centaur.set(PowerUps.REINCARNATE, centaur.reincarnation() != null ? 1 : 0);
    centaur.setOwner(3);
    final Orc orc = new Orc();
    centaur.setMount(orc);
    c.push(centaur);
    assertFalse(c.reinstate());
    Actor a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof Horse);
    assertEquals(3, a.getOwner());
    assertEquals(centaur.getState(), a.getState());
    assertEquals(centaur.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.pop();
    assertEquals(orc, a);
    assertFalse(a.is(PowerUps.REINCARNATE));
    assertEquals(null, c.pop());
  }

  public void testReinstateReincarnationMountedRideable() {
    final Cell c = new Cell(0);
    final RedDragon rd = new RedDragon();
    rd.set(PowerUps.REINCARNATE, rd.reincarnation() != null ? 1 : 0);
    rd.setOwner(3);
    final Orc orc = new Orc();
    rd.setMount(orc);
    c.push(rd);
    assertFalse(c.reinstate());
    Actor a = c.peek();
    assertTrue(a.getClass().getName(), a instanceof ShadowDragon);
    assertEquals(3, a.getOwner());
    assertEquals(rd.getState(), a.getState());
    assertEquals(rd.getRealm(), a.getRealm());
    assertTrue(a.is(PowerUps.REINCARNATE));
    assertFalse(c.reinstate());
    a = c.pop();
    assertEquals(orc, a);
    assertFalse(a.is(PowerUps.REINCARNATE));
    assertEquals(null, c.pop());
  }

  public void testReinstateWizardAndAssociatedThings() {
    final World w = Chaos.getChaos().getWorld();
    final Cell c = w.getCell(1);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.UNCERTAINTY, 5);
    wiz.set(PowerUps.MOVE_IT, 1);
    c.push(wiz);
    MagicWood mw = new MagicWood();
    mw.setOwner(wiz.getOwner());
    w.getCell(0).push(mw);
    mw = new MagicWood();
    mw.setOwner(wiz.getOwner() + 1);
    w.getCell(2).push(mw);
    final Generator g = new Generator();
    g.setOwner(wiz.getOwner());
    w.getCell(3).push(g);
    final Horse h = new Horse();
    h.setOwner(wiz.getOwner());
    w.getCell(4).push(h);
    final Horse hh = new Horse();
    hh.setOwner(wiz.getOwner());
    w.getCell(5).push(hh);
    w.getCell(5).push(new GooeyBlob());
    assertTrue(c.reinstate());
    assertEquals(null, w.actor(0));
    assertEquals(null, w.actor(1));
    assertEquals(mw, w.actor(2));
    assertEquals(null, w.actor(3));
    assertEquals(State.DEAD, wiz.getState());
    assertEquals(State.ASLEEP, h.getState());
    assertEquals(h, w.actor(4));
    assertEquals(0, h.getOwner());
    assertEquals(State.ASLEEP, hh.getState());
    assertEquals(0, hh.getOwner());
    assertEquals(0, wiz.get(PowerUps.UNCERTAINTY));
    assertEquals(0, wiz.get(PowerUps.MOVE_IT));
  }

  public void testReinstateLichLord() {
    final Cell c = new Cell(0);
    final Wizard1 w = new Wizard1();
    w.set(PowerUps.LICH_LORD, 1);
    w.setState(State.ACTIVE);
    c.push(w);
    assertFalse(c.reinstate());
    assertEquals(w, c.pop());
    assertEquals(0, w.get(PowerUps.LICH_LORD));
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(Realm.ETHERIC, w.getRealm());
    assertEquals(w.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
  }

  public void testReinstateLichLord2() {
    final Cell c = new Cell(0);
    final Wizard1 w = new Wizard1();
    w.set(PowerUps.LICH_LORD, 1);
    w.set(PowerUps.SWORD, 1);
    w.set(PowerUps.BOW, 1);
    w.setState(State.ACTIVE);
    c.push(w);
    assertFalse(c.reinstate());
    assertEquals(w, c.pop());
    assertEquals(0, w.get(PowerUps.LICH_LORD));
    assertEquals(0, w.get(PowerUps.SWORD));
    assertEquals(0, w.get(PowerUps.BOW));
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(Realm.ETHERIC, w.getRealm());
    assertEquals(w.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
  }

  public void testReinstateDeadRevenge() {
    final Cell c = new Cell(0);
    final Wizard1 w = new Wizard1();
    w.set(PowerUps.DEAD_REVENGE, 2);
    w.setState(State.ACTIVE);
    c.push(w);
    assertFalse(c.reinstate());
    assertTrue(c.peek() instanceof Generator);
    assertEquals(State.ACTIVE, w.getState());
    assertFalse(c.reinstate());
    // Second DR for this wizard
    assertTrue(c.peek() instanceof Generator);
    assertEquals(State.ACTIVE, w.getState());
    assertTrue(c.reinstate());
    assertEquals(State.DEAD, w.getState());
  }

  public void testReinstateBlob() {
    final Cell c = new Cell(0);
    final Manticore w = new Manticore();
    w.setState(State.ACTIVE);
    w.setMoved(true);
    c.push(w);
    final GooeyBlob g = new GooeyBlob();
    c.push(g);
    assertFalse(c.reinstate());
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(w.getDefault(Attribute.LIFE) >> 1, w.get(Attribute.LIFE));
    assertFalse(w.isMoved());
    assertEquals(0, w.getShotsMade());
    c.reinstate(); // kills manticore
    c.push(g);
    assertTrue(c.reinstate());
    c.pop(); // remove manticore
    c.push(g);
    assertTrue(c.reinstate());
    c.push(new Rock());
    c.push(g);
    assertFalse(c.reinstate());
    assertTrue(c.peek() instanceof Rock);
  }

  volatile int mTestValue = -1;

  public void testEvents() {
    final Cell c = new Cell(0);
    c.register(e -> mTestValue = ((CellEvent) e).getCellNumber());
    c.push(new Lion());
    assertEquals(0, mTestValue);
    mTestValue = -1;
    c.pop();
    assertEquals(0, mTestValue);
  }

  public void testContains() {
    final Cell c = new Cell(0);
    try {
      c.contains((Actor) null);
      fail("Nullo");
    } catch (final NullPointerException e) {
      // ok
    }
    final Lion l = new Lion();
    assertTrue(!c.contains(l));
    c.push(l);
    assertTrue(c.contains(l));
    assertTrue(!c.contains(new Lion()));
    assertTrue(!c.contains(new Hydra()));
    c.push(new Hydra());
    assertTrue(c.contains(l));
    assertTrue(!c.contains(new Lion()));
    assertTrue(!c.contains(new Hydra()));
    c.pop();
    assertTrue(c.contains(l));
    assertTrue(!c.contains(new Lion()));
    assertTrue(!c.contains(new Hydra()));
    c.pop();
    assertTrue(!c.contains(l));
    final Horse h = new Horse();
    h.setMount(l);
    c.push(h);
    assertTrue(c.contains(l));
    assertTrue(c.contains(h));
    assertTrue(!c.contains(new Lion()));
    assertTrue(!c.contains(new Hydra()));
    c.pop();
    final RedDragon d = new RedDragon();
    d.setMount(l);
    c.push(d);
    assertTrue(c.contains(l));
    assertTrue(c.contains(d));
    assertTrue(!c.contains(new Lion()));
    assertTrue(!c.contains(h));
  }

  public void testContains2() {
    final Cell c = new Cell(0);
    try {
      c.contains((Actor) null);
      fail("Nullo");
    } catch (final NullPointerException e) {
      // ok
    }
    final Lion l = new Lion();
    assertTrue(!c.contains(l.getClass()));
    assertTrue(!c.contains(Monster.class));
    c.push(l);
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(c.contains(Monster.class));
    assertTrue(!c.contains(Hydra.class));
    c.push(new Hydra());
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(c.contains(Hydra.class));
    c.pop();
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(!c.contains(Hydra.class));
    c.pop();
    assertTrue(!c.contains(l.getClass()));
    final Horse h = new Horse();
    h.setMount(l);
    c.push(h);
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(h.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(c.contains(Horse.class));
    assertTrue(!c.contains(Hydra.class));
    c.pop();
    final RedDragon d = new RedDragon();
    d.setMount(l);
    c.push(d);
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(d.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(!c.contains(h.getClass()));
    final MagicWood z = new MagicWood();
    z.setMount(l);
    c.push(z);
    assertTrue(c.contains(l.getClass()));
    assertTrue(c.contains(z.getClass()));
    assertTrue(c.contains(Lion.class));
    assertTrue(!c.contains(h.getClass()));
  }

  public void testDescribe() {
    final Cell c = new Cell(0);
    assertEquals("Nothing", c.describe());
    final Lion l = new Lion();
    l.setState(State.DEAD);
    c.push(l);
    assertEquals("Lion (dead)", c.describe());
    l.setState(State.ASLEEP);
    assertEquals("Lion (asleep)", c.describe());
    final Wizard w = new Wizard1();
    w.setPersonalName("Sean");
    w.setState(State.ACTIVE);
    Chaos.getChaos().getWorld().getWizardManager().setWizard(2, w);
    l.setState(State.ACTIVE);
    assertEquals("Lion (?, material)", c.describe());
    l.setOwner(2);
    assertEquals("Lion (Sean, material)", c.describe());
    l.setRealm(Realm.ETHERIC);
    assertEquals("Lion (Sean, undead)", c.describe());
    l.setRealm(Realm.DRACONIC);
    assertEquals("Lion (Sean, dragon)", c.describe());
    c.push(w);
    assertEquals("Sean (material)", c.describe());
    w.setRealm(Realm.DEMONIC);
    assertEquals("Sean (demon)", c.describe());
    w.setState(State.DEAD);
    assertEquals("Sean (dead)", c.describe());
    c.pop();
    assertEquals("Lion (Sean, dragon)", c.describe());
  }

  public void testGetMount() {
    final Cell c = new Cell(0);
    assertEquals(null, c.getMount());
    final Lion l = new Lion();
    c.push(l);
    assertEquals(null, c.getMount());
    c.pop();
    final Horse h = new Horse();
    c.push(h);
    assertEquals(null, c.getMount());
    h.setMount(l);
    assertEquals(l, c.getMount());
    c.pop();
    final RedDragon d = new RedDragon();
    c.push(d);
    assertEquals(null, c.getMount());
    d.setMount(l);
    assertEquals(l, c.getMount());
    c.pop();
    final MagicCastle m = new MagicCastle();
    c.push(m);
    assertEquals(null, c.getMount());
    m.setMount(l);
    assertEquals(l, c.getMount());
    c.pop();
    assertEquals(null, c.getMount());
  }

  public void testReinstateHH() {
    final Cell c = new Cell(0);
    final HiddenHorror hh = new HiddenHorror();
    hh.setState(State.ACTIVE);
    c.push(hh);
    assertFalse(c.reinstate());
    final Actor a = c.pop();
    assertTrue(a instanceof RedDragon);
    assertEquals(hh.getOwner(), a.getOwner());
    assertEquals(Realm.DRACONIC, a.getRealm());
    assertEquals(hh.getState(), a.getState());
    assertTrue(c.reinstate());
    assertEquals(null, c.pop());
  }

  public void testReinstateSWEmpty() {
    final Cell c = new Cell(0);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    c.push(lion);
    final ShadowWood mw = new ShadowWood();
    c.push(mw);
    assertTrue(c.reinstate());
    assertEquals(lion, c.pop());
  }

  public void testReinstateSleepingElemental() {
    final Cell c = new Cell(0);
    final AirElemental elem = new AirElemental();
    elem.setState(State.ASLEEP);
    c.push(elem);
    assertFalse(c.reinstate());
    final Actor a = c.peek();
    assertTrue(a instanceof Tempest);
    assertEquals(State.ASLEEP, a.getState());
  }

  public void testGoblinBomb() {
    final World w = Chaos.getChaos().getWorld();
    final Cell c = w.getCell(1);
    final GoblinBomb gb = new GoblinBomb();
    c.push(gb);
    final Lion deadLion = new Lion();
    deadLion.setState(State.DEAD);
    w.getCell(0).push(deadLion);
    final Lion liveLion = new Lion();
    w.getCell(2).push(liveLion);
    final Zombie z = new Zombie();
    z.set(Attribute.LIFE, 12);
    w.getCell(w.width() + 1).push(z);
    assertTrue(c.reinstate());
    assertNull(c.peek());
    assertNull(w.getCell(0).peek());
    assertNull(w.getCell(w.width() + 1).peek());
    assertEquals(liveLion, w.getCell(2).peek());
    assertEquals(liveLion.get(Attribute.LIFE), liveLion.getDefault(Attribute.LIFE) - 12);
  }

}
