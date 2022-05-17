package chaos.util;

import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.monster.Lion;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ShieldDestroyedEventTest extends TestCase {


  public void test0() {
    final Lion l = new Lion();
    final ShieldDestroyedEvent e = new ShieldDestroyedEvent(1, l, Attribute.LIFE);
    assertEquals(CellEffectType.SHIELD_DESTROYED, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals(Attribute.LIFE, e.getAttribute());
  }

  public void test1() {
    final Lion l = new Lion();
    final ShieldDestroyedEvent e = new ShieldDestroyedEvent(new Cell(1), l, null);
    assertEquals(CellEffectType.SHIELD_DESTROYED, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertNull(e.getAttribute());
  }
}
