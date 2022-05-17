package chaos.util;

import junit.framework.TestCase;
import chaos.common.monster.Lion;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AttackCellEffectEventTest extends TestCase {


  public void test() {
    final Lion l = new Lion();
    final AttackCellEffectEvent e = new AttackCellEffectEvent(1, 10, l, CombatUtils.NORMAL);
    assertEquals(CellEffectType.ATTACK_EVENT, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals(10, e.getDamage());
    assertEquals(l, e.getOffence());
    assertEquals(CombatUtils.NORMAL, e.getType());
  }
}
