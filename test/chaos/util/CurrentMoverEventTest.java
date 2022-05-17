package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CurrentMoverEventTest extends TestCase {


  public void test() {
    final CurrentMoverEvent e = new CurrentMoverEvent(2);
    assertEquals(2, e.getCurrentPlayer());
  }

}
