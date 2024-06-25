package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class VortexTest extends AbstractMonsterTest {

  private static final int MAX_ITERATIONS = 50000;

  @Override
  public Castable getCastable() {
    return new Vortex();
  }

  public void testUpdateEmpty() {
    final World w = new World(5, 5);
    final Vortex t = new Vortex();
    t.cast(null, null, null, null);
    t.cast(null, null, w.getCell(0), null);
    t.cast(null, null, w.getCell(0), w.getCell(0));
    t.cast(w, null, null, null);
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    final boolean[] seen = new boolean[w.size()];
    int seenCount = 0;
    for (int i = 0; i < MAX_ITERATIONS && seenCount < seen.length; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
          if (!seen[j]) {
            ++seenCount;
          }
          seen[j] = true;
          c = w.getCell(j);
          s = true;
        }
      }
      assertTrue(s);
    }
    for (int i = 0; i < seen.length; ++i) {
      assertTrue(String.valueOf(i), seen[i]);
    }
  }

  @Override
  public void testUpdateAsleep() {
    final World w = new World(5, 5);
    final Vortex t = new Vortex();
    t.cast(null, null, null, null);
    t.cast(null, null, w.getCell(0), null);
    t.cast(null, null, w.getCell(0), w.getCell(0));
    t.cast(w, null, null, null);
    t.setState(State.ASLEEP);
    t.setOwner(1);
    final Cell c = w.getCell(0);
    c.push(t);
    for (int i = 0; i < 500; ++i) {
      t.setMoved(true);
      t.update(w, c);
      assertEquals(t, w.actor(0));
    }
  }

  public void testUpdateLions() {
    final World w = new World(5, 5);
    final Vortex t = new Vortex();
    t.cast(null, null, null, null);
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_LOS, t.getCastFlags());
    assertEquals(7, t.getCastRange());
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    for (int i = 1; i < w.size(); ++i) {
      w.getCell(i).push(new Lion());
    }
    final boolean[] seen = new boolean[w.size()];
    int countSeen = 0;
    for (int i = 0; i < MAX_ITERATIONS && countSeen < seen.length; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
          if (!seen[j]) {
            ++countSeen;
          }
          seen[j] = true;
          c = w.getCell(j);
          s = true;
        }
      }
      assertTrue(s);
    }
    int lions = 0;
    for (int i = 0; i < seen.length; ++i) {
      assertTrue(String.valueOf(i), seen[i]);
      if (w.actor(i) instanceof Lion) {
        ++lions;
      }
    }
    assertTrue(lions < 20);
  }

  public void testUpdateReinLions() {
    final World w = new World(5, 5);
    final Vortex t = new Vortex();
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    for (int i = 1; i < 25; ++i) {
      final Lion ll = new Lion();
      ll.set(PowerUps.REINCARNATE, ll.reincarnation() != null ? 1 : 0);
      w.getCell(i).push(ll);
    }
    final boolean[] seen = new boolean[w.size()];
    int countSeen = 0;
    for (int i = 0; i < MAX_ITERATIONS && countSeen != seen.length; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
          if (!seen[j]) {
            ++countSeen;
          }
          seen[j] = true;
          c = w.getCell(j);
          s = true;
        }
      }
      assertTrue(s);
    }
    int lions = 0;
    for (int i = 0; i < seen.length; ++i) {
      assertTrue(String.valueOf(i), seen[i]);
      if (w.actor(i) instanceof Lion) {
        ++lions;
      }
    }
    assertTrue(lions < 20);
  }

  public void testUpdateDualVortex() {
    final World w = new World(5, 5);
    final Vortex t = new Vortex();
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    final Vortex t2 = new Vortex();
    t2.setState(State.ACTIVE);
    t2.setOwner(1);
    Cell c2 = w.getCell(24);
    c2.push(t2);
    for (int i = 0; i < 500; ++i) {
      t.setMoved(true);
      t.update(w, c);
      assertFalse(t.isMoved());
      if (c2.peek() == t2) {
        t2.setMoved(true);
        t2.update(w, c2);
        assertFalse(t2.isMoved());
      }
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          c = w.getCell(j);
          s = true;
        } else if (w.actor(j) == t2) {
          c2 = w.getCell(j);
          s = true;
        }
      }
      if (!s) {
        return; // they annihilated
      }
    }
    fail("Vortices failed to annihilate each other");
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Volcano().reincarnation());
  }

}
