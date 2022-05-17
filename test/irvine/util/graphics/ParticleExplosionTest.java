package irvine.util.graphics;

import irvine.tile.TileImage;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ParticleExplosionTest extends TestCase {

  private static final long CONS = 0b1111111111111111111111111111111111111111000000010000000000000000L;

  public void test() {
    final ParticleExplosion pe = new ParticleExplosion(48, 48, 5, 0xFFFFE030, 24, 44);
    int frames = 0;
    while (pe.update()) {
      ++frames;
      final TileImage image = pe.image();
      assertNotNull(image);
      assertEquals(48, image.getHeight());
      assertEquals(48, image.getWidth());
      long or = 0;
      long and = ~0;
      for (int y = 0; y < image.getHeight(); ++y) {
        for (int x = 0; x < image.getWidth(); ++x) {
          or |= image.getPixel(x, y);
          and &= image.getPixel(x, y);
        }
      }
      if (frames < 20) {
        assertEquals(CONS, or & CONS);
        //System.out.println(Long.toBinaryString(or));
      }
      assertTrue(and != or);
    }
    assertTrue(frames > 200 && frames < 400);
  }
}
