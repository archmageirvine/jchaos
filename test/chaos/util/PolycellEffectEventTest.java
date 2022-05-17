package chaos.util;

import java.util.HashSet;

import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.common.monster.Lion;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PolycellEffectEventTest extends TestCase {


  public void testNull() {
    try {
      new PolycellEffectEvent(null, CellEffectType.NON_EVENT);
      fail("Doh!");
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void test() {
    final HashSet<Cell> h = new HashSet<>();
    final PolycellEffectEvent e = new PolycellEffectEvent(h, CellEffectType.NON_EVENT);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(h, e.getCells());
    assertNull(e.getCause());
    assertEquals(CellEffectType.NON_EVENT, new PolycellEffectEvent(new HashSet<>(), null).getEventType());
  }

  public void testCause() {
    final HashSet<Cell> h = new HashSet<>();
    final Lion lion = new Lion();
    final PolycellEffectEvent e = new PolycellEffectEvent(h, CellEffectType.NON_EVENT, lion);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(h, e.getCells());
    assertEquals(lion, e.getCause());
  }

}
