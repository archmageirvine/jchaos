package chaos.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import junit.framework.TestCase;
import chaos.util.ChaosProperties;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RenderTest extends TestCase {

  public void test1() {
    final MockGraphics g = new MockGraphics();
    final BufferedImage im = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
    Render.renderImage(g, im, 42, 43);
    assertEquals("I(42,43,null)#", g.toString());
    assertEquals(4, Render.getWidthOfImage(im));
    assertEquals(-1, Render.getWidthOfImage(null));
  }

  private static class MyGraphics extends MockGraphics {
    @Override
    public boolean drawImage(final Image img, final int x, final int y, final ImageObserver observer) {
      mHistory.append('X');
      return false;
    }
  }

  public void testMyGraphics() {
    ChaosProperties.properties().setProperty("chaos.short.sleep", "0");
    final MockGraphics g = new MyGraphics();
    final BufferedImage im = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
    Render.renderImage(g, im, 42, 43);
    assertEquals("XXXXXXXXXXXXXXXXXXXXX", g.toString());
  }

  private static class MyImage extends BufferedImage {

    int mFail = 0;

    MyImage() {
      super(4, 4, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public int getWidth(final ImageObserver observer) {
      ++mFail;
      return -1;
    }
  }

  public void testMyImage() {
    ChaosProperties.properties().setProperty("chaos.short.sleep", "0");
    final MyImage im = new MyImage();
    assertEquals(-1, Render.getWidthOfImage(im));
    assertEquals(22, im.mFail);
  }

  public void testRadio() {
    final MockGraphics g = new MyGraphics();
    Render.radio(g, 5, 8, 3, 0);
    Render.radio(g, 5, 9, 3, 1);
    Render.radio(g, 4, 8, 3, 2);
    assertEquals("setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(0,2,5,5)#fillOval(8,2,5,5)#fillOval(16,2,5,5)#setColor(java.awt.Color[r=255,g=255,b=255])#drawOval(0,2,5,5)#drawOval(8,2,5,5)#drawOval(16,2,5,5)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(1,3,3,3)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(0,2,5,5)#fillOval(9,2,5,5)#fillOval(18,2,5,5)#setColor(java.awt.Color[r=255,g=255,b=255])#drawOval(0,2,5,5)#drawOval(9,2,5,5)#drawOval(18,2,5,5)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(10,3,3,3)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(0,1,4,4)#fillOval(8,1,4,4)#fillOval(16,1,4,4)#setColor(java.awt.Color[r=255,g=255,b=255])#drawOval(0,1,4,4)#drawOval(8,1,4,4)#drawOval(16,1,4,4)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(17,3,2,2)#", g.toString());
  }
}
