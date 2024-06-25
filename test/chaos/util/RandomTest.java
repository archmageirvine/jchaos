package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the Random class.
 * @author Sean A. Irvine
 */
public class RandomTest extends TestCase {


  public void testRandomness() {

    for (int i = 0; i < 100; ++i) {
      final int a = Random.nextInt(Integer.MAX_VALUE);
      for (int j = 0; j < 100; ++j) {
        assertTrue(a != Random.nextInt(Integer.MAX_VALUE));
      }
    }
  }

  public void testBooleans() {
    int t = 0;
    for (int i = 0; i < 1000; ++i) {
      if (Random.nextBoolean()) {
        ++t;
      }
    }
    assertTrue(t > 100);
    assertTrue(t < 900);
  }

}
