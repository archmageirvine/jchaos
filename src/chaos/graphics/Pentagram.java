package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import chaos.common.Realm;
import irvine.util.graphics.Stipple;

/**
 * The realm pentagram.
 * @author Sean A. Irvine
 */
final class Pentagram {

  /** Precomputed pentagram image. */
  private final BufferedImage mPentagram;
  /** Coordinates of pentagram points. */
  private final int[] mPX, mPY;

  Pentagram(final int cellWidth) {
    // Precompute the pentagram image, so we can quickly redisplay it
    // Premultiplied angles and lengths for pentagram
    final int l = 2 * cellWidth;
    final int hl = l / 2;
    final int tl = l / 3;
    final int lc = (int) (l * 0.587785252 + 0.5);
    final int ls = (int) (l * 0.809016994 + 0.5);
    // m is the arm length of the inner pentagon
    final int m = (int) (l * 0.181635632 + 0.5); // l * sin 36 * cos 72
    final int hm = (m + 1) / 2;
    final int ms = (int) (m * 0.309016994 + 0.5); // m * sin 18
    final int mc = (int) (m * 0.951056516 + 0.5); // m * cos 18
    final int ds = (int) (hl * 0.309016994 + 0.5); // hl * sin 18
    // Compute the point arrays (used later for decoration)
    // This array includes some corrections for aesthetic reasons
    // and to provide a pixel buffer around the points

    final int f = cellWidth / 4;
    final int t = cellWidth / 8;
    mPX = new int[] {f, f + l, f + l - ls, f + l / 2, f + ls, f + hl - hm - cellWidth / 16, f + hl + hm + ms + t, f + hl - hm - ms - t + 2, f + hl, f + hl};
    mPY = new int[] {tl, tl, tl + lc, tl + lc - (int) (l * 0.891006524 + 0.5), tl + lc, tl, tl + mc + 2, tl + mc + 2, tl + ds, tl + 2 * mc};
    // unused positions (f + hl + hm + 1, tl), (f + hl, tl + (int) (hl * 0.809016994 + 0.5) - 3)
    // Border of at least 4 pixels to enable clearing of any decoration points drawn later
    mPentagram = Stipple.stipple(0, 0, l + cellWidth / 2, l).toBufferedImage();
    final Graphics g = mPentagram.getGraphics();
    if (g != null) {
      g.setColor(Color.CYAN);
      g.drawPolygon(mPX, mPY, 5);
      g.dispose();
    }
  }

  void highlightPentagram(final Graphics g, final Realm realm, final Color color, final int dx, final int dy) {
    if (realm == null) {
      g.drawImage(mPentagram, dx, dy, null);
    } else {
      final int c;
      switch (realm) {
        case MATERIAL:
          c = 0;
          break;
        case ETHERIC:
          c = 5;
          break;
        case MYTHOS:
          c = 6;
          break;
        case DRACONIC:
          c = 7;
          break;
        case DEMONIC:
          c = 9;
          break;
        case HYPERASTRAL:
          c = 1;
          break;
        case SUBHYADIC:
          c = 4;
          break;
        case HYADIC:
          c = 2;
          break;
        default: // core
          c = 8;
          break;
      }
      g.setColor(color);
      g.fillOval(dx + mPX[c] - 2, dy + mPY[c] - 2, 5, 5);
    }
  }
}
