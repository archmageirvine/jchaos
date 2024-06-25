package chaos.util;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class WeaponEffectEventTest extends TestCase {


  public void test() {
    final WeaponEffectEvent e = new WeaponEffectEvent(1, 2, WeaponEffectType.NON_EVENT);
    assertEquals(WeaponEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getSource());
    assertEquals(2, e.getTarget());
    assertNull(e.getCause());
    assertNull(e.getAttribute());
  }

  public void testCause() {
    final Lion l = new Lion();
    final WeaponEffectEvent e = new WeaponEffectEvent(1, 2, WeaponEffectType.NON_EVENT, l, Attribute.LIFE);
    assertEquals(WeaponEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getSource());
    assertEquals(2, e.getTarget());
    assertEquals(l, e.getCause());
    assertEquals(Attribute.LIFE, e.getAttribute());
  }

  public void testNull() {
    final WeaponEffectEvent e = new WeaponEffectEvent(1, 2, null);
    assertEquals(WeaponEffectType.NON_EVENT, e.getEventType());
    assertEquals(1, e.getSource());
    assertEquals(2, e.getTarget());
    try {
      new WeaponEffectEvent(null, new Cell(0), WeaponEffectType.NON_EVENT);
      fail();
    } catch (final NullPointerException ex) {
      // ok
    }
    try {
      new WeaponEffectEvent(new Cell(0), null, WeaponEffectType.NON_EVENT);
      fail();
    } catch (final NullPointerException ex) {
      // ok
    }
  }

}
