package chaos.graphics;

import java.awt.image.BufferedImage;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ImageListTest extends TestCase {

  public void checkFireBall(final int bits) {
    final ImageList l = ImageList.getList("fireball", bits);
    assertEquals(12, l.size());
    final int z = 1 << bits;
    for (final BufferedImage im : l) {
      assertEquals(z, im.getHeight());
      assertEquals(z, im.getWidth());
    }
    assertNotNull(l.getMask());
    assertEquals(l, ImageList.getList("fireball", bits));
  }

  public void test() {
    checkFireBall(4);
    checkFireBall(5);
  }
}
