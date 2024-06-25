package irvine.util.graphics;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class BufferedImageUtilsTest extends TestCase {

  public void testDarken() {
    final BufferedImage i = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Random r = new Random();
    final int[] px = new int[256];
    long sumr = 0;
    long sumg = 0;
    long sumb = 0;
    for (int k = 0; k < px.length; ++k) {
      final int p = r.nextInt(0xFFFFFF);
      px[k] = 0xFF000000 | p;
      sumr += p & 0xFF0000;
      sumg += p & 0xFF00;
      sumb += p & 0xFF;
    }
    i.setRGB(0, 0, 16, 16, px, 0, 16);
    final BufferedImage j = BufferedImageUtils.darken(i);
    assertEquals(16, j.getWidth());
    assertEquals(16, j.getHeight());
    final int[] jx = j.getRGB(0, 0, 16, 16, null, 0, 16);
    long jsumr = 0;
    long jsumg = 0;
    long jsumb = 0;
    for (final int aJx : jx) {
      assertEquals(0xFF000000, aJx & 0xFF000000);
      jsumr += aJx & 0xFF0000;
      jsumg += aJx & 0xFF00;
      jsumb += aJx & 0xFF;
    }
    sumr /= 2;
    sumg /= 2;
    sumb /= 2;
    assertTrue(Math.abs(sumr - jsumr) < (256 << 16));
    assertTrue(Math.abs(sumg - jsumg) < (256 << 8));
    assertTrue(Math.abs(sumb - jsumb) < 256);
  }

  public void testGrayScale() {
    final BufferedImage i = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Random r = new Random();
    final int[] px = new int[256];
    for (int k = 0; k < px.length; ++k) {
      final int p = r.nextInt(0xFFFFFF);
      px[k] = 0xFF000000 | p;
    }
    i.setRGB(0, 0, 16, 16, px, 0, 16);
    final BufferedImage j = BufferedImageUtils.grayScale(i);
    assertEquals(16, j.getWidth());
    assertEquals(16, j.getHeight());
    final int[] jx = j.getRGB(0, 0, 16, 16, null, 0, 16);
    for (final int aJx : jx) {
      assertEquals(0xFF000000, aJx & 0xFF000000);
      final int red = (aJx >>> 16) & 0xFF;
      assertEquals(red, (aJx >>> 8) & 0xFF);
      assertEquals(red, aJx & 0xFF);
    }
  }

  public void testWriteEps() throws IOException {
    final BufferedImage i = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    final Random r = new Random();
    final int[] px = new int[256];
    for (int k = 0; k < px.length; ++k) {
      final int p = r.nextInt(0xFFFFFF);
      px[k] = 0xFF000000 | p;
    }
    try (final ByteArrayOutputStream os = new ByteArrayOutputStream();
         final PrintStream out = new PrintStream(os)) {
      BufferedImageUtils.writeEps(i, out, true);
      final String s = os.toString();
      //System.out.println(s);
      TestUtils.containsAll(s,
        "%%Creator: irvine.util.graphics.BufferedImageUtils",
        "%%Title: image",
        "%%Pages: 0",
        "%%BoundingBox: 0.0 0.0 160.0 160.0",
        "%%EndComments",
        "gsave",
        "10 10 scale",
        ".05 setlinewidth",
        "0.0 0.0 0.0 setrgbcolor",
        "closepath fill",
        ".5 .5 .5 setrgbcolor",
        "newpath 1 0 moveto 1 16 lineto stroke",
        "newpath 0 16 moveto 16 16 lineto stroke",
        "grestore",
        "%%Trailer",
        "%%EOF");
    }
  }
}
