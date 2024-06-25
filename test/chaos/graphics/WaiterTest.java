package chaos.graphics;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class WaiterTest extends TestCase {

  public void test() {
    // Just make sure no exception happens
    Waiter.waitUntilDisplayIsReady();
  }
}
