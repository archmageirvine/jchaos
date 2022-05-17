package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ShootIconEventTest extends TestCase {


  public void test() {
    final ShootIconEvent e = new ShootIconEvent(1);
    assertEquals(1, e.getCell());
    final ShootIconEvent n = new ShootIconEvent(-1);
    assertEquals(-1, n.getCell());
  }

}
