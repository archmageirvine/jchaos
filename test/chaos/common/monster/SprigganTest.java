package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class SprigganTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Spriggan();
  }

  public void testSleepiness() {
    final Spriggan s = (Spriggan) mCastable;
    s.setState(State.ACTIVE);
    for (int i = 0; i < 220; ++i) {
      s.update(null, null);
      if (s.getState() == State.ASLEEP) {
        return;
      }
    }
    fail("Spriggan didn't sleep");
  }

  public void testMeanSleepiness() {
    final int[] c = new int[225];
    for (int i = 0; i < 5000; ++i) {
      final Spriggan s = (Spriggan) mCastable;
      s.setState(State.ACTIVE);
      int j = 0;
      do {
        ++j;
        s.update(null, null);
      } while (s.getState() != State.ASLEEP);
      c[j]++;
    }
    int e = 0;
    for (int i = 0; i < c.length; ++i) {
      e += i * c[i];
    }
    e /= 5000;
    assertTrue(e > 100);
    assertTrue(e < 120);
    //    System.err.println("Mean sleep: " + e);
  }

  /** As above but on the same spriggan. */
  public void testMeanSleepiness2() {
    final int[] c = new int[225];
    final Spriggan s = (Spriggan) mCastable;
    for (int i = 0; i < 5000; ++i) {
      s.setState(State.ACTIVE);
      int j = 0;
      do {
        ++j;
        s.update(null, null);
      } while (s.getState() != State.ASLEEP);
      c[j]++;
    }
    int e = 0;
    for (int i = 0; i < c.length; ++i) {
      e += i * c[i];
    }
    e /= 5000;
    assertTrue(e > 100);
    assertTrue(e < 120);
  }

  @Override
  public void testUpdate() {
    final Actor a = (Actor) mCastable;
    a.setMoved(true);
    assertFalse(a.update(null, null));
    assertFalse(a.isMoved());
    a.setState(State.DEAD);
    assertFalse(a.update(null, null));
  }
}
