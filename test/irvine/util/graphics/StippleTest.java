package irvine.util.graphics;

import irvine.tile.TileImage;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class StippleTest extends TestCase {

  public void test() {
    final TileImage stippled = Stipple.stipple(0, 0, 10, 12, Stipple.BGC);
    assertEquals(10, stippled.getWidth());
    assertEquals(12, stippled.getHeight());
    int c0 = 0;
    int c1 = 0;
    for (int y = 0; y < 12; ++y) {
      for (int x = 0; x < 10; ++x) {
        final int p = stippled.getPixel(x, y);
        assertTrue(p == Stipple.BGC[0] || p == Stipple.BGC[1]);
        if (p == Stipple.BGC[0]) {
          ++c0;
        } else {
          ++c1;
        }
      }
    }
    assertTrue(c0 > 20);
    assertTrue(c1 > 20);
    assertEquals(2, Stipple.BGC.length);
    assertEquals(0xFF000080, Stipple.BGC[0]);
    assertEquals(0xFF000050, Stipple.BGC[1]);
  }

  public void test2() {
    final TileImage stippled = Stipple.stipple(0, 0, 10, 12);
    assertEquals(10, stippled.getWidth());
    assertEquals(12, stippled.getHeight());
    int c0 = 0;
    int c1 = 0;
    for (int y = 0; y < 12; ++y) {
      for (int x = 0; x < 10; ++x) {
        final int p = stippled.getPixel(x, y);
        assertTrue(p == Stipple.BGC[0] || p == Stipple.BGC[1]);
        if (p == Stipple.BGC[0]) {
          ++c0;
        } else {
          ++c1;
        }
      }
    }
    assertTrue(c0 > 20);
    assertTrue(c1 > 20);
  }

  public void testNesting() {
    final TileImage stippled1 = Stipple.stipple(0, 0, 20, 20);
    final TileImage stippled2 = Stipple.stipple(5, 5, 10, 10);
    for (int y = 0; y < 10; ++y) {
      for (int x = 0; x < 10; ++x) {
        assertEquals(stippled1.getPixel(5 + x, 5 + y), stippled2.getPixel(x, y));
      }
    }
  }

}
