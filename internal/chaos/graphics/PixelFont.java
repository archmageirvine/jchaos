package chaos.graphics;

import java.awt.image.BufferedImage;

/**
 * A five-by-five pixel font.
 *
 * @author Sean A. Irvine
 */
final class PixelFont {

  private PixelFont() { }

  private static final int SPACE_BETWEEN_GLYPHS = 1;
  private static final int SPACE_WIDTH = 3;
  private static final int HEIGHT = 5;

  private static final String[] GLYPHS = {
    " ** " + "*  *" + "****" + "*  *" + "*  *",      // A
    "*** " + "*  *" + "*** " + "*  *" + "*** ",      // B
    " ***" + "*   " + "*   " + "*   " + " ***",      // C
    "*** " + "*  *" + "*  *" + "*  *" + "*** ",      // D
    "***" + "*  " + "***" + "*  " + "***",           // E
    "***" + "*  " + "***" + "*  " + "*  ",           // F
    " ***" + "*   " + "* **" + "*  *" + " ***",      // G
    "*  *" + "*  *" + "****" + "*  *" + "*  *",      // H
    "*" + "*" + "*" + "*" + "*",                     // I
    "   *" + "   *" + "   *" + "*  *" + " ** ",      // J
    "*  *" + "* * " + "**  " + "* * " + "*  *",      // K
    "*  " + "*  " + "*  " + "*  " + "***",           // L
    "*   *" + "** **" + "* * *" + "*   *" + "*   *", // M
    "*  *" + "** *" + "* **" + "*  *" + "*  *",      // N
    " ** " + "*  *" + "*  *" + "*  *" + " ** ",      // O
    "*** " + "*  *" + "*** " + "*   " + "*   ",      // P
    " ** " + "*  *" + "*  *" + "* * " + " * *",      // Q
    "*** " + "*  *" + "*** " + "* * " + "*  *",      // R
    " ***" + "*   " + " ** " + "   *" + "*** ",      // S
    "*****" + "  *  " + "  *  " + "  *  " + "  *  ", // T
    "*  *" + "*  *" + "*  *" + "*  *" + " ** ",      // U
    "*   *" + "*   *" + " * * " + " * * " + "  *  ", // V
    "* * *" + "* * *" + "* * *" + " * * " + " * * ", // W
    "*  *" + "*  *" + " ** " + "*  *" + "*  *",      // X
    "*  *" + "*  *" + "****" + "   *" + "*** ",      // Y
    "****" + "   *" + " ** " + "*   " + "****",      // Z
  };

  static int width(final char c) {
    if (c >= 'A' && c <= 'Z') {
      return GLYPHS[c - 'A'].length() / HEIGHT;
    }
    return 0;
  }

  static int width(final String str) {
    if (str == null || str.isEmpty()) {
      return 0;
    }
    int s = SPACE_BETWEEN_GLYPHS * (str.length() - 1);
    for (int k = 0; k < str.length(); ++k) {
      final char c = str.charAt(k);
      if (c == ' ') {
        s += SPACE_WIDTH;
      } else {
        s += width(c);
      }
    }
    return s;
  }

  static void drawString(final BufferedImage image, final int color, final String str, final int x, final int y) {
    int mx = x;
    for (int k = 0; k < str.length(); ++k) {
      final char c = str.charAt(k);
      if (c >= 'A' && c <= 'Z') {
        final String glyph = GLYPHS[c - 'A'];
        final int w = glyph.length() / HEIGHT;
        for (int dx = 0; dx < w; ++dx) {
          for (int dy = 0; dy < HEIGHT; ++dy) {
            if (glyph.charAt(dy * w + dx) == '*') {
              image.setRGB(mx + dx, y + dy, color);
            }
          }
        }
        mx += w + SPACE_BETWEEN_GLYPHS;
      } else if (c == ' ') {
        mx += SPACE_WIDTH;
      }
    }
  }
}

