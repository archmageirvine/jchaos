package irvine.heraldry;

import chaos.graphics.MockGraphics;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DisplayShieldTest extends TestCase {

  public void test() {
    final Shape shape = new Cartouche();
    final MockGraphics g = new MockGraphics();
    DisplayShield.render(g, shape);
    final String s = g.toString();
    //System.out.println(s);
    TestUtils.containsAll(s,
      "setColor(java.awt.Color[r=0,g=0,b=255])",
      "fillRect(0,0,330,460)",
      "translate(-5,-400)",
      "drawOval(0,0,300,400)",
      "translate(20,30)");
  }
}
