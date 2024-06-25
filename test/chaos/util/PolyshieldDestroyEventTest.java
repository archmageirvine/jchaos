package chaos.util;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PolyshieldDestroyEventTest extends TestCase {


  public void testNull() {
    try {
      new PolyshieldDestroyEvent(null, Attribute.LIFE, null);
      fail("Doh!");
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testCause() {
    final HashSet<Cell> h = new HashSet<>();
    final Lion lion = new Lion();
    final PolyshieldDestroyEvent e = new PolyshieldDestroyEvent(h, Attribute.LIFE, lion);
    assertEquals(Attribute.LIFE, e.getAttribute());
    assertEquals(h, e.getCells());
    assertEquals(lion, e.getCause());
  }

}
