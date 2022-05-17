package irvine.tile;

import static irvine.tile.ExplodingCircleEffectTest.isEqual;

import java.util.List;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ExplodingSquareEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new ExplodingSquareEffect(16, 57, new int[] {23, 22});
  }

  public void testBadCons() {
    try {
      new ExplodingSquareEffect(0, 0, new int[] {5});
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
    try {
      new ExplodingSquareEffect(0, 1, new int[] {5});
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
    try {
      new ExplodingSquareEffect(0, 3, new int[] {5});
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
    try {
      new ExplodingSquareEffect(-1, 0, new int[] {5});
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad width.", e.getMessage());
    }
    try {
      new ExplodingSquareEffect(2, 3, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new ExplodingSquareEffect(2, 0, new int[0]);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad colors.", e.getMessage());
    }
  }

  public void testSize16() {
    final TileEffect ef = new ExplodingSquareEffect(16, 57, new int[] {23});
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
    assertTrue(isEqual(list.get(6), new int[] {
      57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57,
      57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      57, 23, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 57,
      57, 23, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 22, 22, 22, 22, 22, 22, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 23, 23, 23, 23, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 22, 22, 22, 22, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 22, 23, 23, 22, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 22, 23, 23, 22, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 22, 22, 22, 22, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 23, 23, 23, 23, 23, 23, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 22, 22, 22, 22, 22, 22, 22, 22, 23, 22, 23, 57,
      57, 23, 22, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 22, 23, 57,
      57, 23, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 57,
      57, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 57,
      57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57}));
  }

  public void testSize16b() {
    final TileEffect ef = new ExplodingSquareEffect(16, 57, new int[] {23});
    for (int i = 0; i < 7; ++i) {
      assertNotNull(ef.next());
    }
    assertEquals(15 - 7, ef.list().size());
  }

  public void testSize4() {
    final TileEffect ef = new ExplodingSquareEffect(4, 0, new int[] {1});
    assertTrue(isEqual(ef.next(), new int[] {0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0}));
    assertTrue(isEqual(ef.next(), new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}));
    assertTrue(isEqual(ef.next(), new int[] {1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1}));
    assertNull(ef.next());
  }

}

