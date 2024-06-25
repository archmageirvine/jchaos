package irvine.tile;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TileImageTest extends TestCase {

  public void testBadCons() {
    try {
      new TileImage(0, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("width must be at least 1", e.getMessage());
    }
    try {
      new TileImage(-1, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("width must be at least 1", e.getMessage());
    }
    try {
      new TileImage(1, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("height must be at least 1", e.getMessage());
    }
    try {
      new TileImage(1, -1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("height must be at least 1", e.getMessage());
    }
    try {
      new TileImage(null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testTiny() {
    final TileImage i = new TileImage(1, 1);
    assertEquals(0, i.getPixel(0, 0));
    i.setPixel(0, 0, ~0);
    assertEquals(~0, i.getPixel(0, 0));
    i.setPixel(0, 0, 0);
    assertEquals(0, i.getPixel(0, 0));
    i.fill(~0);
    assertEquals(~0, i.getPixel(0, 0));
    assertNotNull(i.toBufferedImage());
  }

  public void testEqualsAndHashCode() {
    final TileImage i = new TileImage(2, 2);
    assertEquals(131074, i.hashCode());
    i.setPixel(0, 0, 1);
    assertEquals(131075, i.hashCode());
    i.setPixel(0, 1, 2);
    assertEquals(131077, i.hashCode());
    i.setPixel(1, 0, 3);
    assertEquals(131075, i.hashCode());
    i.setPixel(1, 1, 4);
    assertEquals(131091, i.hashCode());
    assertFalse(i.equals(new Object()));
    assertFalse(i.equals(null));
    assertFalse(i.equals(null));
    assertFalse(i.equals(new TileImage(1, 1)));
    assertFalse(i.equals(new TileImage(2, 2)));
    assertTrue(i.equals(i));
    TileImage j = new TileImage(2, 2);
    j.setPixel(0, 0, 1);
    j.setPixel(0, 1, 2);
    j.setPixel(1, 0, 3);
    j.setPixel(1, 1, 4);
    assertTrue(i.equals(j));
    j.setPixel(0, 0, 5);
    assertFalse(i.equals(j));
    i.setPixel(0, 0, 5);
    assertTrue(i.equals(j));
    j = new TileImage(4, 1);
    j.setPixel(0, 0, 5);
    j.setPixel(1, 0, 2);
    j.setPixel(2, 0, 3);
    j.setPixel(3, 0, 4);
    assertEquals(262169, j.hashCode());
    assertFalse(i.equals(j));
    j = new TileImage(1, 4);
    j.setPixel(0, 0, 5);
    j.setPixel(0, 1, 2);
    j.setPixel(0, 2, 3);
    j.setPixel(0, 3, 4);
    assertEquals(65564, j.hashCode());
    assertFalse(i.equals(j));
  }

  public void test2x2() {
    final TileImage i = new TileImage(2, 2);
    i.setPixel(0, 0, 1);
    i.setPixel(0, 1, 2);
    i.setPixel(1, 0, 3);
    i.setPixel(1, 1, 4);
    assertEquals(1, i.getPixel(0, 0));
    assertEquals(2, i.getPixel(0, 1));
    assertEquals(3, i.getPixel(1, 0));
    assertEquals(4, i.getPixel(1, 1));
    final TileImage v = i.vFlip();
    assertEquals(1, i.getPixel(0, 0));
    assertEquals(2, i.getPixel(0, 1));
    assertEquals(3, i.getPixel(1, 0));
    assertEquals(4, i.getPixel(1, 1));
    assertEquals(2, v.getPixel(0, 0));
    assertEquals(1, v.getPixel(0, 1));
    assertEquals(4, v.getPixel(1, 0));
    assertEquals(3, v.getPixel(1, 1));
    final TileImage h = i.hFlip();
    assertEquals(3, h.getPixel(0, 0));
    assertEquals(4, h.getPixel(0, 1));
    assertEquals(1, h.getPixel(1, 0));
    assertEquals(2, h.getPixel(1, 1));
    final TileImage n = i.rotate90();
    assertEquals(2, n.getPixel(0, 0));
    assertEquals(4, n.getPixel(0, 1));
    assertEquals(1, n.getPixel(1, 0));
    assertEquals(3, n.getPixel(1, 1));
    final TileImage r = i.rotate180();
    assertEquals(4, r.getPixel(0, 0));
    assertEquals(3, r.getPixel(0, 1));
    assertEquals(2, r.getPixel(1, 0));
    assertEquals(1, r.getPixel(1, 1));
    final TileImage m = i.rotate270();
    assertEquals(3, m.getPixel(0, 0));
    assertEquals(1, m.getPixel(0, 1));
    assertEquals(4, m.getPixel(1, 0));
    assertEquals(2, m.getPixel(1, 1));
    assertTrue(i == i.getSubimage(0, 0, 2, 2));
    final TileImage s1 = i.getSubimage(0, 0, 1, 1);
    assertEquals(1, s1.getWidth());
    assertEquals(1, s1.getHeight());
    assertEquals(1, s1.getPixel(0, 0));
    final TileImage s2 = i.getSubimage(0, 1, 1, 1);
    assertEquals(1, s2.getWidth());
    assertEquals(1, s2.getHeight());
    assertEquals(2, s2.getPixel(0, 0));
    final TileImage s3 = i.getSubimage(1, 1, 1, 1);
    assertEquals(1, s3.getWidth());
    assertEquals(1, s3.getHeight());
    assertEquals(4, s3.getPixel(0, 0));
    final TileImage s4 = i.getSubimage(1, 0, 1, 1);
    assertEquals(1, s4.getWidth());
    assertEquals(1, s4.getHeight());
    assertEquals(3, s4.getPixel(0, 0));
    final BufferedImage b = i.toBufferedImage();
    assertEquals(2, b.getWidth());
    assertEquals(2, b.getHeight());
    assertEquals(BufferedImage.TYPE_INT_ARGB, b.getType());
  }

  public void testBadSub() {
    final TileImage i = new TileImage(5, 3);
    try {
      i.getSubimage(-1, 0, 2, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(0, -1, 2, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(0, 0, 0, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(0, 0, 2, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(2, 3, 6, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(2, 3, 5, 3);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(1, 1, 2, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(1, 1, 0, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(1, 1, 5, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
    try {
      i.getSubimage(1, 1, 4, 3);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bounds", e.getMessage());
    }
  }

  public void testGetSubimage() {
    final TileImage i = new TileImage(5, 3);
    for (int y = 0, c = 0; y < 3; ++y) {
      for (int x = 0; x < 5; ++x) {
        i.setPixel(x, y, c++);
      }
    }
    final TileImage s = i.getSubimage(3, 0, 2, 3);
    assertEquals(3, s.getPixel(0, 0));
    assertEquals(4, s.getPixel(1, 0));
    assertEquals(8, s.getPixel(0, 1));
    assertEquals(9, s.getPixel(1, 1));
    assertEquals(13, s.getPixel(0, 2));
    assertEquals(14, s.getPixel(1, 2));
  }

  public void test2x3rotate() {
    final TileImage i = new TileImage(3, 2);
    i.setPixel(0, 0, 1);
    i.setPixel(1, 0, 2);
    i.setPixel(2, 0, 3);
    i.setPixel(0, 1, 4);
    i.setPixel(1, 1, 5);
    i.setPixel(2, 1, 6);
    final TileImage n = i.rotate90();
    assertEquals(4, n.getPixel(0, 0));
    assertEquals(1, n.getPixel(1, 0));
    assertEquals(5, n.getPixel(0, 1));
    assertEquals(2, n.getPixel(1, 1));
    assertEquals(6, n.getPixel(0, 2));
    assertEquals(3, n.getPixel(1, 2));
    final TileImage m = i.rotate270();
    assertEquals(3, m.getPixel(0, 0));
    assertEquals(6, m.getPixel(1, 0));
    assertEquals(2, m.getPixel(0, 1));
    assertEquals(5, m.getPixel(1, 1));
    assertEquals(1, m.getPixel(0, 2));
    assertEquals(4, m.getPixel(1, 2));
  }

  public void testSpecialAnglesRotate() {
    final TileImage i = new TileImage(5, 5);
    i.fill(0xCCCCCCCC);
    i.setPixel(0, 0, 0);
    assertTrue(i == i.rotate(0, 0, false));
    assertTrue(i == i.rotate(0.00001, 0, false));
    assertTrue(i == i.rotate(-0.00001, 0, false));
    assertTrue(i == i.rotate(360, 0, false));
    assertTrue(i == i.rotate(720, 0, false));
    assertTrue(i == i.rotate(-360.000001, 0, false));
    assertTrue(i == i.rotate(0, 0, true));
    assertTrue(i == i.rotate(0.00001, 0, true));
    assertTrue(i == i.rotate(-0.00001, 0, true));
    assertTrue(i == i.rotate(360, 0, true));
    assertTrue(i == i.rotate(-360.000001, 0, true));
    assertEquals(i.rotate90(), i.rotate(90, 0, true));
    assertEquals(i.rotate90(), i.rotate(90.000001, 0, true));
    assertEquals(i.rotate90(), i.rotate(90.000001, 0, false));
    assertEquals(i.rotate90(), i.rotate(-270, 0, false));
    assertEquals(i.rotate180(), i.rotate(180.000001, 0, true));
    assertEquals(i.rotate180(), i.rotate(179.999999, 0, false));
    assertEquals(i.rotate180(), i.rotate(-180, 0, false));
    assertEquals(i.rotate270(), i.rotate(270.000001, 0, true));
    assertEquals(i.rotate270(), i.rotate(269.999999, 0, false));
    assertEquals(i.rotate270(), i.rotate(-90, 0, false));
  }

  public void testRotateSolid() {
    final TileImage i = new TileImage(5, 5);
    i.fill(0xCCCCCCCC);
    assertEquals(1431328001, i.rotate(45, 0, false).hashCode());
    assertEquals(859190113, i.rotate(30, 0, false).hashCode());
    assertEquals(825504609, i.rotate(30, 0xFF0000, false).hashCode());
    assertEquals(438245939, i.rotate(30, 0, true).hashCode());
    assertEquals(572990027, i.rotate(30, 0xDCDCDCDC, true).hashCode());
  }

  public void testRotateLargeImageSmallAngle() {
    final TileImage i = new TileImage(500, 500);
    i.fill(0xCCCCCCCC);
    assertEquals(33030900, i.hashCode());
    assertEquals(1737564636, i.rotate(0.8, 0, false).hashCode());
    assertEquals(1738216324, i.rotate(89.9, 0, false).hashCode());
    assertEquals(33073972, i.rotate(90.1, 0, false).hashCode());
  }

  public void testRandomRotatePalettePreserving() {
    final Random r = new Random();
    for (int i = 0; i < 50; ++i) {
      final int w = 1 + r.nextInt(150);
      final int h = 1 + r.nextInt(150);
      final TileImage z = new TileImage(w, h);
      for (int j = w * h / 2; j > 0; --j) {
        z.setPixel(r.nextInt(w), r.nextInt(h), ~0);
      }
      final TileImage u = z.rotate(1000 * r.nextDouble(), 0, false);
      for (int x = 0; x < u.getWidth(); ++x) {
        for (int y = 0; y < u.getHeight(); ++y) {
          final int p = u.getPixel(x, y);
          assertTrue(p == 0 || p == ~0);
        }
      }
    }
    for (int i = 0; i < 50; ++i) {
      final int w = 1 + r.nextInt(150);
      final int h = 1 + r.nextInt(150);
      final TileImage z = new TileImage(w, h);
      for (int j = w * h / 2; j > 0; --j) {
        z.setPixel(r.nextInt(w), r.nextInt(h), ~0);
      }
      final TileImage u = z.rotate(1000 * r.nextDouble(), 0, true);
      for (int x = 0; x < u.getWidth(); ++x) {
        for (int y = 0; y < u.getHeight(); ++y) {
          // check for grey pixel
          final int p = u.getPixel(x, y);
          final int q = p & 0xFF;
          assertEquals(q, (p >>> 8) & 0xFF);
          assertEquals(q, (p >>> 16) & 0xFF);
          assertEquals(q, (p >>> 24) & 0xFF);
        }
      }
    }
  }

  public void testVroll() {
    final TileImage i = new TileImage(7, 11);
    for (int y = 0; y < 11; ++y) {
      for (int x = 0; x < 7; ++x) {
        i.setPixel(x, y, y);
      }
    }
    assertTrue(i == i.vroll(0));
    assertTrue(i == i.vroll(11));
    assertTrue(i == i.vroll(22));
    assertTrue(i == i.vroll(-11));
    for (int j = -50; j < 50; ++j) {
      final TileImage k = i.vroll(j);
      for (int y = 0; y < 11; ++y) {
        for (int x = 0; x < 7; ++x) {
          assertEquals(String.valueOf(j), (y + j + 55) % 11, k.getPixel(x, y));
        }
      }
    }
  }

  public void testHroll() {
    final TileImage i = new TileImage(11, 7);
    for (int y = 0; y < 7; ++y) {
      for (int x = 0; x < 11; ++x) {
        i.setPixel(x, y, (x + y + 2) % 11);
      }
    }
    assertTrue(i == i.hroll(0));
    assertTrue(i == i.hroll(11));
    assertTrue(i == i.hroll(22));
    assertTrue(i == i.hroll(-11));
    for (int j = -50; j < 50; ++j) {
      final TileImage k = i.hroll(j);
      for (int y = 0; y < 7; ++y) {
        for (int x = 0; x < 11; ++x) {
          assertEquals(String.valueOf(j), (x + y + j + 57) % 11, k.getPixel(x, y));
        }
      }
    }
  }

  public void testCopy() {
    final TileImage i = new TileImage(37, 11);
    i.fill(0xDEADBEEF);
    final TileImage c = i.copy();
    assertEquals(i, c);
    c.setPixel(12, 7, 0);
    assertFalse(i.equals(c));
    c.setPixel(12, 7, 0xDEADBEEF);
    assertEquals(i, c);
  }

  public void testViaRaster() {
    final BufferedImage i = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    final WritableRaster r = i.getRaster();
    r.setDataElements(0, 0, new int[] {0xDEADBEEF});
    r.setDataElements(0, 1, new int[] {0xCCCCCCCC});
    r.setDataElements(1, 0, new int[] {0xF0F0F0F0});
    r.setDataElements(1, 1, new int[] {0x0F0F0F0F});
    final TileImage q = new TileImage(i);
    assertEquals(0xDEADBEEF, q.getPixel(0, 0));
    assertEquals(0xCCCCCCCC, q.getPixel(0, 1));
    assertEquals(0xF0F0F0F0, q.getPixel(1, 0));
    assertEquals(0x0F0F0F0F, q.getPixel(1, 1));
  }

  public void testBadJam() {
    try {
      new TileImage(4, 4).jam(1, 1, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new TileImage(4, 4).jam(-1, 1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).jam(1, -1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).jam(4, 1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).jam(1, 4, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).jam(2, 2, new TileImage(3, 3));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
  }

  public void testBadOver() {
    try {
      new TileImage(4, 4).over(1, 1, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new TileImage(4, 4).over(-1, 1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).over(1, -1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).over(4, 1, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).over(1, 4, new TileImage(1, 1));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
    try {
      new TileImage(4, 4).over(2, 2, new TileImage(3, 3));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad rectangle", e.getMessage());
    }
  }

  public void testJam() {
    final TileImage i = new TileImage(5, 5);
    i.fill(0xDEADBEEF);
    final TileImage j = new TileImage(2, 2);
    j.fill(0x12345678);
    final TileImage k = i.jam(1, 2, j);
    assertEquals(i, k);
    assertEquals(0x12345678, k.getPixel(1, 2));
    assertEquals(0x12345678, k.getPixel(1, 3));
    assertEquals(0x12345678, k.getPixel(2, 2));
    assertEquals(0x12345678, k.getPixel(2, 3));
    k.setPixel(1, 2, 0xDEADBEEF);
    k.setPixel(1, 3, 0xDEADBEEF);
    k.setPixel(2, 2, 0xDEADBEEF);
    k.setPixel(2, 3, 0xDEADBEEF);
    for (int y = 0; y < k.getHeight(); ++y) {
      for (int x = 0; x < k.getWidth(); ++x) {
        assertEquals(0xDEADBEEF, k.getPixel(x, y));
      }
    }
  }

  public void testReplaceColor() {
    final TileImage i = new TileImage(4, 4);
    i.fill(~0);
    final TileImage j = i.replaceColor(~0, ~0);
    assertEquals(i, j);
    for (int y = 0; y < j.getHeight(); ++y) {
      for (int x = 0; x < j.getWidth(); ++x) {
        assertEquals(~0, j.getPixel(x, y));
      }
    }
    i.setPixel(3, 3, 0xABCDEF);
    final TileImage k = i.replaceColor(~0, 0x65);
    for (int y = 0; y < k.getHeight(); ++y) {
      for (int x = 0; x < k.getWidth(); ++x) {
        if (x == 3 && y == 3) {
          assertEquals(0xABCDEF, k.getPixel(x, y));
        } else {
          assertEquals(0x65, k.getPixel(x, y));
        }
      }
    }
  }

  public void testTextureColor() {
    final TileImage i = new TileImage(200, 200);
    try {
      i.textureColor(0, 9);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bits", e.getMessage());
    }
    try {
      i.textureColor(0, -1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("bad bits", e.getMessage());
    }
    i.fill(0xDEADBEEF);
    final TileImage j = i.textureColor(0xDEADBEEF, 4);
    final int[] residualsR = new int[16];
    final int[] residualsG = new int[16];
    final int[] residualsB = new int[16];
    for (int y = 0; y < j.getHeight(); ++y) {
      for (int x = 0; x < j.getWidth(); ++x) {
        int p = j.getPixel(x, y);
        assertEquals(0xE0, p & 0xF0);
        residualsB[p & 0xF]++;
        p >>>= 8;
        assertEquals(0xB0, p & 0xF0);
        residualsG[p & 0xF]++;
        p >>>= 8;
        assertEquals(0xA0, p & 0xF0);
        residualsR[p & 0xF]++;
        p >>>= 8;
        assertEquals(0xDE, p);
      }
    }
    for (int k = 0; k < residualsR.length; ++k) {
      assertTrue(residualsR[k] != 0);
      assertTrue(residualsG[k] != 0);
      assertTrue(residualsB[k] != 0);
    }
  }

  public void testTextureColor2() {
    final TileImage i = new TileImage(20, 20);
    i.fill(0xDEADBEEF);
    final TileImage j = i.textureColor(0xDEADBEEF, 1);
    final int[] residualsR = new int[2];
    final int[] residualsG = new int[2];
    final int[] residualsB = new int[2];
    for (int y = 0; y < j.getHeight(); ++y) {
      for (int x = 0; x < j.getWidth(); ++x) {
        int p = j.getPixel(x, y);
        assertEquals(0xEE, p & 0xFE);
        residualsB[p & 0x1]++;
        p >>>= 8;
        assertEquals(0xBE, p & 0xFE);
        residualsG[p & 0x1]++;
        p >>>= 8;
        assertEquals(0xAC, p & 0xFE);
        residualsR[p & 0x1]++;
        p >>>= 8;
        assertEquals(0xDE, p);
      }
    }
    for (int k = 0; k < residualsR.length; ++k) {
      assertTrue(residualsR[k] != 0);
      assertTrue(residualsG[k] != 0);
      assertTrue(residualsB[k] != 0);
    }
  }

  public void testTextureColor3() {
    final int[] c = new int[4];
    for (int k = 0; k < 50; ++k) {
      final TileImage i = new TileImage(2, 2);
      i.fill(0xDEADBEEF);
      final TileImage j = i.textureColor(0xDEADBEEF, 2);
      if (j.getPixel(0, 0) != 0xDEADBEEF) {
        c[0]++;
      }
      if (j.getPixel(0, 1) != 0xDEADBEEF) {
        c[1]++;
      }
      if (j.getPixel(1, 0) != 0xDEADBEEF) {
        c[2]++;
      }
      if (j.getPixel(1, 1) != 0xDEADBEEF) {
        c[3]++;
      }
    }
    for (final int aC : c) {
      assertTrue(aC != 0);
    }
  }

  public void testOver1() {
    final TileImage i = new TileImage(2, 2);
    i.fill(0xDEADBEEF);
    final TileImage j = new TileImage(2, 2);
    j.fill(0); // completely transparent
    assertEquals(i, i.over(0, 0, j));
    j.fill(0xFFFFFFFF); // completely opaque
    assertEquals(j, i.over(0, 0, j));
  }

  public void testOver2() {
    final TileImage i = new TileImage(2, 2);
    i.fill(0xDEADBEEF);
    final TileImage k = new TileImage(1, 1);
    k.setPixel(0, 0, 0xFF123456);
    i.over(1, 1, k);
    assertEquals(0xFF123456, i.getPixel(1, 1));
    assertEquals(0xDEADBEEF, i.getPixel(1, 0));
    assertEquals(0xDEADBEEF, i.getPixel(0, 1));
    assertEquals(0xDEADBEEF, i.getPixel(0, 0));
    k.setPixel(0, 0, 0x80000000); // 50% opaque
    i.setPixel(1, 0, 0);
    i.over(1, 0, k);
    assertEquals(0x40000000, i.getPixel(1, 0));
    i.setPixel(1, 0, 0xFFFFFFFF);
    assertNotNull(i.over(1, 0, k));
    assertEquals(0xBF7F7F7F, i.getPixel(1, 0));
    i.setPixel(1, 0, 0xDEADBEEF);
    assertNotNull(i.over(1, 0, k));
    assertEquals(0xAF565F77, i.getPixel(1, 0));
    k.setPixel(0, 0, 0x80445566); // 50% opaque
    i.setPixel(0, 1, 0);
    i.over(0, 1, k);
    assertEquals(0x40222A33, i.getPixel(0, 1));
    i.setPixel(0, 1, 0xFFFFFFFF);
    assertNotNull(i.over(0, 1, k));
    assertEquals(0xBFA1AAB2, i.getPixel(0, 1));
    i.setPixel(0, 1, 0xDEADBEEF);
    assertNotNull(i.over(0, 1, k));
    //    System.err.println(Integer.toHexString(i.getPixel(0, 1)));
    assertEquals(0xAF7889AA, i.getPixel(0, 1));

  }
}

