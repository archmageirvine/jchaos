package chaos.util;

import java.util.HashSet;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class HighlightEventTest extends TestCase {


  public void test() {
    final HashSet<Integer> h = new HashSet<>();
    final HighlightEvent e = new HighlightEvent(h);
    assertEquals(h, e.getCells());
    final HighlightEvent n = new HighlightEvent(null);
    assertNull(n.getCells());
  }

}
