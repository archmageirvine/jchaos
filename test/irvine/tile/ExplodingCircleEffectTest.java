package irvine.tile;

import java.util.List;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ExplodingCircleEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new ExplodingCircleEffect(16, 23, 57);
  }

  static boolean isEqual(final TileImage image, final int[] data) {
    for (int i = 0, y = 0; y < image.getHeight(); ++y) {
      for (int x = 0; x < image.getWidth(); ++x, ++i) {
        if (image.getPixel(x, y) != data[i]) {
          System.out.println("Bad pixel (" + x + "," + y + ") expected " + data[i] + " got " + image.getPixel(x, y));
          return false;
        }
      }
    }
    return true;
  }

  public void testBadCons() {
    try {
      new ExplodingCircleEffect(0, 0, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
    try {
      new ExplodingCircleEffect(-1, 0, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
  }

  public void testSize15() {
    final TileEffect ef = new ExplodingCircleEffect(15, 23, 57);
    for (int i = 0; i < 15; ++i) {
      assertNotNull(ef.next());
    }
    assertNull(ef.next());
    assertNull(ef.next());
  }

  public void testSize16() {
    final TileEffect ef = getEffect();
    for (int i = 0; i < 15; ++i) {
      assertNotNull(ef.next());
    }
    assertNull(ef.next());
    assertNull(ef.next());
  }

  public void testSize16all() {
    final TileEffect ef = getEffect();
    final List<TileImage> list = ef.list();
    assertEquals(15, list.size());
    assertTrue(isEqual(list.get(14), new int[] {
      23, 23, 23, 23, 23, 23, 57, 57, 57, 57, 57, 23, 23, 23, 23, 23,
      23, 23, 23, 23, 57, 57, 23, 23, 23, 23, 23, 57, 57, 23, 23, 23,
      23, 23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23, 23,
      23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23,
      23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23,
      23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23,
      23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23,
      23, 23, 23, 57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57, 23, 23,
      23, 23, 23, 23, 57, 57, 23, 23, 23, 23, 23, 57, 57, 23, 23, 23,
      23, 23, 23, 23, 23, 23, 57, 57, 57, 57, 57, 23, 23, 23, 23, 23,
      23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23}));
  }

  public void testSize16b() {
    final TileEffect ef = new ExplodingCircleEffect(16, 23, 57);
    for (int i = 0; i < 7; ++i) {
      assertNotNull(ef.next());
    }
    assertEquals(15 - 7, ef.list().size());
  }

  public void testSize4() {
    final TileEffect ef = new ExplodingCircleEffect(4, 0, 1);
    assertTrue(isEqual(ef.next(), new int[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    assertTrue(isEqual(ef.next(), new int[] {0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0}));
    assertTrue(isEqual(ef.next(), new int[] {0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0}));
    assertNull(ef.next());
  }

}

