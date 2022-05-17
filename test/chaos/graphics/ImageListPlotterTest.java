package chaos.graphics;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ImageListPlotterTest extends TestCase {

  public void test() {
    final ImageList l = ImageList.getList("fireball", 5);
    final MockGraphics g = new MockGraphics();
    final ImageListPlotter p = new ImageListPlotter(l, g, 0);
    p.plot(16, 16);
    p.plot(120, 72);
    assertEquals("I(0,0,null)#I(0,0,null)#I(104,56,null)#", g.toString());
  }
}
