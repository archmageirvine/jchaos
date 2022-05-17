package irvine.util.graphics;

import chaos.graphics.MockGraphics;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class StarsTest extends TestCase {

  public void test() {
    final MockGraphics g = new MockGraphics();
    Stars.drawStar(g, 7, 11, 57, 7, 3);
    final String res = g.toString();
    //System.out.println(res);
    assertEquals("L(7,68,32,-40)#L(32,-40,-38,47)#L(-38,47,63,-2)#L(63,-2,-49,-2)#L(-49,-2,52,47)#L(52,47,-18,-40)#L(-18,-40,7,68)#", res);
  }
}
