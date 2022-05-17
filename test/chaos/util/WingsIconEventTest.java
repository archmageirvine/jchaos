package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class WingsIconEventTest extends TestCase {


  public void test() {
    final WingsIconEvent e = new WingsIconEvent(1);
    assertEquals(1, e.getCell());
    final WingsIconEvent n = new WingsIconEvent(-1);
    assertEquals(-1, n.getCell());
  }

}
