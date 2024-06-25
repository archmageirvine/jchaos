package irvine.util.graphics;

import chaos.graphics.MockGraphics;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class WallsTest extends TestCase {

  public void test() {
    final MockGraphics g = new MockGraphics();
    Walls.drawWall(g, 50, 100, 64, 5, 4, Walls.BRICK_COLOURS, Walls.MORTAR_COLOURS);
    final String s = g.toString();
    TestUtils.containsAll(s,
      "I(50,100,null)#",
      "I(105,138,null)#"
    );
  }

  public void testDemo() {
    final MockGraphics g = new MockGraphics();
    Walls.demo(g);
    final String s = g.toString();
    int hash = 0;
    for (int k = 0; k < s.length(); ++k) {
      if (s.charAt(k) == '#') {
        ++hash;
      }
    }
    assertEquals(798, hash);
  }
}
