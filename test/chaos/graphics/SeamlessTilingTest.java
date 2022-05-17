package chaos.graphics;

import junit.framework.TestCase;

import java.awt.image.BufferedImage;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class SeamlessTilingTest extends TestCase {
  public void test() {
    final SeamlessTiling t = new SeamlessTiling("chaos/graphics/generic/water.jpg");
    final BufferedImage im = t.getTile(0, 0, 5);
    assertNotNull(im);
    assertEquals(im, t.getTile(16, 16, 5));
    assertEquals(im, t.getTile(0, 0, 5));
  }
}
