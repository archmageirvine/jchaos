package chaos.util;

import chaos.board.Cell;
import chaos.common.PowerUps;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PowerUpEventTest extends TestCase {

  public void test0() {
    final Lion l = new Lion();
    final PowerUpEvent e = new PowerUpEvent(1, l, PowerUps.DOUBLE);
    assertEquals(CellEffectType.POWERUP, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals(PowerUps.DOUBLE, e.getPowerUp());
  }

  public void test1() {
    final Lion l = new Lion();
    final PowerUpEvent e = new PowerUpEvent(new Cell(1), l, null);
    assertEquals(CellEffectType.POWERUP, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertNull(e.getPowerUp());
  }
}
