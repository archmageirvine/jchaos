package chaos.graphics;

import junit.framework.TestCase;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ImageLoaderTest extends TestCase {

  public void test() {
    assertNull(ImageLoader.getImage("nosuchimage"));
    assertNotNull(ImageLoader.getImage("chaos/resources/icons/life.png"));
  }

  public void testUpArrow() {
    final BufferedImage im = ImageLoader.upArrow();
    assertNotNull(im);
    assertEquals(im, ImageLoader.upArrow());
    assertEquals(12, im.getWidth());
    assertEquals(6, im.getHeight());
    final int cyan = Color.CYAN.getRGB();
    int c = 0;
    for (int y = 0; y < 6; ++y) {
      for (int x = 0; x < 12; ++x) {
        if (cyan == im.getRGB(x, y)) {
          ++c;
        }
      }
    }
    assertEquals(30, c);
  }

  public void testDownArrow() {
    final BufferedImage im = ImageLoader.downArrow();
    assertNotNull(im);
    assertEquals(im, ImageLoader.downArrow());
    assertEquals(12, im.getWidth());
    assertEquals(6, im.getHeight());
    final int cyan = Color.CYAN.getRGB();
    int c = 0;
    for (int y = 0; y < 6; ++y) {
      for (int x = 0; x < 12; ++x) {
        if (cyan == im.getRGB(x, y)) {
          ++c;
        }
      }
    }
    assertEquals(33, c);
  }

  public void testOverlay() {
    final BufferedImage im = ImageLoader.translucentOverlayWithBorder(3, 0xFF, 0xFFFF);
    assertEquals(3, im.getWidth());
    assertEquals(3, im.getHeight());
    assertEquals(0x6000FFFF, im.getRGB(0, 0));
    assertEquals(0x6000FFFF, im.getRGB(0, 1));
    assertEquals(0x6000FFFF, im.getRGB(0, 2));
    assertEquals(0x6000FFFF, im.getRGB(1, 0));
    assertEquals(0x600000FF, im.getRGB(1, 1));
    assertEquals(0x6000FFFF, im.getRGB(1, 2));
    assertEquals(0x6000FFFF, im.getRGB(2, 0));
    assertEquals(0x6000FFFF, im.getRGB(2, 1));
    assertEquals(0x6000FFFF, im.getRGB(2, 2));
  }
}
