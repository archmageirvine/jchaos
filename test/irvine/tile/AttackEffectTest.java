package irvine.tile;

import java.util.List;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AttackEffectTest extends TestCase {

  public void test() {
    final AttackEffect ae = new AttackEffect(32, "10", 0xFF0000, 0xFF00, 0xFF00, 0xFF00, 4);
    final List<TileImage> list = ae.list();
    assertEquals(4, list.size());
    for (final TileImage image : list) {
      assertNotNull(image);
      assertEquals(32, image.getWidth());
      assertEquals(32, image.getHeight());
      int red = 0;
      for (int x = 0; x < 32; ++x) {
        for (int y = 0; y < 32; ++y) {
          final int px = image.getPixel(x, y);
          if (px == 0xFF0000) {
            ++red;
          }
        }
      }
      assertTrue(red > 600 && red < 800);
    }
  }
}
