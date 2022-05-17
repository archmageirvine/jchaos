package irvine.tile;


/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PixelFadeEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    final TileImage i = new TileImage(3, 3);
    i.fill(~0);
    return new PixelFadeEffect(i, i, 9);
  }

  public void testBadCons() {
    try {
      new PixelFadeEffect(null, new TileImage(3, 4), 12);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new PixelFadeEffect(null, 0, 1);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), null, 12);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(4, 3), 12);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Size mismatch", e.getMessage());
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(3, 4), 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad steps", e.getMessage());
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(3, 4), -12);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad steps", e.getMessage());
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(3, 4), 5);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad steps", e.getMessage());
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(3, 4), 24);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad steps", e.getMessage());
    }
    try {
      new PixelFadeEffect(new TileImage(3, 4), new TileImage(3, 4), 11);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad steps", e.getMessage());
    }
  }

  public void testSize3() {
    final TileEffect ef = new PixelFadeEffect(new TileImage(3, 3), new TileImage(3, 3), 9);
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize3w() {
    final TileEffect ef = new PixelFadeEffect(new TileImage(3, 3), 0xDEADBEEF, 3);
    assertNotNull(ef.next());
    assertNotNull(ef.next());
    assertEquals(-745881884, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testPixelZero() {
    final int[] c = new int[9];
    for (int i = 0; i < 20; ++i) {
      final TileEffect ef = new PixelFadeEffect(new TileImage(3, 3), 0xDEADBEEF, 9);
      final TileImage t = ef.next();
      if (t.getPixel(0, 0) == 0xDEADBEEF) {
        c[0]++;
      }
      if (t.getPixel(0, 1) == 0xDEADBEEF) {
        c[1]++;
      }
      if (t.getPixel(0, 2) == 0xDEADBEEF) {
        c[2]++;
      }
      if (t.getPixel(1, 0) == 0xDEADBEEF) {
        c[3]++;
      }
      if (t.getPixel(1, 1) == 0xDEADBEEF) {
        c[4]++;
      }
      if (t.getPixel(1, 2) == 0xDEADBEEF) {
        c[5]++;
      }
      if (t.getPixel(2, 0) == 0xDEADBEEF) {
        c[6]++;
      }
      if (t.getPixel(2, 1) == 0xDEADBEEF) {
        c[7]++;
      }
      if (t.getPixel(2, 2) == 0xDEADBEEF) {
        c[8]++;
      }
    }
    for (int i = 0; i < c.length; ++i) {
      if (c[i] > 15) {
        fail("Strange behaviour at pixel " + i);
      }
    }
  }
}

