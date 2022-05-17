package chaos.common.inanimate;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.GoblinBomb;
import chaos.common.monster.Lion;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class TempestTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Tempest();
  }

  public void testAgainstSource() {
    AbstractMonsterTest.checkAgainstSource(getActor());
  }

  public void test() {
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_LOS, getCastable().getCastFlags());
    assertEquals(7, getCastable().getCastRange());
  }

  public void testUpdateEmpty() {
    final World w = new World(5, 5);
    final Tempest t = new Tempest();
    t.cast(null, null, null, null);
    t.cast(null, null, w.getCell(0), null);
    t.cast(null, null, w.getCell(0), w.getCell(0));
    t.cast(w, null, null, null);
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    final boolean[] seen = new boolean[w.size()];
    for (int i = 0; i < 500; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      assertFalse(t.isMoved());
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
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

  public void testUpdateLions() {
    final World w = new World(5, 5);
    final Tempest t = new Tempest();
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    for (int i = 1; i < 6; ++i) {
      w.getCell(i).push(new Lion());
    }
    final boolean[] seen = new boolean[w.size()];
    for (int i = 0; i < 500; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      assertFalse(t.isMoved());
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
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
    assertEquals(5, lions);
  }

  public void testUpdateDualTempest() {
    final World w = new World(5, 5);
    final Tempest t = new Tempest();
    t.setState(State.ACTIVE);
    t.setOwner(1);
    Cell c = w.getCell(0);
    c.push(t);
    final Tempest t2 = new Tempest();
    t2.setState(State.ACTIVE);
    t2.setOwner(1);
    Cell c2 = w.getCell(24);
    c2.push(t2);
    for (int i = 0; i < 50; ++i) {
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
    fail("Tempests failed to annihilate each other");
  }

  public void testUpdateReinLions() {
    final World w = new World(5, 5);
    final Tempest t = new Tempest();
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
    for (int i = 0; i < 500; ++i) {
      t.setMoved(true);
      assertFalse(t.update(w, c));
      assertFalse(t.isMoved());
      // find it
      boolean s = false;
      for (int j = 0; j < w.size(); ++j) {
        if (w.actor(j) == t) {
          assertFalse(s);
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

  @Override
  public void testUpdateAsleep() {
    final World w = new World(5, 5);
    final Tempest t = new Tempest();
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

  public void testBug165() {
    final World w = Chaos.getChaos().getWorld();
    final Tempest t = new Tempest();
    t.setOwner(1);
    t.set(Attribute.LIFE, 1);
    t.setState(State.ACTIVE);
    final Cell c = w.getCell(0);
    while (c.peek() != null) {
      c.pop();
    }
    c.push(t);
    for (int k = 1; k < w.size(); ++k) {
      final GoblinBomb b = new GoblinBomb();
      b.setOwner(1);
      w.getCell(k).push(b);
    }
    t.setMoved(true);
    t.update(w, c);
    assertNull(w.actor(0));
  }
}
