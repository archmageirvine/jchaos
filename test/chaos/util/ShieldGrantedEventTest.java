package chaos.util;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class ShieldGrantedEventTest extends TestCase {


  public void test0() {
    final Lion l = new Lion();
    final ShieldGrantedEvent e = new ShieldGrantedEvent(1, l, Attribute.LIFE);
    assertEquals(CellEffectType.SHIELD_GRANTED, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals(Attribute.LIFE, e.getAttribute());
  }

  public void test1() {
    final Lion l = new Lion();
    final ShieldGrantedEvent e = new ShieldGrantedEvent(new Cell(1), l, null);
    assertEquals(CellEffectType.SHIELD_GRANTED, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertNull(e.getAttribute());
  }
}
