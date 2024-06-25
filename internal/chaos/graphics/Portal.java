package chaos.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;
import irvine.math.IntegerUtils;

/**
 * Draw the graphics for the Portal object.
 * @author Sean A. Irvine
 */
public final class Portal {

  private Portal() {
  }

  private static double textWidth(final String s, final Graphics g) {
    final Rectangle2D r = g.getFontMetrics().getStringBounds(s, g);
    return Math.max(r.getWidth(), r.getHeight());
  }

  private static void setFont(final Graphics g, final int size) {
    g.setFont(new Font("TimesRoman", Font.PLAIN, size));
  }

  private static int selectFont(final String s, final int w, final Graphics g) {
    final double targetMaxTextSize = w - 7;
    int minPt = 5;
    setFont(g, minPt);
    if (textWidth(s, g) > targetMaxTextSize) {
      return 0;
    }
    int maxPt = w;
    setFont(g, maxPt);
    // binary search
    while (true) {
      final int mid = minPt + (maxPt - minPt) / 2;
      setFont(g, mid);
      final double tw = textWidth(s, g);
      if (tw > targetMaxTextSize) {
        maxPt = mid - 1;
      } else if (targetMaxTextSize < tw) {
        minPt = mid + 1;
      } else {
        return mid;
      }
    }
  }

  private static void drawImageText(final Graphics g, final int w, final String text, final int symbolColor) {
    final int size = selectFont(text, w, g);
    if (size > 0) {
      final FontMetrics fm = g.getFontMetrics();
      final Rectangle2D r = fm.getStringBounds(text, g);
      final double tw = r.getWidth();
      final double th = r.getHeight();
      final double d = (w - tw) / 2;
      // This y-offset still might not be perfect
      final double y = w - th + fm.getMaxDescent();
      final int dx = (int) Math.round(d);
      final int dy = (int) Math.round(w - y / 2);
      final Color c = new Color(symbolColor);
      g.setColor(c.darker());
      g.drawString(text, dx - 1, dy - 1);
      g.setColor(c);
      g.drawString(text, dx, dy);
    } else {
      throw new RuntimeException();
    }
  }

  static void drawPortal(final Graphics2D g, final int w, final char symbol, final int symbolColor, final int ringColor, final double theta) {
    final Color bg = new Color(0xA9A9A9);
    g.setColor(bg);
    g.fillRect(0, 0, w, w);
    if (w > 16) {
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    final Color rc = new Color(ringColor);
    final Color drc = rc.darker();
    final int e = w / 8;
    if (w > 16) {
      g.setColor(drc);
      g.fillOval(e - 1, e - 1, 3 * w / 4 + 2, 3 * w / 4 + 2);
    }
    g.setColor(rc);
    g.fillOval(e, e, 3 * w / 4, 3 * w / 4);
    if (w > 16) {
      g.setColor(drc);
      g.fillOval(e + 1, e + 1, 3 * w / 4 - 2, 3 * w / 4 - 2);
    }
    g.setColor(bg);
    g.fillOval(e + 2, e + 2, 3 * w / 4 - 4, 3 * w / 4 - 4);
    drawImageText(g, w, String.valueOf(symbol), symbolColor);
    final double r = 3 * w / 8.0;
    final double x = r * Math.cos(theta);
    final double y = r * Math.sin(theta);
    final int b = IntegerUtils.lg(w);
    final double bh = 0.5 * b;
    g.setColor(new Color(symbolColor));
    g.fillOval(w / 2 + (int) Math.round(x - bh), w / 2 + (int) Math.round(y - bh), b, b);
    final int u = b - 3;
    final int v = u / 2;
    g.setColor(rc.darker());
    g.fillOval(e - v, e - v, u, u);
    g.fillOval(w - e - v, e - v, u, u);
    g.fillOval(e - v, w - e - v, u, u);
    g.fillOval(w - e - v, w - e - v, u, u);
  }


  /**
   * Used for testing.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("Test");
    SwingUtilities.invokeLater(() -> {
      f.setSize(530, 560);
      f.setVisible(true);
      for (int k = 0; k < 200; ++k) {
        final Graphics2D g = (Graphics2D) f.getGraphics();
        if (g != null) {
          final double theta = (5 * k) % (2 * Math.PI);
          g.translate(100, 100);
          drawPortal(g, 16, 'G', 0xC00000, 0x0000C0, theta);
          g.translate(100, 0);
          drawPortal(g, 32, 'G', 0xC00000, 0x0000C0, theta);
          g.translate(100, 0);
          drawPortal(g, 64, 'G', 0xC00000, 0x0000C0, theta);
          g.dispose();
        }
        Sleep.sleep(100);
      }
      f.dispose();
    });
  }
}
