package chaos.common;

import chaos.Chaos;
import chaos.board.World;
import chaos.common.growth.Balefire;
import chaos.common.growth.Earthquake;
import chaos.common.growth.Fire;
import chaos.common.growth.Flood;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.Generator;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.Pit;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.Orc;
import chaos.common.monster.Skeleton;
import chaos.common.monster.StoneGolem;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests general growth functionality.
 * @author Sean A. Irvine
 */
public class MaterialGrowthTest extends TestCase {

  public void testGrowByCombat() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Fire);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Fire);
  }

  public void testGrowByCombatDead() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    f.setState(State.DEAD);
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
  }

  public void testGrowByCombatOrc() {
    final World world = new World(2, 1);
    final Fire f = new Fire();
    world.getCell(0).push(f);
    final Orc o = new Orc();
    world.getCell(1).push(o);
    f.grow(0, world);
    assertTrue(world.actor(0) instanceof Fire);
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Fire);
    assertEquals(State.DEAD, o.getState());
    f.grow(0, world);
    assertEquals(f, world.actor(0));
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Fire);
    world.getCell(1).pop();
    assertNull(world.actor(1));
  }

  public void testGrowByCombatSkeleton() {
    final World world = new World(2, 1);
    final Fire f = new Fire();
    world.getCell(0).push(f);
    final Skeleton skeleton = new Skeleton();
    world.getCell(1).push(skeleton);
    f.grow(0, world);
    assertTrue(world.actor(0) instanceof Balefire);
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Fire);
    assertFalse(world.actor(1) instanceof Balefire);
    final Balefire bf = (Balefire) world.actor(0);
    bf.grow(0, world);
    assertEquals(bf, world.actor(0));
    assertTrue(bf != world.actor(1));
    assertTrue(world.actor(1) instanceof Fire);
    assertFalse(world.actor(1) instanceof Balefire);
    world.getCell(1).pop();
    assertNull(world.actor(1));
  }

  public void testGrowByCombatOverFlood() {
    final World world = new World(2, 1);
    final Fire f = new Fire();
    f.setOwner(1);
    world.getCell(0).push(f);
    final Flood o = new Flood();
    o.setOwner(2);
    world.getCell(1).push(o);
    f.grow(0, world);
    assertTrue(world.actor(0) instanceof Fire);
    assertTrue(f != world.actor(1));
    assertTrue("Life:" + world.actor(1).get(Attribute.LIFE), world.actor(1) instanceof Fire);
    f.grow(0, world);
    assertEquals(f, world.actor(0));
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Fire);
    assertFalse(world.actor(1) instanceof Balefire);
    world.getCell(1).pop();
    assertNull(world.actor(1));
  }

  public void testGrowByCombatOverFire() {
    final World world = new World(2, 1);
    final Flood f = new Flood();
    f.setOwner(1);
    world.getCell(0).push(f);
    final Fire o = new Fire();
    o.setOwner(2);
    world.getCell(1).push(o);
    f.grow(0, world);
    assertEquals(f, world.actor(0));
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Flood);
    f.grow(0, world);
    assertEquals(f, world.actor(0));
    assertTrue(f != world.actor(1));
    assertTrue(world.actor(1) instanceof Flood);
    world.getCell(1).pop();
    assertNull(world.actor(1));
  }

  public void testGrowByCombatOverEq() {
    final World w = new World(2, 1);
    final Flood f = new Flood();
    f.setOwner(1);
    w.getCell(0).push(f);
    final Earthquake o = new Earthquake();
    o.setOwner(2);
    w.getCell(1).push(o);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Earthquake);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Earthquake);
    w.getCell(1).pop();
    assertNull(w.actor(1));
  }

  public void testGrowByCombatLion() {
    final World w = new World(2, 1);
    final Flood f = new Flood();
    w.getCell(0).push(f);
    final Lion o = new Lion();
    w.getCell(1).push(o);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(o, w.actor(1));
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Flood);
    assertEquals(State.DEAD, o.getState());
  }

  public void testGrowByCombatPit() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    final Pit o = new Pit();
    w.getCell(1).push(o);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(o, w.actor(1));
  }

  private void checkNucked(final MaterialGrowth g) {
    final World w = new World(2, 1);
    w.getCell(0).push(g);
    final Nuked o = new Nuked();
    w.getCell(1).push(o);
    for (int i = 0; i < 20; ++i) {
      g.grow(0, w);
    }
    assertEquals(g, w.actor(0));
    assertEquals(o, w.actor(1));
  }

  public void testGrowByCombatNuked() {
    checkNucked(new Flood());
    checkNucked(new GooeyBlob());
    checkNucked(new Earthquake());
  }

  public void testGrowByCombat8() {
    final World w = new World(3, 3);
    final Fire f = new Fire();
    w.getCell(4).push(f);
    for (int i = 0; i < 100; ++i) {
      f.grow(4, w);
    }
    for (int i = 0; i < 8; ++i) {
      assertTrue(w.actor(i) instanceof Fire);
    }
  }

  public void testGrowByCombat8Eq() {
    final World w = new World(3, 3);
    final Earthquake f = new Earthquake();
    w.getCell(4).push(f);
    for (int i = 0; i < 100; ++i) {
      f.grow(4, w);
    }
    assertTrue(w.actor(1) instanceof Earthquake);
    assertTrue(w.actor(3) instanceof Earthquake);
    assertTrue(w.actor(4) instanceof Earthquake);
    assertTrue(w.actor(5) instanceof Earthquake);
    assertTrue(w.actor(7) instanceof Earthquake);
    assertNull(w.actor(0));
    assertNull(w.actor(2));
    assertNull(w.actor(6));
    assertNull(w.actor(8));
  }

  public void testGrowByCombat8b() {
    final World w = new World(3, 3);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    for (int i = 0; i < 50; ++i) {
      f.grow(0, w);
    }
    assertTrue(w.actor(0) instanceof Fire);
    assertTrue(w.actor(1) instanceof Fire);
    assertTrue(!(w.actor(2) instanceof Fire));
    assertTrue(w.actor(3) instanceof Fire);
    assertTrue(w.actor(4) instanceof Fire);
    assertTrue(!(w.actor(5) instanceof Fire));
    assertTrue(!(w.actor(6) instanceof Fire));
    assertTrue(!(w.actor(7) instanceof Fire));
    assertTrue(!(w.actor(8) instanceof Fire));
  }

  public void testGrowByCombatDoesNotGrowOnOwnGrowths() {
    final World w = new World(2, 1);
    final Fire f1 = new Fire();
    w.getCell(0).push(f1);
    final Fire f2 = new Fire();
    w.getCell(1).push(f2);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    assertEquals(f1, w.actor(0));
    assertEquals(f2, w.actor(1));
  }

  public void testGrowByCombatFloodFireInteraction() {
    final World w = new World(2, 1);
    final Fire f1 = new Fire();
    w.getCell(0).push(f1);
    final Flood f2 = new Flood();
    w.getCell(1).push(f2);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    assertEquals(f1, w.actor(0));
    assertEquals(f2, w.actor(1));
  }

  public void testGrowByCombatKillsWizard() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      ((Growth) w.actor(0)).grow(0, w);
    }
    assertTrue(w.actor(0) instanceof Fire);
    assertTrue(w.actor(1) instanceof Fire);
    assertEquals(State.DEAD, z.getState());
  }

  public void testGrowByCombatDoesNotKillWizardWithFireShield() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    z.set(PowerUps.FIRE_SHIELD, 1);
    w.getCell(1).push(z);
    assertFalse(f.canGrowOver(w.getCell(1)));
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
  }

  public void testGrowByCombatDoesNotKillWizardWithFloodShield() {
    final World w = new World(2, 1);
    final Flood f = new Flood();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    z.set(PowerUps.FLOOD_SHIELD, 1);
    w.getCell(1).push(z);
    assertFalse(f.canGrowOver(w.getCell(1)));
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
  }

  public void testGrowByCombatDoesNotKillWizardWithFireShieldButDoesKillMount() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    z.set(PowerUps.FIRE_SHIELD, 1);
    final Horse h = new Horse();
    h.setMount(z);
    w.getCell(1).push(h);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0)); // horse should have died leaving wizard protected with shield
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
  }

  public void testGrowOver() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
  }

  public void testGrowOverPoisoned() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.set(PowerUps.NO_GROW, 1);
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertNull(w.actor(1));
    f.set(PowerUps.NO_GROW, 0);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(w.actor(1) instanceof GooeyBlob);
  }

  public void testGrowOverDead() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setState(State.DEAD);
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
  }

  public void testGrowOverOrc() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Orc o = new Orc();
    w.getCell(1).push(o);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    w.getCell(1).reinstate();    // extricate orc, check life constraint
    assertEquals(o, w.actor(1));
    assertEquals(State.ACTIVE, o.getState());
    assertTrue(o.get(Attribute.LIFE) < new Orc().get(Attribute.LIFE));
    assertTrue(o.get(Attribute.LIFE) > 0);
  }

  public void testGrowOverLion() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Lion lion = new Lion();
    w.getCell(1).push(lion);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    w.getCell(1).reinstate();    // extricate lion, check life constraint
    assertEquals(lion, w.actor(1));
    assertEquals(State.ACTIVE, lion.getState());
    assertTrue(lion.get(Attribute.LIFE) < new Lion().get(Attribute.LIFE));
    assertTrue(lion.get(Attribute.LIFE) > 0);
  }

  public void testGrowOver8() {
    final World w = new World(3, 3);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(4).push(f);
    for (int i = 0; i < 100; ++i) {
      f.grow(4, w);
    }
    for (int i = 0; i < 8; ++i) {
      assertTrue(w.actor(i) instanceof GooeyBlob);
    }
  }

  public void testGrowOver8b() {
    final World w = new World(3, 3);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    for (int i = 0; i < 50; ++i) {
      f.grow(0, w);
    }
    assertTrue(w.actor(0) instanceof GooeyBlob);
    assertTrue(w.actor(1) instanceof GooeyBlob);
    assertTrue(!(w.actor(2) instanceof GooeyBlob));
    assertTrue(w.actor(3) instanceof GooeyBlob);
    assertTrue(w.actor(4) instanceof GooeyBlob);
    assertTrue(!(w.actor(5) instanceof GooeyBlob));
    assertTrue(!(w.actor(6) instanceof GooeyBlob));
    assertTrue(!(w.actor(7) instanceof GooeyBlob));
    assertTrue(!(w.actor(8) instanceof GooeyBlob));
  }

  public void testGrowOverDoesNotGrowOnOwnGrowths() {
    final World w = new World(2, 1);
    final GooeyBlob f1 = new GooeyBlob();
    w.getCell(0).push(f1);
    final GooeyBlob f2 = new GooeyBlob();
    w.getCell(1).push(f2);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    f1.grow(0, w);
    f2.grow(1, w);
    assertEquals(f1, w.actor(0));
    assertEquals(f2, w.actor(1));
  }

  public void testGrowOverKillsWizard() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Wizard z = new Wizard1();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertTrue(w.actor(1) instanceof GooeyBlob);
    assertEquals(State.DEAD, z.getState());
  }

  public void testGrowOverKillsMountedWizard() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    final Horse h = new Horse();
    h.setMount(z);
    w.getCell(1).push(h);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
    f.grow(0, w);
    assertTrue(w.actor(1) instanceof GooeyBlob);
    assertEquals(State.DEAD, z.getState());
  }

  public void testGrowOverOwnCreatures() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    final Orc z = new Orc();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
  }

  public void testGrowOverOwnTree() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    final MagicWood z = new MagicWood();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
  }

  public void testGrowOverOthersTree() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Fire z = new Fire();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
  }

  public void testGrowOverOwnFire() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    final Fire z = new Fire();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
  }

  public void testGrowOverOthersFire() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Fire z = new Fire();
    z.setState(State.ACTIVE);
    w.getCell(1).push(z);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(z, w.actor(1));
  }

  public void testFireFloodFight() {
    // in a 3x3 world carry out 100 iterations for 100 games tests ownership at end
    int totalFire = 0;
    int totalFlood = 0;
    for (int game = 0; game < 100; ++game) {
      final World w = new World(3, 3);
      final Fire f = new Fire();
      f.setOwner(1);
      w.getCell(0).push(f);
      final Flood g = new Flood();
      g.setOwner(2);
      w.getCell(8).push(g);
      for (int j = 0; j < 50; ++j) {
        for (int i = 0; i < 9; ++i) {
          final Actor a = w.actor(i);
          if (a != null) {
            ((Growth) a).grow(i, w);
          }
        }
      }
      for (int i = 0; i < 9; ++i) {
        final Actor a = w.actor(i);
        if (a instanceof Fire) {
          ++totalFire;
          assertEquals(1, a.getOwner());
        } else if (a instanceof Flood) {
          ++totalFlood;
          assertEquals(2, a.getOwner());
        } else {
          assertNull(a);
        }
      }
    }
    assertTrue(totalFlood > 0);
    assertTrue(totalFire > totalFlood); // expect slight bias to fire because it moves first
  }

  public void testGrowByCombatDoesNotKillWizardWithLichLord() {
    final World w = new World(2, 1);
    final Fire f = new Fire();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    z.set(PowerUps.LICH_LORD, 1);
    w.getCell(1).push(z);
    f.grow(0, w);
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
    assertEquals(Realm.MATERIAL, z.getRealm());
    f.grow(0, w);    // should kill wizard, lich lord happens
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
    assertEquals(Realm.ETHERIC, z.getRealm());
    assertEquals(0, z.get(PowerUps.LICH_LORD));
    f.grow(0, w);    // no change
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
    assertEquals(Realm.ETHERIC, z.getRealm());
    f.grow(0, w);    // now wizard will die
    assertTrue(w.actor(1) instanceof Fire);
    assertEquals(State.DEAD, z.getState());
  }

  private Wizard createWizard(final int owner) {
    final Wizard wizard = new Wizard1();
    wizard.setOwner(owner);
    wizard.setState(State.ACTIVE);
    return wizard;
  }

  public void testGrowOverDoesNotKillWizardWithLichLord() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    w.getCell(0).push(f);
    final Wizard z = createWizard(1);
    z.set(PowerUps.LICH_LORD, 1);
    w.getCell(1).push(z);
    f.grow(0, w); // should kill wizard, lich lord happens
    assertEquals(z, w.actor(1));
    assertEquals(State.ACTIVE, z.getState());
    assertEquals(Realm.ETHERIC, z.getRealm());
    assertEquals(0, z.get(PowerUps.LICH_LORD));
    f.grow(0, w);    // now wizard will die
    assertTrue(w.actor(1) instanceof GooeyBlob);
    assertEquals(State.DEAD, z.getState());
  }

  public void testGrowOverDoesNotKillWizardWithDR() {
    final World w = new World(2, 1);
    final GooeyBlob f = new GooeyBlob();
    f.setOwner(1);
    w.getCell(0).push(f);
    final Wizard z = createWizard(3);
    z.set(PowerUps.DEAD_REVENGE, 1);
    w.getCell(1).push(z);
    f.grow(0, w); // should kill wizard, DR happens
    assertEquals(State.ACTIVE, z.getState());
    assertTrue(w.actor(1) instanceof Generator);
    assertEquals(z.getOwner(), w.actor(1).getOwner());
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertTrue(w.actor(1) instanceof Generator);    // shouldn't grow over generator
  }

  public void testGrowByCombatKillsWizard2() {
    final World w = Chaos.getChaos().getWorld();
    for (int k = 0; k < w.size(); ++k) {    // Make sure world is clean before test
      while (w.actor(k) != null) {
        w.getCell(k).pop();
      }
    }
    final Fire f = new Fire();
    w.getCell(0).push(f);
    f.setOwner(1);
    final Wizard z = createWizard(1);
    w.getCell(1).push(z);
    final Lion l = new Lion();
    l.setOwner(1);
    w.getCell(2).push(l);
    while (z.getState() != State.DEAD) { // grow until wizard dies
      f.grow(0, w);
      l.set(Attribute.LIFE, (byte) 100); // make sure lion does not burn
    }
    assertTrue(w.actor(0) instanceof Fire);
    assertEquals(State.ASLEEP, f.getState());
    assertEquals(State.ASLEEP, l.getState());
    assertEquals(0, l.getOwner());
    assertTrue(w.actor(1) instanceof Fire);
    final Fire v = (Fire) w.actor(1);
    assertEquals(0, v.getOwner());
    assertEquals(State.ASLEEP, v.getState());
  }

  public void testXX() {
    final Earthquake eq = new Earthquake();
    assertEquals(0, eq.getLosMask());
    assertEquals(Castable.CAST_LOS | Castable.CAST_EMPTY, eq.getCastFlags());
    eq.cast(null, null, null, null);
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    eq.cast(w, wiz, w.getCell(0), null);
    assertEquals(eq, w.actor(0));
  }

  public void testFireBonus() {
    final World w = Chaos.getChaos().getWorld();
    final Wizard q = createWizard(1);
    w.getWizardManager().setWizard(1, q);
    final Fire f = new Fire();
    f.setOwner(1);
    w.getCell(0).push(f);
    final StoneGolem z = new StoneGolem();
    z.setOwner(3);
    z.set(Attribute.LIFE, 1);
    w.getCell(1).push(z);    // Force it to grow over this object, but putting object in 3 places
    w.getCell(w.width()).push(z);
    w.getCell(w.width() + 1).push(z);
    f.grow(0, w); // this should kill wizard
    assertEquals(0, z.get(Attribute.LIFE));
    assertEquals(State.DEAD, z.getState());
    assertEquals(1, q.getBonusSelect());
  }
}
