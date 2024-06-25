package chaos.graphics;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class HighlightUtilsTest extends TestCase {

  public void testHighlight() {
    HighlightUtils.highlight(null, 10, 20, 32, 32);
    final MockGraphics g = new MockGraphics();
    HighlightUtils.highlight(g, 10, 20, 32, 32);
    assertEquals("setColor(java.awt.Color[r=255,g=255,b=0])#L(10,20,41,20)#L(10,51,41,51)#L(10,20,10,51)#L(41,20,41,51)#"
      + "setColor(java.awt.Color[r=255,g=0,b=0])#L(10,20,13,20)#L(41,51,38,51)#L(18,20,21,20)#L(33,51,30,51)#"
      + "L(26,20,29,20)#L(25,51,22,51)#L(34,20,37,20)#L(17,51,14,51)#L(10,51,10,48)#L(41,20,41,23)#L(10,43,10,40)#"
      + "L(41,28,41,31)#L(10,35,10,32)#L(41,36,41,39)#L(10,27,10,24)#L(41,44,41,47)#", g.toString());
  }

  public void testLightHighlight() {
    HighlightUtils.lightHighlight(null, 10, 20, 32);
    final MockGraphics g = new MockGraphics();
    HighlightUtils.lightHighlight(g, 10, 20, 32);
    assertEquals("setColor(java.awt.Color[r=127,g=0,b=0])#L(10,20,41,20)#L(10,51,41,51)#L(10,20,10,51)#L(41,20,41,51)#", g.toString());
  }
}
