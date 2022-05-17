package chaos.util;

import java.util.HashSet;

import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.common.monster.Lion;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PolycellAttackEventTest extends TestCase {


  public void testNull() {
    try {
      new PolycellAttackEvent(null, null, 0);
      fail("Doh!");
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testCause() {
    final HashSet<Cell> h = new HashSet<>();
    final Lion lion = new Lion();
    final PolycellAttackEvent e = new PolycellAttackEvent(h, lion, 5);
    assertEquals(5, e.getDamage());
    assertEquals(h, e.getCells());
    assertEquals(lion, e.getCause());
  }

}
