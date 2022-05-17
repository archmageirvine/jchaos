package chaos.util;

import junit.framework.TestCase;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class SleepTest extends TestCase {

  public void testSleep() {
    Sleep.sleep(1); // force class to be loaded
    long t = System.currentTimeMillis();
    Sleep.sleep(10);
    long s = System.currentTimeMillis();
    assertTrue(s - t < 25);
    assertTrue(s - t > 5);
    t = System.currentTimeMillis();
    Sleep.sleep(100);
    s = System.currentTimeMillis();
    assertTrue(s - t < 130);
    assertTrue(s - t > 80);
  }

  public void testShortSleep() {
    ChaosProperties.properties().setProperty("chaos.short.sleep", "100");
    Sleep.shortSleep(); // Make sure classes etc. loaded before proper timing test
    final long t = System.currentTimeMillis();
    Sleep.shortSleep();
    final long s = System.currentTimeMillis();
    final long delta = s - t;
    assertTrue(String.valueOf(delta), delta < 180);
    assertTrue(delta > 80);
  }
}
