package irvine.tile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ImageUtilsTest extends TestCase {

  /** A test image used for demonstration purposes. */
  private static final String RED_DRAGON_DATA =
        "  ##            "
      + "    ##          "
      + "     ##   #     "
      + "    ###   ###   "
      + "  ####    ##### "
      + "    ##   ####   "
      + "    ### ##    # "
      + "   ######     # "
      + "  #   ####    # "
      + " #   # ####    #"
      + "    #   ####   #"
      + "    #   #####  #"
      + "         #### # "
      + "         #### # "
      + "          ### # "
      + "       #######  ";

  /** Get the dragon image in the specified colors. */
  private static TileImage getDragon(final int fg, final int bg) {
    final TileImage i = new TileImage(16, 16);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        i.setPixel(x, y, RED_DRAGON_DATA.charAt(c++) == '#' ? fg : bg);
      }
    }
    return i;
  }

  private String getDateFromLinux() {
    try {
      final Process p = Runtime.getRuntime().exec("date +%Y-%m-%d");
      try {
        try (final BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
          return r.readLine();
        }
      } finally {
        try {
          p.waitFor();
        } catch (final InterruptedException e) {
          // too bad
        }
        p.getInputStream().close();
        p.getOutputStream().close();
        p.getErrorStream().close();
      }
    } catch (final IOException e) {
      return null;
    }
  }

  private static final String LS = System.lineSeparator();

  public void testWriteEPS() throws Exception {
    final TileImage dragon = getDragon(0, 0x123456);
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      try (final PrintStream ps = new PrintStream(bos)) {
        ImageUtils.writeEPS(dragon, ps, false);
      }
    } finally {
      bos.close();
    }
    final String res = bos.toString();
    assertTrue(res.startsWith("%!PS-Adobe-3.0 EPSF-2.0"));
    assertTrue(res.contains("%%BoundingBox: 0.0 0.0 160.0 160.0"));
    assertTrue(res.contains("0.0703125 0.203125 0.3359375 setrgbcolor"));
    assertTrue(res.contains("closepath fill"));
    assertTrue(res.contains("newpath"));
    assertTrue(res.contains("gsave"));
    assertTrue(res.contains("grestore"));
    for (int i = 0; i < 16; ++i) {
      assertTrue(res.contains(i + " 0 moveto"));
      assertTrue(res.contains(i + " 15 moveto"));
      assertTrue(res.contains(i + " 15 lineto"));
      assertTrue(res.contains((i + 1) + " 16 lineto"));
      assertTrue(res.contains((i + 1) + " 0 lineto"));
      assertTrue(res.contains("16 " + i + " lineto"));
    }
    assertTrue(res.contains("newpath" + LS + "0 15 moveto" + LS + "1 15 lineto" + LS + "1 16 lineto" + LS + "0 16 lineto" + LS + "closepath fill"));
    assertTrue(res.contains("15 14 lineto"));
    assertTrue(res.contains("%%Trailer"));
    assertTrue(res.contains("%%Creator: irvine.tile.ImageUtils"));
    assertTrue(res.contains("%%Title: image"));
    assertTrue(res.contains("%%Pages: 0"));
    assertTrue(res.contains("%%LanguageLevel: 2"));
    assertTrue(res.contains("%%Orientation: Portrait"));
    assertTrue(res.contains("%%EndComments"));
    assertTrue(res.contains("10 10 scale"));
    assertTrue(res.contains(".05 setlinewidth"));
    assertTrue(res.contains("closepath fill"));
    assertTrue(res.contains("0.0 0.0 0.0 setrgbcolor"));
    assertTrue(res.contains("15 1 lineto"));
    assertTrue(res.endsWith("%%EOF" + LS));
    final String date = getDateFromLinux();
    if (date != null) {
      assertTrue(res.contains("%%CreationDate: " + date + " "));
    } else {
      assertTrue(res.contains("%%CreationDate: "));
    }
    //    System.err.println(res);
  }

  public void testWriteEPSGrid() throws Exception {
    final TileImage dragon = getDragon(0, ~0);
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      try (final PrintStream ps = new PrintStream(bos)) {
        ImageUtils.writeEPS(dragon, ps, true);
      }
    } finally {
      bos.close();
    }
    final String res = bos.toString();
    assertTrue(res.startsWith("%!PS-Adobe-3.0 EPSF-2.0"));
    assertTrue(res.contains("%%BoundingBox: 0.0 0.0 160.0 160.0"));
    assertTrue(res.contains("0.99609375 0.99609375 0.99609375 setrgbcolor"));
    assertTrue(res.contains("closepath fill"));
    assertTrue(res.contains("newpath"));
    assertTrue(res.contains("gsave"));
    assertTrue(res.contains("grestore"));
    assertTrue(res.contains("16 0 lineto"));
    assertTrue(res.contains("16 1 lineto"));
    assertTrue(res.contains("15 14 lineto"));
    assertTrue(res.contains("%%Trailer"));
    assertTrue(res.contains("%%Creator: irvine.tile.ImageUtils"));
    assertTrue(res.contains("%%Title: image"));
    assertTrue(res.contains("%%Pages: 0"));
    assertTrue(res.contains("%%LanguageLevel: 2"));
    assertTrue(res.contains("%%Orientation: Portrait"));
    assertTrue(res.contains("%%EndComments"));
    assertTrue(res.contains("10 10 scale"));
    assertTrue(res.contains(".05 setlinewidth"));
    assertTrue(res.contains("%%CreationDate: "));
    assertTrue(res.contains("closepath fill"));
    assertTrue(res.contains("0.0 0.0 0.0 setrgbcolor"));
    assertTrue(res.contains("15 0 moveto"));
    assertTrue(res.contains("15 1 lineto"));
    for (int i = 0; i < 16; ++i) {
      assertTrue(res.contains("newpath " + i + " 0 moveto " + i + " 16 lineto stroke"));
      assertTrue(res.contains("newpath 0 " + i + " moveto 16 " + i + " lineto stroke"));
    }
    assertTrue(res.contains(".5 .5 .5 setrgbcolor"));
    assertTrue(res.endsWith("%%EOF" + System.lineSeparator()));
    //    System.err.println(res);
  }

  public void testWriteReadPPM() throws Exception {
    final TileImage dragon = getDragon(0, 0x123456);
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      ImageUtils.writePPM(dragon, bos);
    } finally {
      bos.close();
    }
    final byte[] b = bos.toByteArray();
    int sum = 0;
    for (final byte aB : b) {
      sum += aB & 0xFF;
    }
    assertEquals(26810, sum);
    try (final ByteArrayInputStream bis = new ByteArrayInputStream(b)) {
      final TileImage d = ImageUtils.readPPM(bis);
      assertEquals(dragon, d);
    }
  }

  public void testReadNonPPM() {
    try (final ByteArrayInputStream bis = new ByteArrayInputStream(new byte[0])) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 X".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1X".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 ".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 X".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 2".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 2X".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 2 ".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6 1 2 X".getBytes())) {
      ImageUtils.readPPM(bis);
      fail();
    } catch (final IOException e) {
      assertEquals("not ppm 6", e.getMessage());
    }
  }

  public void testReadPPM() throws Exception {
    try (final ByteArrayInputStream bis = new ByteArrayInputStream("P6   1   1   66\nA".getBytes())) {
      final TileImage i = ImageUtils.readPPM(bis);
      assertEquals(1, i.getWidth());
      assertEquals(1, i.getWidth());
      assertEquals(-1, i.getPixel(0, 0));
    }
  }

  public void testGetCharImageBad() {
    try {
      ImageUtils.getCharImage('A', 7);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      ImageUtils.getCharImage('0', 5);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      ImageUtils.getCharImage(':', 6);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void testGetCharImage() {
    for (int s = 8; s < 32; ++s) {
      final TileImage i = ImageUtils.getCharImage('0', s);
      assertEquals(s, i.getHeight());
      final int w = i.getWidth();
      assertTrue(w < s);
      assertTrue(w > s / 2);
      if (s == 8) {
        assertEquals(5, w);
      }
      boolean sawBlack = false;
      boolean sawWhite = false;
      boolean penultimateRowBlack = false;
      int whiteBorderCount = 0;
      for (int y = 0; y < s; ++y) {
        for (int x = 0; x < w; ++x) {
          final int p = i.getPixel(x, y);
          if (p == ~0 && (y == 0 || x == 0 || y == s - 1 || x == w - 1)) {
            ++whiteBorderCount;
          }
          sawWhite |= p == ~0;
          sawBlack |= p == 0xFF000000;
          if (y == s - 2) {
            penultimateRowBlack |= p == 0xFF000000;
          }
          assertTrue(Integer.toHexString(p), p == 0xFF000000 || p == ~0);
        }
      }
      if (s <= 25) {
        assertTrue(whiteBorderCount > 13 * (s + w - 2) / 10);
      } else {
        final int expected = 2 * (s + w - 2);
        assertEquals(expected, whiteBorderCount);
      }
      assertTrue(sawWhite);
      assertTrue(sawBlack);
      if (s <= 14) {
        assertTrue(penultimateRowBlack);
      } else if (s >= 16) {
        assertFalse(penultimateRowBlack);
      }
    }
  }

  public void testSize8Image2() {
    final TileImage i = ImageUtils.getCharImage('2', 8);
    // should be at most one change from white/black on top line
    int trans = 0;
    for (int x = 0; x < i.getWidth() - 1; ++x) {
      if (i.getPixel(x, 0) == ~0 && i.getPixel(x + 1, 0) == 0xFF000000) {
        ++trans;
      }
    }
    assertTrue(trans <= 1);
  }

  private static final int BG = 0xFFFFFF;
  private static final int FG = 0xFF000000;

  @SuppressWarnings("unchecked")
  public void testSize6Image2() {
    assertNotNull(ImageUtils.getCharImage('0', 6));
    final TileImage i = ImageUtils.getCharImage('2', 6);
    assertEquals(6, i.getHeight());
    assertEquals(5, i.getWidth());
    assertEquals(BG, i.getPixel(0, 0));
    assertEquals(BG, i.getPixel(1, 0));
    assertEquals(FG, i.getPixel(2, 0));
    assertEquals(FG, i.getPixel(3, 0));
    assertEquals(BG, i.getPixel(4, 0));
    assertEquals(BG, i.getPixel(0, 1));
    assertEquals(FG, i.getPixel(1, 1));
    assertEquals(BG, i.getPixel(2, 1));
    assertEquals(BG, i.getPixel(3, 1));
    assertEquals(FG, i.getPixel(4, 1));
    assertEquals(BG, i.getPixel(0, 2));
    assertEquals(BG, i.getPixel(1, 2));
    assertEquals(BG, i.getPixel(2, 2));
    assertEquals(BG, i.getPixel(3, 2));
    assertEquals(FG, i.getPixel(4, 2));
    assertEquals(BG, i.getPixel(0, 3));
    assertEquals(BG, i.getPixel(1, 3));
    assertEquals(BG, i.getPixel(2, 3));
    assertEquals(FG, i.getPixel(3, 3));
    assertEquals(BG, i.getPixel(4, 3));
    assertEquals(BG, i.getPixel(0, 4));
    assertEquals(BG, i.getPixel(1, 4));
    assertEquals(FG, i.getPixel(2, 4));
    assertEquals(BG, i.getPixel(3, 4));
    assertEquals(BG, i.getPixel(4, 4));
    assertEquals(BG, i.getPixel(0, 5));
    assertEquals(FG, i.getPixel(1, 5));
    assertEquals(FG, i.getPixel(2, 5));
    assertEquals(FG, i.getPixel(3, 5));
    assertEquals(FG, i.getPixel(4, 5));
    assertNotNull(ImageUtils.getCharImage('9', 6));
    final Object obj = TestUtils.getField("GLYPHS", ImageUtils.class);
    assertTrue(obj instanceof HashMap);
    assertEquals(ImageUtils.getCharImage('7', 6), ((HashMap<String, TileImage>) obj).get("7_6"));
  }

  public void testZXBad() {
    try {
      ImageUtils.getZXImage((char) 31);
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      ImageUtils.getZXImage((char) 128);
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      ImageUtils.getZXImage((char) 0);
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  @SuppressWarnings("unchecked")
  public void testZXGood() {
    final TileImage space = ImageUtils.getZXImage(' ');
    assertEquals(8, space.getWidth());
    assertEquals(8, space.getHeight());
    assertEquals(524296, space.hashCode());
    assertEquals(-524316, ImageUtils.getZXImage('!').hashCode());
    assertEquals(-524344, ImageUtils.getZXImage('Y').hashCode());
    assertEquals(524336, ImageUtils.getZXImage((char) 127).hashCode());
    final Object obj = TestUtils.getField("GLYPHS", ImageUtils.class);
    assertTrue(obj instanceof HashMap);
    assertEquals(ImageUtils.getZXImage('#'), ((HashMap<String, TileImage>) obj).get("#_zx"));
  }

  public void testNastyReflection() {
    final Object obj = TestUtils.getField("LOGICAL_FONT_NAME", ImageUtils.class);
    assertTrue(obj instanceof String);
    assertEquals("SansSerif", obj);
  }

}

