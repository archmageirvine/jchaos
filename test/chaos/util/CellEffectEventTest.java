package chaos.util;

import chaos.board.Cell;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class CellEffectEventTest extends TestCase {


  public void test() {
    final CellEffectEvent e = new CellEffectEvent(1, CellEffectType.NON_EVENT);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getCellNumber());
  }

  public void testNull() {
    final CellEffectEvent e = new CellEffectEvent(1, null);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getCellNumber());
  }

  public void test2() {
    try {
      new CellEffectEvent(0, CellEffectType.ATTACK_EVENT);
      fail("Accepted not allowed");
    } catch (final IllegalArgumentException e) {
      assertEquals("ATTACK_EVENT only for AttackCellEffectEvent", e.getMessage());
    }
  }

  public void testC() {
    final Cell c = new Cell(1);
    final CellEffectEvent e = new CellEffectEvent(c, CellEffectType.NON_EVENT);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getCellNumber());
  }

  public void testNullC() {
    final Cell c = new Cell(1);
    final Lion l = new Lion();
    final CellEffectEvent e = new CellEffectEvent(c, null, l);
    assertEquals(CellEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals(l, e.getActor());
  }

  public void test2C() {
    final Cell c = new Cell(0);
    try {
      new CellEffectEvent(c, CellEffectType.ATTACK_EVENT);
      fail("Accepted not allowed");
    } catch (final IllegalArgumentException e) {
      assertEquals("ATTACK_EVENT only for AttackCellEffectEvent", e.getMessage());
    }
  }

}
