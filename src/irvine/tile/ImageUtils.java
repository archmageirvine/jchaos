package irvine.tile;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import irvine.util.io.IOUtils;
import irvine.util.string.PostScript;

/**
 * Various utility functions associated with images.
 * @author Sean A. Irvine
 */
public final class ImageUtils {

  private ImageUtils() { }

  /**
   * Write an Encapsulated PostScript (EPS) version of the given image to the
   * output stream.  The image is expanded and optionally gridded.  The purpose is for
   * the display of small icon or tile like images, so that the individual
   * pixel detail can be seen.
   *
   * @param image image to write
   * @param out stream to write to
   * @param grid whether or not to write a grid on top of the image
   * @exception NullPointerException if <code>image</code> or <code>out</code> is null
   */
  public static void writeEPS(final TileImage image, final PrintStream out, final boolean grid) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    PostScript.header(out, ImageUtils.class, "image", 10 * w, 10 * h);
    out.println("10 10 scale");
    out.println(".05 setlinewidth");

    // draw all the cells
    for (int x = 0; x < w; ++x) {
      for (int y = 0; y < h; ++y) {
        final int c = image.getPixel(x, y);
        out.println(((c >> 16) & 0xFF) / 256.0 + " " + ((c >> 8) & 0xFF) / 256.0 + " " + (c & 0xFF) / 256.0 + " setrgbcolor");
        out.println("newpath");
        out.println(x + " " + (h - y - 1) + " moveto");
        out.println((x + 1) + " " + (h - y - 1) + " lineto");
        out.println((x + 1) + " " + (h - y) + " lineto");
        out.println(x + " " + (h - y) + " lineto");
        out.println("closepath fill");
      }
    }

