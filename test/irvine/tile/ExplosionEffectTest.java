package irvine.tile;

import java.util.Random;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ExplosionEffectTest extends AbstractTileEffectTest {


  @Override
  public TileEffect getEffect() {
    final TileImage i = new TileImage(16, 16);
    i.fill(0x38);
    return new ExplosionEffect(i, 0, false);
  }

  public void testBadCons() {
    try {
      new ExplosionEffect(null, 0, false);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new ExplosionEffect(new TileImage(1, 4), 0, false);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad dimension", e.getMessage());
    }
    try {
      new ExplosionEffect(new TileImage(3, 3), 0, false);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad dimension", e.getMessage());
    }
    try {
      new ExplosionEffect(new TileImage(65536, 65536), 0, false);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad dimension", e.getMessage());
    }
  }

  public void testSize16() {
    final TileEffect ef = getEffect();
    assertEquals(1050896, ef.next().hashCode());
    assertEquals(1053712, ef.next().hashCode());
    assertEquals(1064720, ef.next().hashCode());
    assertEquals(1051664, ef.next().hashCode());
    assertEquals(1051920, ef.next().hashCode());
    assertEquals(1052176, ef.next().hashCode());
    assertEquals(1052432, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize16d() {
    final TileImage i = new TileImage(16, 16);
    i.fill(0x38);
    final TileEffect ef = new ExplosionEffect(i, 0, true);
    assertEquals(1063696, ef.next().hashCode());
    assertEquals(1058576, ef.next().hashCode());
    assertEquals(1063696, ef.next().hashCode());
    assertEquals(1061648, ef.next().hashCode());
    assertEquals(1061904, ef.next().hashCode());
    assertEquals(1057552, ef.next().hashCode());
    assertEquals(1050384, ef.next().hashCode());
    assertEquals(1056016, ef.next().hashCode());
    assertEquals(1050128, ef.next().hashCode());
    assertEquals(1055248, ef.next().hashCode());
    assertEquals(1054480, ef.next().hashCode());
    assertEquals(1056016, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1052432, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testPixelConservation() {
    final Random r = new Random();
    for (int q = 0; q < 10; ++q) {
      final int w = (2 + r.nextInt(100)) & ~1;
      final TileImage i = new TileImage(w, w);
      for (int z = 0; z < w * w / 3; ++z) {
        i.setPixel(r.nextInt(w), r.nextInt(w), ~0);
      }
      TileImage k, j = i;
      ExplosionEffect ef = new ExplosionEffect(i, 0, false);
      while ((k = ef.next()) != null) {
        int pc = 0;
        for (int u = 1; u < w - 1; ++u) {
          for (int v = 1; v < w - 1; ++v) {
            if (j.getPixel(u, v) == ~0) {
              ++pc;
            }
          }
        }
        int pcc = 0;
        for (int u = 0; u < w; ++u) {
          for (int v = 0; v < w; ++v) {
            final int p = k.getPixel(u, v);
            if (p == ~0) {
              ++pcc;
            } else {
              assertEquals(0, p);
            }
          }
        }
        assertEquals(pc, pcc);
        j = k;
      }
      j = i;
      ef = new ExplosionEffect(i, 0, true);
      while ((k = ef.next()) != null) {
        int pc = 0;
        for (int u = 1; u < w - 1; ++u) {
          for (int v = 1; v < w - 1; ++v) {
            if (j.getPixel(u, v) == ~0) {
              ++pc;
            }
          }
        }
        int pcc = 0;
        for (int u = 0; u < w; ++u) {
          for (int v = 0; v < w; ++v) {
            final int p = k.getPixel(u, v);
            if (p == ~0) {
              ++pcc;
            } else {
              assertEquals(0, p);
            }
          }
        }
        assertEquals(pc, pcc);
        j = k;
      }
    }
  }

  public void testVectorField4() {
    final TileImage vf = ExplosionEffect.getVectorField(4);
    assertEquals(-1, vf.getPixel(0, 0));
    assertEquals(-1, vf.getPixel(1, 0));
    assertEquals(-1, vf.getPixel(2, 0));
    assertEquals(-1, vf.getPixel(3, 0));
    assertEquals(-1, vf.getPixel(0, 1));
    assertEquals(0, vf.getPixel(1, 1));
    assertEquals(3, vf.getPixel(2, 1));
    assertEquals(-1, vf.getPixel(3, 1));
    assertEquals(-1, vf.getPixel(0, 2));
    assertEquals(3 << 16, vf.getPixel(1, 2));
    assertEquals((3 << 16) + 3, vf.getPixel(2, 2));
    assertEquals(-1, vf.getPixel(3, 2));
    assertEquals(-1, vf.getPixel(0, 3));
    assertEquals(-1, vf.getPixel(1, 3));
    assertEquals(-1, vf.getPixel(2, 3));
    assertEquals(-1, vf.getPixel(3, 3));
  }
}

