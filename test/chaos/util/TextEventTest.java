package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TextEventTest extends TestCase {


  public void test() {
    final TextEvent e = new TextEvent("hi there");
    assertEquals("hi there", e.toString());
  }

  public void test2() {
    try {
      new TextEvent(null);
      fail("Accepted null");
    } catch (final NullPointerException e) {
      // ok
    }
  }

}
