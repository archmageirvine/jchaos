package chaos.board;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.MagicCastle;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Roper;
import chaos.common.monster.CatLord;
import chaos.common.monster.Haunt;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.Spriggan;
import chaos.common.monster.Wraith;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard3;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class UpdaterTest extends TestCase {

  public void testEmpty() {
    final World w = new World(5, 5);
    final Updater u = new Updater(w);
    u.update();
    for (int i = 0; i < w.size(); ++i) {
      assertNull(w.actor(i));
    }
  }

  public void testCatLord() {
    final World w = new World(5, 5);
    final CatLord cl = new CatLord();
    cl.setOwner(2);
    final Lion l = new Lion();
    l.setOwner(1);
    w.getCell(12).push(cl);
    w.getCell(3).push(l);
    final Updater u = new Updater(w);
    u.update();
    assertEquals(l, w.actor(3));
    assertEquals(cl, w.actor(12));
    assertEquals(2, w.actor(3).getOwner());
    assertEquals(2, w.actor(12).getOwner());
    assertTrue(w.actor(3).isMoved());
  }

  public void testPoisoning1() {
    final World w = new World(3, 3);
    final Lion l = new Lion();
    l.setOwner(1);
    l.set(Attribute.LIFE, 1);
    l.set(Attribute.LIFE_RECOVERY, -4);
    w.getCell(3).push(l);
    final Updater u = new Updater(w);
    u.update();
    assertEquals(l, w.actor(3));
    assertEquals(1, w.actor(3).getOwner());
    assertEquals(State.DEAD, w.actor(3).getState());
  }

  public void testPoisoning2() {
    final World w = new World(3, 3);
    final Wraith l = new Wraith();
    l.setOwner(1);
    l.set(Attribute.LIFE, 1);
    l.set(Attribute.LIFE_RECOVERY, -4);
    w.getCell(3).push(l);
    final Updater u = new Updater(w);
    u.update();
    assertEquals(null, w.actor(3));
  }

  public void testPoisoning3() {
    final World w = new World(3, 3);
    final Lion l = new Lion();
    l.setOwner(1);
    l.set(Attribute.INTELLIGENCE, 1);
    l.set(Attribute.INTELLIGENCE_RECOVERY, -4);
    w.getCell(3).push(l);
    final Updater u = new Updater(w);
    u.update();
    assertEquals(l, w.actor(3));
    assertEquals(1, w.actor(3).getOwner());
    assertEquals(State.DEAD, w.actor(3).getState());
  }

  public void testPoisoning4() {
    final World w = new World(3, 3);
    final Lion l = new Lion();
    l.setOwner(1);
    l.set(Attribute.MAGICAL_RESISTANCE, 1);
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, -4);
    w.getCell(3).push(l);
    final Updater u = new Updater(w);
    u.update();
    assertEquals(l, w.actor(3));
    assertEquals(1, w.actor(3).getOwner());
    assertEquals(State.DEAD, w.actor(3).getState());
  }

  public void testRoper() {
    final World w = new World(3, 3);
    final Roper l = new Roper();
    l.setOwner(1);
    w.getCell(0).push(l);
    final Updater u = new Updater(w);
    while (w.actor(0) != null) {
      l.setMoved(true);
      u.update();
    }
    int c = 0;
    for (int i = 1; i < w.size(); ++i) {
      if (w.actor(i) != null) {
        assertEquals(l, w.actor(i));
        ++c;
      }
    }
    assertEquals(1, c);
  }

  public void testRoperDist() {
    final World w = new World(3, 3);
    final Roper l = new Roper();
    l.setOwner(1);
    w.getCell(0).push(l);
    final Updater u = new Updater(w);
    final boolean[] seen = new boolean[w.size()];
    int at = 0;
    for (int i = 0; i < 100; ++i) {
      while (w.actor(at) != null) {
        l.setMoved(true);
        u.update();
      }
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == l) {
          at = j;
          seen[j] = true;
        }
      }
    }
    for (final boolean s : seen) {
      assertTrue(s);
    }
  }

  public void testRoperDist2() {
    final World w = new World(3, 3);
    final Roper l = new Roper();
    l.setOwner(1);
    w.getCell(0).push(l);
    final Lion ll = new Lion();
    w.getCell(1).push(ll);
    final Lion lll = new Lion();
    lll.setState(State.DEAD);
    w.getCell(7).push(lll);
    final Updater u = new Updater(w);
    final boolean[] seen = new boolean[w.size()];
    int at = 0;
    for (int i = 0; i < 100; ++i) {
      while (w.actor(at) != null) {
        l.setMoved(true);
        u.update();
      }
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == l) {
          at = j;
          seen[j] = true;
        }
      }
    }
    assertTrue(seen[0]);
    assertFalse(seen[1]);
    assertTrue(seen[2]);
    assertTrue(seen[3]);
    assertTrue(seen[4]);
    assertTrue(seen[5]);
    assertTrue(seen[6]);
    assertFalse(seen[7]);
    assertTrue(seen[8]);
    assertEquals(ll, w.actor(1));
    assertEquals(lll, w.actor(7));
  }

  public void testEmptyCollapse() {
    final World w = new World(3, 3);
    final MagicCastle l = new MagicCastle();
    l.setOwner(1);
    w.getCell(5).push(l);
    final Updater u = new Updater(w);
    for (int i = 0; i < 100; ++i) {
      u.update();
    }
    assertNull(w.actor(5));
  }

  public void testEmptyCollapse2() {
    final World w = new World(3, 3);
    final MagicWood l = new MagicWood();
    l.setOwner(1);
    w.getCell(5).push(l);
    final Updater u = new Updater(w);
    for (int i = 0; i < 100; ++i) {
      u.update();
    }
    assertEquals(l, w.actor(5));
  }

  public void testCollapse() {
    final World w = new World(3, 3);
    final Updater u = new Updater(w);
    for (int k = 0; k < 50; ++k) {
      final MagicCastle l = new MagicCastle();
      l.setOwner(1);
      w.getCell(5).push(l);
      final Wizard z = new Wizard3();
      w.getWizardManager().setWizard(3, z);
      z.setState(State.ACTIVE);
      z.setShotsMade(1);
      z.setMoved(true);
      l.setMount(z);
      for (int i = 0; i < 1000 && w.actor(5).equals(l); ++i) {
        u.update();
      }
      assertEquals(z, w.actor(5));
      assertEquals(1, z.getBonusSelect());
      assertTrue(z.getBonusCount() >= 4);
      assertTrue(z.getBonusCount() <= 9);
      assertEquals(0, z.getShotsMade());
      assertFalse(z.isMoved());
      assertEquals(1, z.get(PowerUps.LEVEL));
      w.getCell(5).pop();
    }
  }

  public void testCollapse2() {
    final World w = new World(3, 3);
    final MagicWood l = new MagicWood();
    l.setOwner(1);
    w.getCell(5).push(l);
    final Wizard z = new Wizard1();
    z.setState(State.ACTIVE);
    z.setOwner(3);
    l.setMount(z);
    final Updater u = new Updater(w);
    for (int i = 0; i < 100; ++i) {
      u.update();
    }
    assertEquals(z, w.actor(5));
    assertTrue(z.getBonus() > 0);
  }

  public void testMeditationIsMoveCleared() {
    final World w = new World(3, 3);
    final MagicWood l = new MagicWood();
    l.setOwner(1);
    w.getCell(5).push(l);
    final Wizard z = new Wizard1();
    z.setState(State.ACTIVE);
    z.setOwner(3);
    z.setMoved(true);
    l.setMount(z);
    new Updater(w).update();
    assertFalse(z.isMoved());
  }

  public void testMountIsMoveCleared() {
    final World w = new World(3, 3);
    final Horse l = new Horse();
    l.setOwner(3);
    w.getCell(5).push(l);
    final Wizard z = new Wizard1();
    w.getWizardManager().setWizard(3, z);
    z.setState(State.ACTIVE);
    z.setMoved(true);
    l.setMount(z);
    new Updater(w).update();
    assertFalse(z.isMoved());
    assertEquals(0, z.getShotsMade());
  }

  public void testRideIsMoveCleared() {
    final World w = new World(3, 3);
    final RedDragon l = new RedDragon();
    l.setOwner(3);
    w.getCell(5).push(l);
    final Wizard z = new Wizard1();
    z.setState(State.ACTIVE);
    z.setOwner(3);
    z.setMoved(true);
    l.setMount(z);
    new Updater(w).update();
    assertFalse(z.isMoved());
  }

  public void testUnc() {
    final Wizard w = new Wizard1();
    w.set(PowerUps.UNCERTAINTY, 5);
    w.setState(State.ACTIVE);
    final World world = new World(3, 3);
    world.getWizardManager().setWizard(2, w);
    world.getCell(0).push(w);
    final Updater u = new Updater(world);
    for (int i = 0; i < 20; ++i) {
      u.update();
      if (i == 0) {
        assertTrue(w.get(PowerUps.UNCERTAINTY) >= 3);
      }
    }
    assertEquals(0, w.get(PowerUps.UNCERTAINTY));
    assertEquals(w, world.actor(0));
    int h = 0;
    for (int i = 1; i < world.size(); ++i) {
      final Actor a = world.actor(i);
      if (a != null) {
        ++h;
        assertTrue(a instanceof Monster);
        if (!(a instanceof Spriggan)) {
          assertEquals(a.toString(), State.ACTIVE, a.getState());
          if (!(a instanceof Haunt)) {
            assertEquals(a.toString(), 2, a.getOwner());
          }
        }
      }
    }
    assertEquals(5, h);
  }

  public void testTopScorerBonus() {
    final World w = new World(1, 2);
    final Wizard w1 = w.getWizardManager().getWizard(1);
    w1.setState(State.ACTIVE);
    w1.addScore(1000);
    w1.set(Attribute.INTELLIGENCE, 0);
    w1.set(Attribute.AGILITY, 0);
    w1.set(Attribute.LIFE, 0);
    w1.set(Attribute.MAGICAL_RESISTANCE, 0);
    w1.set(Attribute.INTELLIGENCE_RECOVERY, 0);
    w1.set(Attribute.AGILITY_RECOVERY, 0);
    w1.set(Attribute.LIFE_RECOVERY, 0);
    w1.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 0);
    final Wizard w2 = w.getWizardManager().getWizard(2);
    w2.setState(State.ACTIVE);
    w2.addScore(100000);
    w2.set(Attribute.INTELLIGENCE, 0);
    w2.set(Attribute.AGILITY, 0);
    w2.set(Attribute.LIFE, 0);
    w2.set(Attribute.MAGICAL_RESISTANCE, 0);
    w2.set(Attribute.INTELLIGENCE_RECOVERY, 0);
    w2.set(Attribute.AGILITY_RECOVERY, 0);
    w2.set(Attribute.LIFE_RECOVERY, 0);
    w2.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 0);
    w.getCell(0).push(w1);
    w.getCell(1).push(w2);
    new Updater(w).applyTopScorerBonus();
    int inc = 0;
    for (final Attribute a : Updater.TOP_SCORER_BONUS_ATTRIBUTES) {
      assertEquals(0, w1.get(a));
      inc += w2.get(a);
    }
    assertEquals(1, inc);
  }

  public void testExitOpening() {
    final World w = new World(3, 3);
    final Exit e = new Exit(null, Exit.ExitType.NO_REAL_ENEMY);
    e.setOwner(1);
    w.getCell(0).push(e);
    final RedDragon l = new RedDragon();
    l.setOwner(3);
    w.getCell(5).push(l);
    new Updater(w).update();
    assertFalse(e.isOpen());
    w.getCell(5).pop();
    new Updater(w).update();
    assertTrue(e.isOpen());
  }

}
