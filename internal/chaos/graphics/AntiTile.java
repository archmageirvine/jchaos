package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

/**
 * Place a large "X" over an existing tile.
 *
 * @author Sean A. Irvine
 */
final class AntiTile {

  private AntiTile() { }

  private static Polygon[] ex(final int w, final int b) {
    final int[] x1 = {0, w - b, w, b};
    final int[] y1 = {0, w, w, 0};
    final int[] x2 = {w, w - b, 0, b};
    final int[] y2 = {0, 0, w, w};
    return new Polygon[] {new Polygon(x1, y1, 4), new Polygon(x2, y2, 4)};
  }

  static BufferedImage antiTile(final BufferedImage image) {
    final int w = image.getWidth();
    final int wb = w / 8;
    final Graphics2D g = image.createGraphics();
    try {
      g.translate(wb - 1, wb - 1);
      g.setColor(Color.RED);
      final Polygon[] ex = ex(w - 2 * wb, wb);
      for (final Polygon s : ex) {
        g.fill(s);
      }
      g.setColor(Color.RED.darker());
      g.translate(wb / 2, 1);
      for (final Polygon s : ex) {
        g.fill(s);
      }
    } finally {
      g.dispose();
    }
    return image;
  }
}
