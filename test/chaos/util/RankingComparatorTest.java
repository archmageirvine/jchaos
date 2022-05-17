package chaos.util;

import junit.framework.TestCase;
import chaos.common.Castable;
import chaos.common.monster.Horse;
import chaos.common.spell.TouchOfGod;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RankingComparatorTest extends TestCase {

  public void testForwards() {
    final RankingComparator c = RankingComparator.FORWARD_COMPARATOR;
    final Castable t = new TouchOfGod();
    final Castable u = new Horse();
    assertEquals(0, c.compare(t, t));
    assertEquals(0, c.compare(u, u));
    assertTrue(c.compare(t, u) > 0);
    assertTrue(c.compare(u, t) < 0);
  }

  public void testReverse() {
    final RankingComparator c = RankingComparator.REVERSE_COMPARATOR;
    final Castable t = new TouchOfGod();
    final Castable u = new Horse();
    assertEquals(0, c.compare(t, t));
    assertEquals(0, c.compare(u, u));
    assertTrue(c.compare(t, u) < 0);
    assertTrue(c.compare(u, t) > 0);
  }
}
