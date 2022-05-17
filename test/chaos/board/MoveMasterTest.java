package chaos.board;

import chaos.Chaos;
import chaos.common.Attribute;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.dragon.RedDragon;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.Pit;
import chaos.common.inanimate.Roper;
import chaos.common.inanimate.ShadowWood;
import chaos.common.monster.Bat;
import chaos.common.monster.Centaur;
import chaos.common.monster.HiddenHorror;
import chaos.common.monster.Hippocrates;
import chaos.common.monster.Horse;
import chaos.common.monster.Hydra;
import chaos.common.monster.Imp;
import chaos.common.monster.Lion;
import chaos.common.monster.Manticore;
import chaos.common.monster.OgreMage;
import chaos.common.monster.Orc;
import chaos.common.monster.Skeleton;
import chaos.common.monster.Solar;
import chaos.common.monster.StoneGiant;
import chaos.common.monster.Thundermare;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import chaos.util.CombatUtils;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class MoveMasterTest extends TestCase {

  public void testActor() {
    final World w = new World(5, 5);
    w.getCell(12).push(new Lion());
    w.getCell(20).push(new Hydra());
    final MoveMaster mm = new MoveMaster(w);
    for (int i = -2; i < 100; ++i) {
      assertEquals(mm.actor(i), w.actor(i));
    }
  }

  public void testIsMoveable() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    final Hydra hydra = new Hydra();
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    lion.setOwner(p.getOwner());
    hydra.setOwner(lion.getOwner() + 1);
    w.getCell(3).push(mw);
    w.getCell(12).push(lion);
    w.getCell(20).push(hydra);
    final MoveMaster mm = new MoveMaster(w);
    assertTrue(mm.isMovable(p, 12));
    assertFalse(mm.isMovable(p, 20));
    assertFalse(mm.isMovable(p, 0));
    assertFalse(mm.isMovable(p, 3));
    assertFalse(mm.isMovable(p, 11));
    lion.setMoved(true);
    assertFalse(mm.isMovable(p, 12));
    lion.setMoved(false);
    assertTrue(mm.isMovable(p, 12));
    lion.setState(State.DEAD);
    assertFalse(mm.isMovable(p, 12));
    lion.setState(State.ASLEEP);
    assertFalse(mm.isMovable(p, 12));
  }
  public void testIsEngaged() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    final Hydra hydra = new Hydra();
    lion.setOwner(p.getOwner());
    hydra.setOwner(lion.getOwner() + 1);
    w.getCell(12).push(lion);
    w.getCell(13).push(hydra);
    final MoveMaster mm = new MoveMaster(w);
    for (int i = 0; i < 6; ++i) {
      assertFalse(mm.isEngaged(p, 0));
    }
    for (int i = 0; i < 6; ++i) {
      assertFalse(mm.isEngaged(p, 7));
    }
    for (int i = 0; i < 6; ++i) {
      assertFalse(mm.isEngaged(p, 0));
      assertFalse(mm.isEngaged(p, 7));
    }
    boolean sawTrueLion = false;
    boolean sawTrueHydra = false;
    for (int i = 0; i < 200; ++i) {
      lion.setEngaged(false);
      hydra.setEngaged(false);
      boolean e = mm.isEngaged(p, 12);
      if (e) {
        for (int j = 0; j < 10; ++j) {
          assertTrue(mm.isEngaged(p, 12));
          assertEquals(MoveMaster.ILLEGAL, mm.move(p, 12, 11));
        }
        sawTrueLion = true;
      }
      e = mm.isEngaged(p, 13);
      if (e) {
        for (int j = 0; j < 10; ++j) {
          assertTrue(mm.isEngaged(p, 13));
        }
        sawTrueHydra = true;
      }
    }
    assertTrue(sawTrueLion);
    assertTrue(sawTrueHydra);
  }

  public void testIsMountable() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    final Hydra hydra = new Hydra();
    final MagicWood mw = new MagicWood();
    final Horse horse = new Horse();
    final RedDragon rd = new RedDragon();
    mw.setOwner(p.getOwner());
    lion.setOwner(p.getOwner());
    hydra.setOwner(lion.getOwner() + 1);
    horse.setOwner(lion.getOwner() + 1);
    rd.setOwner(lion.getOwner() + 1);
    w.getCell(12).push(lion);
    w.getCell(13).push(hydra);
    w.getCell(14).push(mw);
    w.getCell(15).push(horse);
    w.getCell(16).push(rd);
    final MoveMaster mm = new MoveMaster(w);
    assertFalse(mm.isMountable(p, 0));
    assertFalse(mm.isMountable(p, 12));
    assertFalse(mm.isMountable(p, 13));
    assertTrue(mm.isMountable(p, 14));
    assertFalse(mm.isMountable(p, 15));
    horse.setOwner(p.getOwner());
    assertTrue(mm.isMountable(p, 15));
    horse.setState(State.DEAD);
    assertFalse(mm.isMountable(p, 15));
    horse.setState(State.ACTIVE);
    assertTrue(mm.isMountable(p, 15));
    horse.setMount(hydra);
    assertFalse(mm.isMountable(p, 15));
    assertFalse(mm.isMountable(p, 16));
    rd.setOwner(p.getOwner());
    assertFalse(mm.isMountable(p, 16));
    rd.setState(State.DEAD);
    assertFalse(mm.isMountable(p, 16));
    rd.setState(State.ACTIVE);
    assertFalse(mm.isMountable(p, 16));
    rd.setMount(hydra);
    assertFalse(mm.isMountable(p, 16));
    rd.setMount(null);
    assertFalse(mm.isMountable(p, 16));
    p.set(PowerUps.RIDE, 1);
    assertTrue(mm.isMountable(p, 16));
    rd.setOwner(p.getOwner());
    assertTrue(mm.isMountable(p, 16));
    rd.setState(State.DEAD);
    assertFalse(mm.isMountable(p, 16));
    rd.setState(State.ACTIVE);
    assertTrue(mm.isMountable(p, 16));
    rd.setMount(hydra);
    assertFalse(mm.isMountable(p, 16));
  }

  public void testIsAttackable() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    final Hydra hydra = new Hydra();
    lion.setOwner(p.getOwner());
    hydra.setOwner(lion.getOwner() + 1);
    w.getCell(12).push(lion);
    w.getCell(13).push(hydra);
    final MoveMaster mm = new MoveMaster(w);
    assertFalse(mm.isAttackable(5, 5));
    assertFalse(mm.isAttackable(5, 12));
    assertFalse(mm.isAttackable(5, 13));
    assertFalse(mm.isAttackable(12, 12));
    assertTrue(mm.isAttackable(12, 13));
    assertFalse(mm.isAttackable(12, 5));
    hydra.setState(State.DEAD);
    assertFalse(mm.isAttackable(12, 13));
    hydra.setState(State.ASLEEP);
    assertTrue(mm.isAttackable(12, 13));
    hydra.setOwner(p.getOwner());
    assertTrue(mm.isAttackable(12, 13));
    hydra.setOwner(p.getOwner() + 1);
    lion.setState(State.DEAD);
    assertFalse(mm.isAttackable(12, 13));
    lion.setState(State.ASLEEP);
    assertFalse(mm.isAttackable(12, 13));
    lion.setState(State.ACTIVE);
    assertTrue(mm.isAttackable(12, 13));
    final Skeleton skeleton = new Skeleton();
    skeleton.setOwner(lion.getOwner() + 1);
    w.getCell(14).push(skeleton);
    assertFalse(mm.isAttackable(12, 14));
    skeleton.setRealm(Realm.MATERIAL);
    assertTrue(mm.isAttackable(12, 14));
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    w.getCell(15).push(mw);
    assertFalse(mm.isAttackable(15, 14));
  }

  public void testGetSquaredMovementPoints() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner());
    w.getCell(12).push(lion);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    w.getCell(15).push(mw);
    final MoveMaster mm = new MoveMaster(w);
    assertEquals(0, mm.getSquaredMovementPoints(p, 0));
    assertEquals(0, mm.getSquaredMovementPoints(p, 1000));
    assertEquals(0, mm.getSquaredMovementPoints(p, 15));
    assertEquals(lion.get(Attribute.MOVEMENT) + lion.get(Attribute.MOVEMENT) * lion.get(Attribute.MOVEMENT), mm.getSquaredMovementPoints(p, 12));
    assertEquals(lion.get(Attribute.MOVEMENT) + lion.get(Attribute.MOVEMENT) * lion.get(Attribute.MOVEMENT), mm.getSquaredMovementPoints(p, 12));
    lion.set(Attribute.MOVEMENT, 0);
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    assertEquals(0, mm.getSquaredMovementPoints(p, 12));
    lion.set(Attribute.MOVEMENT, 1);
    assertEquals(0, mm.getSquaredMovementPoints(p, 12));
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    lion.setMoved(false);
    assertEquals(2, mm.getSquaredMovementPoints(p, 12));
    lion.set(Attribute.MOVEMENT, 3);
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    lion.setMoved(false);
    assertEquals(12, mm.getSquaredMovementPoints(p, 12));
    lion.setMoved(true);
    lion.set(Attribute.MOVEMENT, 0);
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    assertEquals(0, mm.getSquaredMovementPoints(p, 12));
    lion.set(Attribute.MOVEMENT, 1);
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    assertEquals(0, mm.getSquaredMovementPoints(p, 12));
    lion.set(Attribute.MOVEMENT, 3);
    assertEquals(0, mm.getSquaredMovementPoints(p, 2));
    assertEquals(0, mm.getSquaredMovementPoints(p, 12));
  }

  public void testConstantsAreDifferent() {
    assertTrue(MoveMaster.ILLEGAL != MoveMaster.OK);
    assertTrue(MoveMaster.ILLEGAL != MoveMaster.INVULNERABLE);
    assertTrue(MoveMaster.ILLEGAL != MoveMaster.COMBAT_FAILED);
    assertTrue(MoveMaster.ILLEGAL != MoveMaster.PARTIAL);
    assertTrue(MoveMaster.OK != MoveMaster.INVULNERABLE);
    assertTrue(MoveMaster.OK != MoveMaster.COMBAT_FAILED);
    assertTrue(MoveMaster.OK != MoveMaster.PARTIAL);
    assertTrue(MoveMaster.INVULNERABLE != MoveMaster.COMBAT_FAILED);
    assertTrue(MoveMaster.INVULNERABLE != MoveMaster.PARTIAL);
    assertTrue(MoveMaster.COMBAT_FAILED != MoveMaster.PARTIAL);
  }

  private World prepareWorld(final Wizard p) {
    final World w = new World(5, 5);
    Hydra hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    hydra.setState(State.DEAD);
    w.getCell(7).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner() + 1);
    hydra.setState(State.ASLEEP);
    w.getCell(17).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    hydra.setState(State.ASLEEP);
    w.getCell(18).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner() + 1);
    w.getCell(5).push(hydra);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner());
    w.getCell(12).push(lion);
    assertTrue(!lion.is(PowerUps.FLYING));
    return w;
  }

  public void testMoveIllegal() {
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final World w = prepareWorld(p);
    final Bat bat = new Bat();
    bat.setOwner(p.getOwner());
    bat.set(Attribute.MOVEMENT, 3); // the board is small
    w.getCell(20).push(bat);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    w.getCell(9).push(mw);
    final MoveMaster mm = new MoveMaster(w);
    // move empty cell
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 0, 1));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 0, -1));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, -1, -1));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, -1, 0));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 1, 6));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 24, 25));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 25, 24));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 27, 24));
    // move cell not owned
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 5, 0));
    // move corpse
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 7, 8));
    // move out of range
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 12, 4));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 4));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 9));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 14));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 19));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 24));
    // move idempotence
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 0, 0));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 12, 12));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 20));
    // move onto owned
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 12));
    // move sleeping
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 18, 19));
    // move non-monster
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 9, 8));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 9, 13));
    // mark bat as moved
    bat.setMoved(true);
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 20, 22));
  }

  public void testFlyingMovement() {
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final World w = prepareWorld(p);
    final StringListener l = new StringListener("Bat: movement points = 3.46 (flying)");
    final MoveMaster mm = new MoveMaster(w);
    mm.register(l);
    try {
      final Bat bat = new Bat();
      bat.setOwner(p.getOwner());
      bat.set(Attribute.MOVEMENT, 3); // the board is small
      w.getCell(20).push(bat);
      final MagicWood mw = new MagicWood();
      mw.setOwner(p.getOwner());
      w.getCell(9).push(mw);
      // flight onto empty cell
      assertTrue(bat.is(PowerUps.FLYING));
      assertTrue(mm.isMovable(p, 20));
      assertEquals(MoveMaster.OK, mm.move(p, 20, 22));
      assertEquals(null, w.actor(20));
      assertEquals(bat, w.actor(22));
      assertTrue(bat.isMoved());
      assertEquals(MoveMaster.ILLEGAL, mm.move(p, 22, 20));
      // flight onto corpse
      assertTrue(mm.isMovable(p, 12)); // refocus attention
      bat.setMoved(false);
      assertTrue(mm.actor(7).getState() == State.DEAD);
      assertTrue(mm.actor(7) instanceof Hydra);
      assertTrue(mm.actor(22) instanceof Bat);
      assertEquals(3, mm.actor(22).get(Attribute.MOVEMENT));
      assertTrue(mm.isMovable(p, 22));
      assertFalse(mm.isAttackable(22, 7));
      assertEquals(MoveMaster.OK, mm.move(p, 22, 7));
      assertEquals(null, w.actor(22));
      assertEquals(bat, w.actor(7));
      assertTrue(bat.isMoved());
      assertEquals(MoveMaster.ILLEGAL, mm.move(p, 7, 22));
      // flight onto tree
      assertTrue(mm.isMovable(p, 12)); // refocus attention
      bat.setMoved(false);
      assertTrue(mm.isMovable(p, 7));
      assertEquals(MoveMaster.ILLEGAL, mm.move(p, 7, 9));
      assertEquals(mw, w.actor(9));
      assertEquals(bat, w.actor(7));
      // fly next to enemy hydra
      assertEquals(MoveMaster.PARTIAL, mm.move(p, 7, 1));
      assertEquals(bat, w.actor(1));
      assertTrue(w.actor(7) instanceof Hydra);
      final Hydra hh = (Hydra) w.actor(7);
      assertEquals(State.DEAD, hh.getState());
    } finally {
      mm.deregister(l);
    }
    assertTrue(l.seen());
  }

  public void testFlyingMovementD1() {
    final World w = new World(5, 5);
    final StringListener l = new StringListener("Bat: movement points = 1.41 (flying)");
    final MoveMaster mm = new MoveMaster(w);
    mm.register(l);
    try {
      final Wizard p = new Wizard1();
      p.setState(State.ACTIVE);
      final Bat bat = new Bat();
      bat.setOwner(p.getOwner());
      bat.set(Attribute.MOVEMENT, 1);
      w.getCell(20).push(bat);
      assertTrue(bat.is(PowerUps.FLYING));
      assertTrue(mm.isMovable(p, 20));
      assertEquals(MoveMaster.OK, mm.move(p, 20, 21));
    } finally {
      mm.deregister(l);
    }
    assertTrue(l.seen());
  }

  public void testWalkingMovementMsg() {
    final World w = new World(3, 3);
    final StringListener l = new StringListener("Lion: movement points = 1.00");
    final MoveMaster mm = new MoveMaster(w);
    mm.register(l);
    try {
      final Wizard p = new Wizard1();
      p.setState(State.ACTIVE);
      final Lion lion = new Lion();
      lion.setOwner(p.getOwner());
      lion.set(Attribute.MOVEMENT, 2);
      w.getCell(0).push(lion);
      assertEquals(MoveMaster.PARTIAL, mm.move(p, 0, 4));
      assertEquals(MoveMaster.OK, mm.move(p, 4, 5));
    } finally {
      mm.deregister(l);
    }
    assertTrue(l.seen());
  }

  public void testGroundMovement() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner());
    w.getCell(12).push(lion);
    Hydra hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    hydra.setState(State.DEAD);
    w.getCell(7).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner() + 1);
    hydra.setState(State.ASLEEP);
    w.getCell(17).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    hydra.setState(State.ASLEEP);
    w.getCell(18).push(hydra);
    hydra = new Hydra();
    hydra.setOwner(p.getOwner() + 1);
    w.getCell(5).push(hydra);
    final Bat bat = new Bat();
    bat.setOwner(p.getOwner());
    bat.set(Attribute.MOVEMENT, 4); // the board is small
    w.getCell(20).push(bat);
    assertTrue(!lion.is(PowerUps.FLYING));
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    w.getCell(9).push(mw);
    final MoveMaster mm = new MoveMaster(w);
    // step onto empty cell
    assertTrue(!lion.is(PowerUps.FLYING));
    assertTrue(mm.isMovable(p, 12));
    assertFalse(mm.isEngaged(p, 12));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 8));
    assertEquals(null, w.actor(12));
    assertEquals(lion, w.actor(8));
    assertTrue(!lion.isMoved());
    // step onto corpse
    assertTrue(mm.isMovable(p, 8));
    assertFalse(mm.isAttackable(8, 7));
    assertFalse(mm.isEngaged(p, 8));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 8, 7));
    assertEquals(null, w.actor(8));
    assertEquals(lion, w.actor(7));
    assertTrue(!bat.isMoved());
    // step next to enemy
    assertTrue(mm.isMovable(p, 7));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 7, 6));
    assertEquals(lion, w.actor(6));
    assertTrue(w.actor(7) instanceof Hydra);
    final Hydra hh = (Hydra) w.actor(7);
    assertEquals(State.DEAD, hh.getState());
    assertTrue(mm.isEngaged(p, 6));
  }

  public void testGroundMovement2() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final MoveMaster mm = new MoveMaster(w);

    // NW
    Hydra hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 6));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(6));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 6, 12));

    // N
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 7));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(7));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 7, 12));

    // NE
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 8));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(8));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 8, 12));

    // E
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 13));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(13));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 13, 12));

    // SE
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 18));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(18));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 18, 12));

    // S
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 17));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(17));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 17, 12));

    // SW
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 16));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(16));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 16, 12));

    // W
    hydra = new Hydra();
    hydra.setOwner(p.getOwner());
    w.getCell(12).push(hydra);
    assertEquals(MoveMaster.OK, mm.move(p, 12, 11));
    assertEquals(null, w.actor(12));
    assertEquals(hydra, w.actor(11));
    assertTrue(hydra.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 11, 12));
  }

  public void testGroundMovement3() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final MoveMaster mm = new MoveMaster(w);

    // NW
    StoneGiant sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 6));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(6));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 6, 0));
    assertEquals(MoveMaster.OK, mm.move(p, 6, 1));
    assertTrue(sg.isMoved());

    // N
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 7));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(7));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.OK, mm.move(p, 7, 3));
    assertTrue(sg.isMoved());

    // NE
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 8));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(8));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 8, 2));
    assertEquals(MoveMaster.OK, mm.move(p, 8, 9));
    assertTrue(sg.isMoved());

    // E
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 13));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(13));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.OK, mm.move(p, 13, 14));
    assertTrue(sg.isMoved());

    // SE
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 18));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(18));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 18, 24));
    assertEquals(MoveMaster.OK, mm.move(p, 18, 19));
    assertTrue(sg.isMoved());

    // S
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 17));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(17));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.OK, mm.move(p, 17, 23));
    assertTrue(sg.isMoved());

    // SW
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 16));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(16));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 16, 20));
    assertEquals(MoveMaster.OK, mm.move(p, 16, 21));
    assertTrue(sg.isMoved());

    // W
    sg = new StoneGiant();
    sg.setOwner(p.getOwner());
    w.getCell(12).push(sg);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 12, 11));
    assertEquals(null, w.actor(12));
    assertEquals(sg, w.actor(11));
    assertTrue(!sg.isMoved());
    assertEquals(MoveMaster.OK, mm.move(p, 11, 12));
    assertTrue(sg.isMoved());
  }

  public void testMeditation() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    final Wizard p2 = new Wizard1();
    p1.setState(State.ACTIVE);
    p2.setState(State.ACTIVE);
    final MagicWood mw1 = new MagicWood();
    mw1.setOwner(p1.getOwner());
    w.getCell(9).push(mw1);
    final MagicWood mw2 = new MagicWood();
    mw2.setOwner(p1.getOwner());
    w.getCell(14).push(mw2);
    w.getCell(3).push(p1);
    final Lion lion = new Lion();
    lion.setOwner(p1.getOwner());
    lion.setState(State.DEAD);
    w.getCell(13).push(lion);
    w.getCell(13).push(p2);
    final MoveMaster mm = new MoveMaster(w);
    // step into own tree
    assertEquals(p1.getOwner(), w.actor(3).getOwner());
    assertTrue(w.actor(3) instanceof Monster);
    assertEquals(1, w.actor(3).get(Attribute.MOVEMENT));
    assertTrue(w.actor(9) instanceof Meditation);
    assertEquals(MoveMaster.OK, mm.move(p1, 3, 9));
    assertEquals(null, w.actor(3));
    assertEquals(mw1, w.actor(9));
    assertEquals(p1, mw1.getMount());
    assertTrue(p1.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 9, 3));
    // step into someone elses tree (also reveal corpse)
    assertEquals(MoveMaster.OK, mm.move(p2, 13, 14));
    assertEquals(lion, w.actor(13));
    assertEquals(mw2, w.actor(14));
    assertEquals(p1.getOwner(), w.actor(14).getOwner());
    assertEquals(p2, mw2.getMount());
    assertTrue(p2.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 14, 13));
  }

  public void testMeditation2() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    final Wizard p2 = new Wizard1();
    p1.setState(State.ACTIVE);
    p2.setState(State.ACTIVE);
    final MagicWood mw1 = new MagicWood();
    mw1.setOwner(p1.getOwner());
    w.getCell(9).push(mw1);
    w.getCell(3).push(p1);
    w.getCell(13).push(p2);
    final MoveMaster mm = new MoveMaster(w);
    // step into someone elses tree
    assertEquals(MoveMaster.OK, mm.move(p2, 13, 9));
    assertEquals(mw1, w.actor(9));
    assertEquals(p1.getOwner(), w.actor(9).getOwner());
    assertEquals(p2, mw1.getMount());
    assertTrue(p2.isMoved());
    // step into own occupied tree
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 3, 9));
    assertEquals(p1, w.actor(3));
    assertEquals(mw1, w.actor(9));
    assertEquals(p2, mw1.getMount());
  }

  public void testMeditation3() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    final Wizard p2 = new Wizard1();
    p1.setState(State.ACTIVE);
    p1.setOwner(0);
    p2.setState(State.ACTIVE);
    p2.setOwner(1);
    final MagicWood mw1 = new MagicWood();
    mw1.setOwner(p2.getOwner());
    w.getCell(9).push(mw1);
    w.getCell(3).push(p1);
    w.getCell(13).push(p2);
    final MoveMaster mm = new MoveMaster(w);
    // step into own tree
    assertEquals(MoveMaster.OK, mm.move(p2, 13, 9));
    assertEquals(mw1, w.actor(9));
    assertEquals(p1.getOwner(), w.actor(3).getOwner());
    assertEquals(p2.getOwner(), w.actor(9).getOwner());
    assertEquals(p2, mw1.getMount());
    assertTrue(p2.isMoved());
    // step into occupied enemy tree -- combat occurs
    assertTrue(mm.isMovable(p1, 3));
    assertTrue(mm.isAttackable(3, 9));
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(p1, 3, 9));
    assertEquals(p1, w.actor(3));
    assertEquals(mw1, w.actor(9));
    assertEquals(p2, mw1.getMount());
    assertTrue(p2.isMoved());
  }

  public void testMounting() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Horse horse = new Horse();
    horse.setOwner(p1.getOwner());
    w.getCell(9).push(horse);
    w.getCell(8).push(p1);
    final MoveMaster mm = new MoveMaster(w);
    // step into own horse
    assertEquals(MoveMaster.OK, mm.move(p1, 8, 9));
    assertEquals(horse, w.actor(9));
    assertEquals(null, w.actor(8));
    assertEquals(p1, horse.getMount());
    assertTrue(p1.isMoved());
    // try remount, this should be illegal because already mounted
    w.getCell(8).push(p1);
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 8, 9));
    assertEquals(horse, w.actor(9));
    assertEquals(p1, w.actor(8));
    assertEquals(p1, horse.getMount());
    w.getCell(8).pop(); // drop extra wizard
    // now move the horse and make sure mount is retained
    assertEquals(MoveMaster.PARTIAL, mm.move(p1, 9, 13));
    assertEquals(horse, w.actor(13));
    assertEquals(null, w.actor(9));
    assertEquals(p1, horse.getMount());
    assertTrue(p1.isMoved());
  }

  public void testMounting2() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Horse horse = new Horse();
    horse.setOwner(p1.getOwner() + 1);
    w.getCell(9).push(horse);
    w.getCell(8).push(p1);
    final MoveMaster mm = new MoveMaster(w);
    // step into own horse
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(p1, 8, 9));
    assertEquals(horse, w.actor(9));
    assertEquals(p1, w.actor(8));
    assertEquals(null, horse.getMount());
  }

  public void testSimpleKill() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(null, w.actor(16));
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    // sg should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    sg.setMoved(false);
    sg.setEngaged(false);
    assertEquals(MoveMaster.PARTIAL, mm.move(p1, 12, 7));
    assertEquals(sg, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }

  // as above but with a sleeping creature
  public void testSimpleKillOfAsleep() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    orc.setState(State.ASLEEP);
    w.getCell(12).push(orc);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(null, w.actor(16));
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    // sg should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    sg.setMoved(false);
    assertEquals(MoveMaster.PARTIAL, mm.move(p1, 12, 7));
    assertEquals(sg, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testFlyingKill() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final RedDragon sg = new RedDragon();
    sg.set(Attribute.COMBAT, 15);
    sg.setOwner(p1.getOwner());
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    w.getCell(20).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 20, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(null, w.actor(20));
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    // sg should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    sg.setMoved(false);
    assertEquals(MoveMaster.OK, mm.move(p1, 12, 7));
    assertEquals(sg, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testAttackerOnCorpseKill() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    w.getCell(16).push(lion);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(lion, w.actor(16));
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    // sg should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    sg.setMoved(false);
    sg.setEngaged(false);
    assertEquals(MoveMaster.PARTIAL, mm.move(p1, 12, 7));
    assertEquals(sg, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }


  public void testMountedAttack() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Horse horse = new Horse();
    horse.setOwner(p1.getOwner());
    horse.setMount(p1);
    horse.set(Attribute.COMBAT, 15);
    w.getCell(16).push(horse);
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with horse, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertTrue(w.actor(12) instanceof Thundermare);
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    assertEquals(p1, horse.getMount());
    // horse should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    w.getCell(12).pop();
    w.getCell(12).push(horse);
    horse.setMoved(false);
    horse.setEngaged(false);
    assertEquals(MoveMaster.PARTIAL, mm.move(p1, 12, 7));
    assertEquals(horse, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testRideableAttack() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final RedDragon redDragon = new RedDragon();
    redDragon.setOwner(p1.getOwner());
    redDragon.setMount(p1);
    redDragon.set(Attribute.COMBAT, 15);
    w.getCell(16).push(redDragon);
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with redDragon, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(redDragon, w.actor(12));
    assertEquals(0, orc.get(Attribute.LIFE));
    assertEquals(State.DEAD, orc.getState());
    assertEquals(p1, redDragon.getMount());
    // redDragon should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 12, 7));
    redDragon.setMoved(false);
    redDragon.setEngaged(false);
    assertEquals(MoveMaster.OK, mm.move(p1, 12, 7));
    assertEquals(redDragon, w.actor(7));
    assertEquals(orc, w.actor(12));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testSimpleKillFails() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    orc.set(Attribute.LIFE, 60);
    w.getCell(12).push(orc);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should fail
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(16));
    assertEquals(orc, w.actor(12));
    assertEquals(60 - sg.get(Attribute.COMBAT), orc.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, orc.getState());
    // sg should no longer be able to move
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 16, 11));
  }

  public void testSimpleKillMR() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    sg.setCombatApply(Attribute.MAGICAL_RESISTANCE);
    sg.set(Attribute.COMBAT, 100); // higher than normally possible
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(null, w.actor(16));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testSimpleKillIntelligence() {
    final World w = new World(5, 5);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final StoneGiant sg = new StoneGiant();
    sg.setOwner(p1.getOwner());
    sg.setCombatApply(Attribute.INTELLIGENCE);
    sg.set(Attribute.COMBAT, 100); // higher than normally possible
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner() + 1);
    w.getCell(12).push(orc);
    w.getCell(16).push(sg);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.OK, mm.move(p1, 16, 12));
    assertEquals(sg, w.actor(12));
    assertEquals(null, w.actor(16));
    assertEquals(State.DEAD, orc.getState());
  }

  public void testMountingBug() {
    final World w = new World(2, 1);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Manticore sg = new Manticore();
    sg.setOwner(p1.getOwner());
    sg.setState(State.ACTIVE);
    w.getCell(0).push(sg);
    final Orc orc = new Orc();
    orc.setOwner(p1.getOwner());
    orc.setState(State.ACTIVE);
    w.getCell(1).push(orc);
    final MoveMaster mm = new MoveMaster(w);
    // attack orc with giant, should kill
    assertEquals(MoveMaster.ILLEGAL, mm.move(p1, 1, 0));
  }

  public void testMountingSleepBug() {
    final World w = new World(2, 1);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    final Manticore sleepyManticore = new Manticore();
    sleepyManticore.setOwner(p1.getOwner());
    sleepyManticore.setState(State.ASLEEP);
    w.getCell(0).push(sleepyManticore);
    w.getCell(1).push(p1);
    final MoveMaster mm = new MoveMaster(w);
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(p1, 1, 0)); // Attack
    assertEquals(p1, w.actor(1));
    assertEquals(sleepyManticore, w.actor(0));
  }

  public void testFlyingMovementToAdjacentWithEngagement() {
    final World w = new World(3, 3);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner() + 1);
    w.getCell(0).push(lion);
    final Bat bat = new Bat();
    bat.setOwner(p.getOwner());
    w.getCell(8).push(bat);
    final MoveMaster mm = new MoveMaster(w);
    // flight onto empty cell by lion
    assertTrue(bat.is(PowerUps.FLYING));
    assertTrue(mm.isMovable(p, 8));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 8, 4));
    assertTrue(mm.isEngaged(p, 4));
    assertTrue(mm.isEngaged(p, 4));
  }

  public void testWalkingMovementToAdjacentWithEngagement() {
    final World w = new World(3, 3);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner() + 1);
    w.getCell(0).push(lion);
    final Lion bat = new Lion();
    bat.setOwner(p.getOwner());
    w.getCell(8).push(bat);
    final MoveMaster mm = new MoveMaster(w);
    // flight onto empty cell by lion
    assertTrue(!bat.is(PowerUps.FLYING));
    assertTrue(mm.isMovable(p, 8));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 8, 4));
    assertTrue(mm.isEngaged(p, 4));
    assertTrue(mm.isEngaged(p, 4));
  }

  public void testIsShootable1() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    for (int i = 0; i < 9; ++i) {
      for (int j = 0; j < 9; ++j) {
        assertFalse(mm.isShootable(p, i, j));
      }
    }
    assertFalse(mm.isShootable(p, -1));
    assertFalse(mm.isShootable(p, w.size()));
  }

  public void testIsShootable1C() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    for (int i = 0; i < 9; ++i) {
      for (int j = 0; j < 9; ++j) {
        assertFalse(mm.isShootableConveyance(p, i, j));
      }
    }
    assertFalse(mm.isShootableConveyance(p, -1));
    assertFalse(mm.isShootableConveyance(p, w.size()));
  }

  public void testIsShootable2() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    for (int j = 0; j < 9; ++j) {
      assertTrue(mm.isShootable(p, 0, j));
    }
  }

  public void testIsShootable2C() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.set(Attribute.RANGE, 5);
    p.set(Attribute.RANGED_COMBAT, 5);
    p.set(Attribute.SHOTS, 1);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner() + 1);
    mw.setMount(p);
    w.getCell(0).push(mw);
    for (int j = 0; j < 9; ++j) {
      assertTrue(mm.isShootableConveyance(p, 0, j));
    }
  }

  public void testIsShootable3() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion elf = new Lion();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    for (int j = -1; j < 10; ++j) {
      assertFalse(mm.isShootable(p, 0, j));
    }
  }

  public void testIsShootable4() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    elf.setShotsMade(1);
    w.getCell(0).push(elf);
    for (int j = 0; j < 9; ++j) {
      assertFalse(mm.isShootable(p, 0, j));
    }
  }

  public void testIsShootable5() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner() + 1);
    w.getCell(0).push(elf);
    for (int j = 0; j < 9; ++j) {
      assertFalse(mm.isShootable(p, 0, j));
    }
  }

  public void testIsShootable6() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    elf.setState(State.DEAD);
    w.getCell(0).push(elf);
    for (int j = 0; j < 9; ++j) {
      assertFalse(mm.isShootable(p, 0, j));
    }
  }

  public void testIsShootable7() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    elf.setState(State.ASLEEP);
    w.getCell(0).push(elf);
    for (int j = 0; j < 9; ++j) {
      assertFalse(mm.isShootable(p, 0, j));
    }
  }


  public void testIsShootable8() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    elf.set(Attribute.RANGE, 6);
    w.getCell(0).push(elf);
    assertTrue(mm.isShootable(p, 0, 0));
    assertTrue(mm.isShootable(p, 0, 1));
    assertTrue(mm.isShootable(p, 0, 2));
    assertTrue(mm.isShootable(p, 0, 3));
    assertTrue(mm.isShootable(p, 0, 4));
    assertTrue(mm.isShootable(p, 0, 5));
    assertTrue(mm.isShootable(p, 0, 6));
    assertFalse(mm.isShootable(p, 0, 7));
    assertFalse(mm.isShootable(p, 0, 8));
    assertFalse(mm.isShootable(p, 0, 9));
  }

  public void testIsShootable9() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    elf.set(Attribute.RANGE, 6);
    // elf is an archer, can see over this obstruction
    w.getCell(0).push(elf);
    final Lion l = new Lion();
    w.getCell(3).push(l);
    assertTrue(mm.isShootable(p, 0, 0));
    assertTrue(mm.isShootable(p, 0, 1));
    assertTrue(mm.isShootable(p, 0, 2));
    assertTrue(mm.isShootable(p, 0, 3));
    assertTrue(mm.isShootable(p, 0, 4));
    assertTrue(mm.isShootable(p, 0, 5));
    assertTrue(mm.isShootable(p, 0, 6));
    assertFalse(mm.isShootable(p, 0, 7));
    assertFalse(mm.isShootable(p, 0, 8));
    assertFalse(mm.isShootable(p, 0, 9));
  }

  public void testIsShootable10() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final RedDragon elf = new RedDragon();
    elf.setOwner(p.getOwner());
    elf.set(Attribute.RANGE, 6);
    w.getCell(0).push(elf);
    final Lion l = new Lion();
    w.getCell(3).push(l);
    assertTrue(mm.isShootable(p, 0, 0));
    assertTrue(mm.isShootable(p, 0, 1));
    assertTrue(mm.isShootable(p, 0, 2));
    assertTrue(mm.isShootable(p, 0, 3));
    assertFalse(mm.isShootable(p, 0, 4));
    assertFalse(mm.isShootable(p, 0, 5));
    assertFalse(mm.isShootable(p, 0, 6));
    assertFalse(mm.isShootable(p, 0, 7));
    assertFalse(mm.isShootable(p, 0, 8));
    assertFalse(mm.isShootable(p, 0, 9));
  }

  public void testIsShootable11() {
    final World w = new World(3, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setOwner(1);
    p.setState(State.ACTIVE);
    w.getCell(2).push(p);
    final Wizard p2 = new Wizard1();
    p2.setOwner(2);
    p2.setState(State.ACTIVE);
    w.getCell(1).push(p2);
    final WoodElf elf = new WoodElf();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    assertTrue(mm.isShootable(p, 0, 1));
    elf.setShotsMade(1);
    assertFalse(mm.isShootable(p, 0, 1));
  }

  public void testDismount() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final RedDragon elf = new RedDragon();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    final Lion lion = new Lion();
    w.getCell(5).push(lion);
    for (int i = -1; i <= w.size(); ++i) {
      assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, i, i));
    }
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, -1, 0));
    elf.setMount(p);
    // try to dismount off board
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, -1));
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, -2));
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, w.size()));
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, w.size() + 1));
    // not a mount
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 6, 7));
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 5, 6));
    // already moved
    p.setMoved(true);
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 1));
    p.setMoved(false);
    // too far
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 2));
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 3));
    // valid
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertEquals(null, elf.getMount());
    assertTrue(p.isMoved());
  }

  public void testDismountOntoCorpse() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final RedDragon elf = new RedDragon();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    elf.setMount(p);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertEquals(null, elf.getMount());
    assertTrue(p.isMoved());
  }

  public void testDismountOntoNuked() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final RedDragon elf = new RedDragon();
    elf.setOwner(p.getOwner());
    w.getCell(0).push(elf);
    elf.setMount(p);
    final Nuked lion = new Nuked();
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 1));
    assertFalse(p.isMoved());
  }

  public void testDismountAttackAndKill() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.set(Attribute.COMBAT, 15);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    mw.setMount(p);
    w.getCell(0).push(mw);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner() + 1);
    lion.set(Attribute.LIFE, 1);
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(null, mw.getMount());
  }

  public void testDismountAttackInvul() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    mw.setMount(p);
    w.getCell(0).push(mw);
    final Wizard wi = new Wizard1();
    wi.setOwner(p.getOwner() + 1);
    wi.setState(State.ACTIVE);
    wi.set(PowerUps.INVULNERABLE, 2);
    w.getCell(1).push(wi);
    assertEquals(MoveMaster.INVULNERABLE, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 1));
  }

  public void testDismountOntoMeditation() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final RedDragon mw = new RedDragon();
    mw.setOwner(p.getOwner());
    mw.setMount(p);
    w.getCell(0).push(mw);
    final MagicWood lion = new MagicWood();
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(null, mw.getMount());
    assertEquals(p, lion.getMount());
  }

  public void testDismountOntoMeditation2() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final MagicWood mw = new MagicWood();
    mw.setMount(p);
    w.getCell(0).push(mw);
    final RedDragon rd = new RedDragon();
    rd.setOwner(p.getOwner());
    w.getCell(1).push(rd);
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 1));
    assertFalse(p.isMoved());
    assertEquals(p, mw.getMount());
    assertEquals(null, rd.getMount());
    p.set(PowerUps.RIDE, 1);
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(null, mw.getMount());
    assertEquals(p, rd.getMount());
  }

  public void testDismountAdjacentEngage() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.setOwner(1);
    final RedDragon mw = new RedDragon();
    mw.setOwner(p.getOwner());
    w.getCell(0).push(mw);
    mw.setMount(p);
    final Lion lion = new Lion();
    lion.setOwner(3);
    lion.setState(State.ACTIVE);
    w.getCell(2).push(lion);
    assertEquals(MoveMaster.PARTIAL, mm.dismount(p, 0, 1));
    assertEquals(null, mw.getMount());
    assertFalse(p.isMoved());
    assertEquals(p, w.actor(1));
  }

  public void testAlternatingMoveBug() {
    final World w = new World(10, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.setOwner(1);
    final Lion l1 = new Lion();
    l1.setOwner(1);
    w.getCell(0).push(l1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    w.getCell(20).push(l2);
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 0, 1)); // first lion
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 20, 21)); // second lion
    // now check a bug whereby the first lion could be moved again
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 1, 2));
  }

  public void testAlternatingMoveBugPart2() {
    final World w = new World(10, 3);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.setOwner(1);
    final Lion l1 = new Lion();
    l1.setOwner(1);
    w.getCell(0).push(l1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    w.getCell(20).push(l2);
    assertTrue(mm.isMovable(p, 0)); // first lion
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 20, 21)); // second lion
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 0, 1)); // first lion
  }

  public void testIsMoved() {
    for (int i = 0; i < 3; ++i) {
      final World w = new World(3, 3);
      final MoveMaster mm = new MoveMaster(w);
      final Wizard p = new Wizard1();
      p.setState(State.ACTIVE);
      p.setOwner(1);
      final Solar s = new Solar();
      s.setOwner(1);
      w.getCell(i).push(s);
      assertEquals(MoveMaster.OK, mm.move(p, i, i + 3));
      assertTrue(s.isMoved());
      assertEquals(MoveMaster.ILLEGAL, mm.move(p, i + 3, i + 4));
    }
  }

  public void testIsAttackableHealing() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.setOwner(2);
    final Hippocrates hippocrates = new Hippocrates();
    hippocrates.setOwner(p.getOwner());
    w.getCell(5).push(p);
    w.getCell(12).push(hippocrates);
    final MoveMaster mm = new MoveMaster(w);
    assertTrue(mm.isAttackable(12, 5));
  }

  public void testShadowWoodCanAttack() {
    final World w = new World(5, 5);
    final ShadowWood sw = new ShadowWood();
    sw.setOwner(1);
    w.getCell(0).push(sw);
    final Lion l = new Lion();
    l.setOwner(2);
    w.getCell(1).push(l);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final MoveMaster mm = new MoveMaster(w);
    assertTrue(mm.isMovable(wiz, 0));
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(wiz, 0, 1));
    sw.setMoved(false);
    sw.set(Attribute.COMBAT, 99);
    assertEquals(MoveMaster.OK, mm.move(wiz, 0, 1));
    assertEquals(sw, w.actor(0));
    assertEquals(l, w.actor(1));
    assertEquals(State.DEAD, l.getState());
  }

  public void testRoperCantAttack() {
    final World w = new World(5, 5);
    final Roper sw = new Roper();
    sw.setOwner(1);
    w.getCell(0).push(sw);
    final Lion l = new Lion();
    l.setOwner(2);
    w.getCell(1).push(l);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final MoveMaster mm = new MoveMaster(w);
    assertFalse(mm.isMovable(wiz, 0));
  }

  public void testPit() {
    final World w = new World(5, 5);
    final Pit sw = new Pit();
    sw.setOwner(1);
    w.getCell(0).push(sw);
    final Lion l = new Lion();
    l.setOwner(2);
    w.getCell(1).push(l);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(2);
    final MoveMaster mm = new MoveMaster(w);
    assertTrue(mm.isMovable(wiz, 1));
    assertEquals(MoveMaster.OK, mm.move(wiz, 1, 0));
    assertNull(w.actor(0));
    assertNull(w.actor(1));
  }

  public void testBug42FoundByCallum() {
    final World w = new World(2, 1);
    final Wizard1 l = new Wizard1();
    l.setOwner(1);
    l.set(Attribute.AGILITY, 0);
    w.getCell(0).push(l);
    final Lion x = new Lion();
    x.setOwner(2);
    w.getCell(1).push(x);
    final MoveMaster mm = new MoveMaster(w);
    // Wait to get engaged
    while (!mm.isEngaged(l, 0)) {
      // do nothing
    }
    assertTrue(mm.isEngaged(l, 0));
    // Simulate death of engagee
    w.getCell(1).pop();
    // In theory can no longer be engagedc
    assertFalse(mm.isEngaged(l, 0));
  }

  public void testDismountAttack() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    mw.setMount(p);
    w.getCell(0).push(mw);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner() + 1);
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.COMBAT_FAILED, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(MoveMaster.ILLEGAL, mm.dismount(p, 0, 1));
  }

  public void testDismountAttackSuccess() {
    final World w = new World(1, 12);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.set(Attribute.COMBAT, 100);
    final MagicWood mw = new MagicWood();
    mw.setOwner(p.getOwner());
    mw.setMount(p);
    w.getCell(0).push(mw);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner() + 1);
    w.getCell(1).push(lion);
    assertEquals(MoveMaster.OK, mm.dismount(p, 0, 1));
    assertTrue(p.isMoved());
    assertEquals(p, w.actor(1));
    w.getCell(1).pop();
    assertEquals(lion, w.actor(1));
    assertEquals(State.DEAD, lion.getState());
  }

  public void testBug121() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    p1.setOwner(1);
    p1.set(Attribute.COMBAT, 15);
    final Centaur centaur = new Centaur();
    centaur.setOwner(p1.getOwner());
    centaur.setMount(p1);
    centaur.setMoved(true); // this is needed, otherwise it is centaur to move
    centaur.set(Attribute.AGILITY, 100);
    centaur.setEngaged(false);
    w.getCell(0).push(centaur);
    final Wizard p2 = new Wizard1();
    p2.setState(State.ACTIVE);
    p2.setOwner(2);
    // Although this horse is marked for reincarnation, it cannot because a
    // wizard is riding it.  The wizard should be exposed as a result.
    final Horse h = new Horse();
    h.set(Attribute.LIFE, 1);
    h.setOwner(p2.getOwner());
    h.set(PowerUps.REINCARNATE, h.reincarnation() != null ? 1 : 0);
    h.setMount(p2);
    w.getCell(1).push(h);
    assertFalse(p1.isMoved());
    assertEquals(MoveMaster.COMBAT_FAILED, mm.dismount(p1, 0, 1));
    assertTrue(p1.isMoved());
    assertEquals(p1, centaur.getMount());
    assertEquals(p2, w.actor(1));
  }

  public void testIsAttackableWithImp() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Imp imp = new Imp();
    imp.setOwner(p.getOwner());
    w.getCell(0).push(imp);
    final Skeleton skeleton = new Skeleton();
    skeleton.setOwner(imp.getOwner() + 1);
    w.getCell(1).push(skeleton);
    assertTrue(mm.isAttackable(0, 1));
  }

  public void testIsShootableWithImp() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Imp imp = new Imp();
    imp.set(Attribute.RANGE, 100);
    imp.set(Attribute.RANGED_COMBAT, 100);
    imp.set(Attribute.SHOTS, 1);
    imp.setOwner(p.getOwner());
    w.getCell(0).push(imp);
    final Skeleton skeleton = new Skeleton();
    skeleton.setOwner(imp.getOwner() + 1);
    w.getCell(1).push(skeleton);
    assertTrue(mm.isShootable(p, 0, 1));
  }

  public void testIsAttackableRanged() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final OgreMage ogreMage = new OgreMage();
    final Hydra hydra = new Hydra();
    ogreMage.setOwner(p.getOwner());
    hydra.setOwner(ogreMage.getOwner() + 1);
    w.getCell(12).push(ogreMage);
    w.getCell(13).push(hydra);
    final MoveMaster mm = new MoveMaster(w);
    assertTrue(mm.isAttackable(12, 13, CombatUtils.RANGED));
    assertFalse(mm.isAttackable(12, 13, CombatUtils.SPECIAL));
  }

  public void testQuickshot() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.set(Attribute.SHOTS, 3);
    p.set(Attribute.RANGE, 100);
    p.set(Attribute.RANGED_COMBAT, 100);
    w.getCell(12).push(p);
    for (int k = 13; k < 17; ++k) {
      final Orc o = new Orc();
      o.setOwner(p.getOwner() + 1);
      w.getCell(k).push(o);
    }
    final MoveMaster mm = new MoveMaster(w);
    assertEquals(MoveMaster.OK, mm.shoot(p, 12, 13));
    assertEquals(1, p.getShotsMade());
    assertEquals(MoveMaster.OK, mm.shoot(p, 12, 14));
    assertEquals(2, p.getShotsMade());
    assertEquals(MoveMaster.OK, mm.shoot(p, 12, 15));
    assertEquals(3, p.getShotsMade());
    assertEquals(MoveMaster.ILLEGAL, mm.shoot(p, 12, 16));
  }

  public void testQuickshotConveyance() {
    final World w = new World(5, 5);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    p.set(Attribute.SHOTS, 3);
    p.set(Attribute.RANGE, 100);
    p.set(Attribute.RANGED_COMBAT, 100);
    final Horse h = new Horse();
    h.setOwner(p.getOwner());
    h.setMount(p);
    w.getCell(12).push(h);
    for (int k = 13; k < 17; ++k) {
      final Orc o = new Orc();
      o.setOwner(p.getOwner() + 1);
      w.getCell(k).push(o);
    }
    final MoveMaster mm = new MoveMaster(w);
    assertEquals(MoveMaster.OK, mm.shootFromConveyance(p, 12, 13));
    assertEquals(1, p.getShotsMade());
    assertEquals(MoveMaster.OK, mm.shootFromConveyance(p, 12, 14));
    assertEquals(2, p.getShotsMade());
    assertEquals(MoveMaster.OK, mm.shootFromConveyance(p, 12, 15));
    assertEquals(3, p.getShotsMade());
    assertEquals(MoveMaster.ILLEGAL, mm.shootFromConveyance(p, 12, 16));
  }

  public void testConfidence() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    w.getWizardManager().setWizard(1, p);
    p.setState(State.ACTIVE);
    p.set(PowerUps.CONFIDENCE, 1);
    final Centaur centaur = new Centaur();
    centaur.set(Attribute.RANGE, 100);
    centaur.set(Attribute.RANGED_COMBAT, 100);
    centaur.setOwner(p.getOwner());
    w.getCell(0).push(centaur);
    final Skeleton skeleton = new Skeleton();
    skeleton.setOwner(centaur.getOwner() + 1);
    w.getCell(1).push(skeleton);
    assertTrue(mm.isAttackable(0, 1));
    assertTrue(mm.isShootable(p, 0, 1));
  }

  public void testAttackAnyRealm() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    w.getWizardManager().setWizard(1, p);
    p.set(PowerUps.ATTACK_ANY_REALM, 1);
    p.setState(State.ACTIVE);
    p.set(Attribute.RANGE, 100);
    p.set(Attribute.RANGED_COMBAT, 100);
    p.set(Attribute.SHOTS, 1);
    w.getCell(0).push(p);
    final Skeleton skeleton = new Skeleton();
    skeleton.setOwner(p.getOwner() + 1);
    w.getCell(1).push(skeleton);
    assertTrue(mm.isAttackable(0, 1));
    assertTrue(mm.isShootable(p, 0, 1));
  }

  public void testKillWithoutVacationAndInvulnerable() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner());
    lion.set(Attribute.COMBAT, 100);
    w.getCell(0).push(lion);
    final HiddenHorror hiddenHorror = new HiddenHorror();
    hiddenHorror.setOwner(lion.getOwner() + 1);
    w.getCell(1).push(hiddenHorror);
    assertEquals(MoveMaster.COMBAT_FAILED, mm.move(p, 0, 1));
    assertTrue(w.actor(1) instanceof RedDragon);
    assertEquals(lion, w.actor(0));
    assertTrue(lion.isMoved());
    lion.setMoved(false);
    final Wizard wizard = new Wizard1();
    wizard.set(PowerUps.INVULNERABLE, 1);
    wizard.setOwner(lion.getOwner() + 1);
    wizard.setState(State.ACTIVE);
    w.getCell(1).push(wizard);
    final boolean e1 = mm.isEngaged(p, 0);
    assertEquals(e1, mm.isEngaged(p, 0));
    assertEquals(MoveMaster.INVULNERABLE, mm.move(p, 0, 1));
    lion.setMoved(false);
    assertEquals(e1, mm.isEngaged(p, 0));
    w.getCell(1).pop(); // bye bye wizard
    w.getCell(1).pop(); // bye bye hidden horror
    assertFalse(mm.isEngaged(p, 0));
    assertFalse(mm.isEngaged(p, 0));
    assertEquals(MoveMaster.PARTIAL, mm.move(p, 0, 1));
    assertFalse(mm.isEngaged(p, 0));
  }

  public void testNoMove() {
    final World w = new World(2, 1);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p = new Wizard1();
    p.setState(State.ACTIVE);
    final Lion lion = new Lion();
    lion.setOwner(p.getOwner());
    lion.set(Attribute.MOVEMENT, 0);
    w.getCell(0).push(lion);
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 0, 1));
    assertEquals(MoveMaster.ILLEGAL, mm.move(p, 0, 0));
  }

  public void testBug304() {
    final World w = Chaos.getChaos().getWorld();
    final MoveMaster mm = new MoveMaster(w);
    final Wizard p1 = new Wizard1();
    p1.setState(State.ACTIVE);
    p1.setOwner(1);
    p1.set(Attribute.LIFE, 1);
    w.getWizardManager().setWizard(1, p1);
    final Wizard p2 = new Wizard1();
    p2.setState(State.ACTIVE);
    p2.setOwner(2);
    p2.set(Attribute.COMBAT, 10);
    w.getWizardManager().setWizard(2, p2);
    final MagicWood mw = new MagicWood();
    mw.setOwner(1);
    w.getCell(0).push(mw);
    mw.setMount(p2);
    w.getCell(1).push(p1);
    assertEquals(MoveMaster.OK, mm.dismount(p2, 0, 1));
    assertEquals(State.DEAD, p1.getState());
    assertNull(w.actor(0));
    assertEquals(p2, w.actor(1));
  }

  public void testExitMounting() {
    final World w = new World(1, 2);
    final MoveMaster mm = new MoveMaster(w);
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    wiz.set(Attribute.LIFE, 1);
    w.getWizardManager().setWizard(1, wiz);
    final Exit exit = new Exit();
    exit.setOpen(false);
    exit.setOwner(1);
    w.getCell(0).push(exit);
    w.getCell(1).push(wiz);
    assertEquals(MoveMaster.ILLEGAL, mm.move(wiz, 1, 0));
    assertNull(exit.getMount());
    exit.setOpen(true);
    assertEquals(MoveMaster.OK, mm.move(wiz, 1, 0));
    assertEquals(wiz, exit.getMount());
  }

}
