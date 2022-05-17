package irvine.util.graphics;

import chaos.graphics.MockGraphics;
import chaos.graphics.MockScreen;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ThunderboltTest extends TestCase {

  public void test() {
    final MockScreen screen = new MockScreen();
    final MockGraphics g = new MockGraphics();
    Thunderbolt.draw(screen, g, 20, 20, 200, 160, 100);
    final String s = g.toString();
    //System.out.println(s.replace('#', '\n').substring(0, 5000));
    TestUtils.containsAll(s,
      "getClip()",
      "setColor(java.awt.Color[r=255,g=0,b=0])",
      "setColor(java.awt.Color[r=255,g=165,b=0])",
      "setColor(java.awt.Color[r=0,g=0,b=0])",
      "setColor(java.awt.Color[r=178,g=0,b=0])",
      "drawPolyline(4)",
      "setClip(null)");
  }
}