    if (grid) {
      // overlay grid
      out.println(".5 .5 .5 setrgbcolor");
      for (int x = 0; x <= w; ++x) {
        for (int y = 0; y <= h; ++y) {
          out.println("newpath " + x + " 0 moveto " + x + " " + h + " lineto stroke");
          out.println("newpath 0 " + y + " moveto " + w + " " + y + " lineto stroke");
        }
      }
    }
    PostScript.trailer(out);
    out.flush();
  }

  /** Write an integer value to the output stream as ASCII digits. */
  private static void writeIntAsString(final OutputStream os, final int v) throws IOException {
    final String w = String.valueOf(v);
    for (int i = 0; i < w.length(); ++i) {
      os.write(w.charAt(i));
    }
  }

  /**
   * Write the given image to the specified stream as a portable pixmap image
   * (ppm).  Alpha channel information is not retained.
   *
   * @param image image to write
   * @param os stream to write to
   * @exception IOException if an I/O error occurs
   * @exception NullPointerException if either parameter is null
   */
  public static void writePPM(final TileImage image, final OutputStream os) throws IOException {
    os.write('P');
    os.write('6');
    os.write(' ');
    final int w = image.getWidth();
    writeIntAsString(os, w);
    os.write(' ');
    final int h = image.getHeight();
    writeIntAsString(os, h);
    os.write(' ');
    os.write('2');
    os.write('5');
    os.write('5');
    os.write('\n');
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        final int p = image.getPixel(x, y);
        os.write((p >> 16) & 0xFF);
        os.write((p >> 8) & 0xFF);
        os.write(p & 0xFF);
      }
    }
    os.flush();
  }

  /**
   * Read a PPM file into the Image format.
   *
   * @param is stream to read from
   * @return image
   * @exception IOException if an I/O error occurs
   * @exception NullPointerException if either parameter is null
   */
  public static TileImage readPPM(final InputStream is) throws IOException {
    if (is.read() != 'P' || is.read() != '6') {
      throw new IOException("not ppm 6");
    }
    int c;
    while (Character.isWhitespace(c = is.read())) {
      // do nothing
    }
    int v = c - '0';
    while (Character.isDigit(c = is.read())) {
      v *= 10;
      v += c - '0';
    }
    final int w = v;
    while (Character.isWhitespace(c = is.read())) {
      // do nothing
    }
    v = c - '0';
    while (Character.isDigit(c = is.read())) {
      v *= 10;
      v += c - '0';
    }
    final int h = v;
    while (Character.isWhitespace(is.read())) {
      // do nothing
    }
    // we ignore the max color value
    while (Character.isDigit(c = is.read())) {
      // do nothing
    }
    if (c == -1) {
      throw new IOException("not ppm 6");
    }
    // we have read the single whitespace, pixel data follows
    final TileImage i = new TileImage(w, h);
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        final int r = is.read();
        final int g = is.read();
        final int b = is.read();
        i.setPixel(x, y, (r << 16) | (g << 8) | b);
      }
    }
    return i;
  }

  /** Reciprocal of the golden ratio. */
  private static final double RECIPROCAL_PHI = 1 / 1.61803399;
  /** Font to use. */
  private static final String LOGICAL_FONT_NAME = "SansSerif";
  /** Cache of certain small glyphs. */
  private static final HashMap<String, TileImage> GLYPHS = new HashMap<>();

  /** Get character image for c at specified point size. */
  private static TileImage getSmallGlyph(final char c, final int size) {
    final String s = c + "_" + size;
    TileImage r = GLYPHS.get(s);
    if (r == null) {
      try {
        try (final BufferedReader reader = IOUtils.reader("irvine/tile/" + size + "pt.txt")) {
          for (char u = '0'; u <= '9'; ++u) {
            TileImage im = null;
            for (int y = 0; y < size; ++y) {
              final String row = reader.readLine();
              if (im == null) {
                im = new TileImage(row.length(), size);
              }
              for (int x = 0; x < im.getWidth(); ++x) {
                im.setPixel(x, y, row.charAt(x) == '#' ? 0xFF000000 : 0xFFFFFF);
              }
            }
            reader.readLine(); // skip separator line
            GLYPHS.put(u + "_" + size, im);
            if (u == c) {
              r = im;
            }
          }
        }
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
    return r;
  }

  /**
   * Get a black and white image for the given character at the given size.
   * The resulting image will have height size and a width determined by
   * size / phi, where phi is the golden ratio.  The pixels of the background
   * are white and of the character are black.<p>
   *
   * Can get glyphs for all characters of point-size 8 and above and digits
   * for point-size 6 and above.
   *
   * @param c character to draw
   * @param size height of image in pixels
   * @return black and white image of character
   * @exception IllegalArgumentException if no glyph is available for the
   * given character at the given point size.
   */
  public static TileImage getCharImage(final char c, final int size) {
    if (size < 8) {
      if (size > 5) {
        final TileImage r = getSmallGlyph(c, size);
        if (r != null) {
          return r;
        }
      }
      throw new IllegalArgumentException();
    }
    final int w = (int) (size * RECIPROCAL_PHI + 0.5);
    final BufferedImage im = new BufferedImage(w, size, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g = im.createGraphics();
    try {
      g.setPaintMode();
      g.setColor(new Color(0xFFFFFFFF, true));
      g.fillRect(0, 0, w, size);
      g.setColor(new Color(0xFF000000, true));
      g.setFont(new Font(LOGICAL_FONT_NAME, Font.PLAIN, Math.max(9, size)));
      g.drawString(String.valueOf(c), 0, size - (size >> 3));
    } finally {
      g.dispose();
    }
    return new TileImage(im);
  }

  /** Pixel size of Spectrum font. */
  private static final int ZX_SIZE = 8;

  /**
   * Get a black and white image for the given character from the ZX Spectrum
   * character set.  The resulting image will by 8 by 8.
   *
   * @param c character to get image for
   * @return black and white image of character
   * @exception IllegalArgumentException if no glyph is available for the
   * given character.
   */
  public static TileImage getZXImage(final char c) {
    if (c < ' ' || c > 128) {
      throw new IllegalArgumentException();
    }
    final String s = c + "_zx";
    TileImage r = GLYPHS.get(s);
    if (r == null) {
      try {
        try (final BufferedReader reader = IOUtils.reader("irvine/tile/zxfont.txt")) {
          for (char u = ' '; u < 128; ++u) {
            final TileImage im = new TileImage(ZX_SIZE, ZX_SIZE);
            for (int y = 0; y < ZX_SIZE; ++y) {
              final String row = reader.readLine();
              for (int x = 0; x < ZX_SIZE; ++x) {
                im.setPixel(x, y, row.charAt(x) == '#' ? 0xFF000000 : 0xFFFFFF);
              }
            }
            reader.readLine(); // skip separator line
            GLYPHS.put(u + "_zx", im);
            if (u == c) {
              r = im;
            }
          }
        }
      } catch (final IOException e) {
        throw new RuntimeException(e);
      }
    }
    return r;
  }

}
