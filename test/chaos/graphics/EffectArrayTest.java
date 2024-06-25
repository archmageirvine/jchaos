package chaos.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class EffectArrayTest extends TestCase {

  private void checkColors(final BufferedImage im, final int... color) {
    for (int y = 0; y < im.getHeight(); ++y) {
      for (int x = 0; x < im.getWidth(); ++x) {
        // Ignores alpha channel
        final int c = im.getRGB(x, y) & 0xFFFFFF;
        boolean ok = false;
        for (final int u : color) {
          if (c == u) {
            ok = true;
            break;
          }
        }
        assertTrue("Color was: 0x" + Integer.toHexString(c), ok);
      }
    }
  }

  public void testGetShieldArray() {
    final BufferedImage[] a = new EffectArray(4).getShieldArray(Color.RED);
    assertEquals(8, a.length);
    for (final BufferedImage t : a) {
      checkColors(t, 0xFF0000, 0x000000);
    }
  }

  public void test() {
    final EffectArray ea = new EffectArray(4);
    assertEquals(7, ea.mCorpseExplosionEffect.length);
    assertEquals(15, ea.mWhiteCircleExplode.length);
    checkColors(ea.mWhiteCircleExplode[5], 0xFFFFFF, 0);
    assertEquals(15, ea.mGreenCircleExplode.length);
    checkColors(ea.mGreenCircleExplode[5], 0xFF00, 0);
    assertEquals(15, ea.mRedCircleExplode.length);
    checkColors(ea.mRedCircleExplode[5], 0xFF0000, 0);
    assertEquals(15, ea.mWarp.length);
    checkColors(ea.mWarp[5], 0xFFFFFF, 0);
    assertEquals(12, ea.mTwirlEffect.length);
    checkColors(ea.mTwirlEffect[5], 0xFFFF, 0);
  }
}
