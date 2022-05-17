package irvine.util.graphics;

import chaos.graphics.MockGraphics;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PlasmaTest extends TestCase {

  public void testVertical() {
    final MockGraphics g = new MockGraphics();
    Plasma.plasma(g, 0, 100, 500, 100, 16, 0, 1);
    final String s = g.toString();
    //System.out.println(s);
    TestUtils.containsAll(s,
      "setColor(java.awt.Color[r=0,g=0,b=0])",
      "drawPolyline(500)",
      "fillRect(0,84,501,32)"
    );
  }

  public void testHorizontal() {
    final MockGraphics g = new MockGraphics();
    Plasma.plasma(g, 100, 100, 100, 200, 32, 0xFF, 10);
    final String s = g.toString();
    TestUtils.containsAll(s,
      "setColor(java.awt.Color[",
      "drawPolyline(100)",
      "fillRect(68,100,64,101)"
    );
  }

  public void testDiagonal() {
    final MockGraphics g = new MockGraphics();
    Plasma.plasma(g, 100, 100, 300, 300, 24, 0xFFFF, 4);
    final String s = g.toString();
    TestUtils.containsAll(s,
      "setColor(java.awt.Color[",
      "drawPolyline(282)",
      "fillPolygon(4)"
    );
  }
}
