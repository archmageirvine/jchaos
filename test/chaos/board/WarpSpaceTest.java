package chaos.board;

import chaos.common.State;
import chaos.common.monster.Centaur;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class WarpSpaceTest extends TestCase {

  public void testEmpty() {
    final World w = new World(5, 5);
    final WarpSpace u = w.getWarpSpace();
    u.warpIn(null);
    u.warpIn(w);
    u.warpOut(null, null);
    final Lion l = new Lion();
    w.getCell(3).push(l);
    u.warpOut(w.getCell(3), l);
    assertTrue(u.contains(l));
    assertNull(w.actor(3));
    for (int i = 0; i < 5; ++i) {
      u.warpIn(w);
    }
    int lc = 0, nc = 0, cell = 0;
    for (int j = 0; j < w.size(); ++j) {
      if (w.actor(j) != null) {
        ++nc;
      }
      if (w.actor(j) == l) {
        ++lc;
        cell = j;
      }
    }
    assertEquals(1, lc);
    assertEquals(1, nc);
    assertFalse(u.contains(l));
    u.warpOut(w.getCell(cell), null);
    assertTrue(u.contains(l));
    for (int j = 0; j < w.size(); ++j) {
      final Centaur c = new Centaur();
      if (j == 7) {
        c.setState(State.DEAD);
      }
      w.getCell(j).push(c);
    }
    for (int i = 0; i < 5; ++i) {
      u.warpIn(w);
    }
    lc = 0;
    nc = 0;
    cell = 0;
    for (int j = 0; j < w.size(); ++j) {
      if (w.actor(j) != null) {
        ++nc;
      }
      if (w.actor(j) == l) {
        ++lc;
        cell = j;
      }
    }
    assertEquals(7, cell);
    assertEquals(1, lc);
    assertEquals(w.size(), nc);
    assertFalse(u.contains(l));
    u.warpOut(w.getCell(cell), null);
    assertTrue(u.contains(l));
    for (int j = 0; j < w.size(); ++j) {
      w.getCell(j).pop();
      w.getCell(j).pop();
      final Centaur c = new Centaur();
      w.getCell(j).push(c);
    }
    for (int i = 0; i < 4; ++i) {
      u.warpIn(w);
      assertTrue(u.contains(l));
    }
    for (int j = 0; j < w.size(); ++j) {
      assertTrue(w.actor(j) != l);
    }
    u.warpIn(w);
    lc = 0;
    nc = 0;
    for (int j = 0; j < w.size(); ++j) {
      if (w.actor(j) != null) {
        ++nc;
      }
      if (w.actor(j) == l) {
        ++lc;
      }
    }
    assertEquals(1, lc);
    assertEquals(w.size(), nc);
    assertFalse(u.contains(l));
    for (int j = 0; j < w.size(); ++j) {
      w.getCell(j).pop();
      w.getCell(j).pop();
    }
    for (int i = 0; i < 4; ++i) {
      u.warpIn(w);
    }
    for (int j = 0; j < w.size(); ++j) {
      assertTrue(!(w.actor(j) instanceof Centaur));
    }
    u.warpIn(w);
    lc = 0;
    for (int j = 0; j < w.size(); ++j) {
      if (w.actor(j) instanceof Centaur) {
        ++lc;
      }
    }
    assertEquals(1, lc);
  }

  public void testSpellSelectionBug() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    w.getCell(4).push(wiz);
    w.getWarpSpace().warpOut(w.getCell(4), wiz);
    assertTrue(wiz.getState() == State.ACTIVE);
    assertTrue(w.getWarpSpace().contains(wiz));
  }

  public void testSpellSelectionMountBug() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    final Horse h = new Horse();
    h.setOwner(2);
    h.setMount(wiz);
    w.getCell(4).push(h);
    w.getWarpSpace().warpOut(w.getCell(4), wiz);
    assertTrue(wiz.getState() == State.ACTIVE);
    assertTrue(w.getWarpSpace().contains(wiz));
  }

  public void testPrune() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    final Horse h = new Horse();
    h.setOwner(2);
    w.getCell(4).push(h);
    w.getWarpSpace().warpOut(w.getCell(4), wiz);
    assertTrue(w.getWarpSpace().contains(h));
    w.getWarpSpace().prune(2);
    assertFalse(w.getWarpSpace().contains(h));
  }
}
