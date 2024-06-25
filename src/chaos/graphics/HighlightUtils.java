package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Utility functions for highlighting.
 * @author Sean A. Irvine
 */
final class HighlightUtils {

  private HighlightUtils() {
  }

  private static final Color DARK_RED = new Color(0x7F0000);

  // Yellow and red box highlighting
  static void highlight(final Graphics g, final int x, final int y, final int w, final int h) {
    if (g != null) {
      final int xx = x + w - 1;
      final int yy = y + h - 1;
      g.setColor(Color.YELLOW);
      g.drawLine(x, y, xx, y);
      g.drawLine(x, yy, xx, yy);
      g.drawLine(x, y, x, yy);
      g.drawLine(xx, y, xx, yy);
      g.setColor(Color.RED);
      for (int i = 0; i < w; i += 8) {
        g.drawLine(x + i, y, x + i + 3, y);
        g.drawLine(xx - i, yy, xx - i - 3, yy);
      }
      for (int i = 0; i < h; i += 8) {
        g.drawLine(x, yy - i, x, yy - i - 3);
        g.drawLine(xx, y + i, xx, y + i + 3);
      }
    }
  }

  static void lightHighlight(final Graphics g, final int x, final int y, final int w) {
    if (g != null) {
      final int xx = x + w - 1;
      final int yy = y + w - 1;
      g.setColor(DARK_RED);
      g.drawLine(x, y, xx, y);
      g.drawLine(x, yy, xx, yy);
      g.drawLine(x, y, x, yy);
      g.drawLine(xx, y, xx, yy);
    }
  }
}
