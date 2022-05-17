package irvine.util.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;
import irvine.math.r.Constants;
import irvine.tile.ImageUtils;
import irvine.tile.TileImage;

/**
 * Draw a simple plasma effect.
 * @author Sean A. Irvine
 */
public final class Plasma {

  private Plasma() { }

  private static final Random RANDOM = new Random();

  private static int getRandomColor(final int baseColor) {
    int b = baseColor & 0xFF;
    int g = (baseColor >> 8) & 0xFF;
    int r = (baseColor >> 16) & 0xFF;
    b = b == 0 ? RANDOM.nextInt(100) : b - RANDOM.nextInt(b / 2);
    g = g == 0 ? RANDOM.nextInt(100) : g - RANDOM.nextInt(g / 2);
    r = r == 0 ? RANDOM.nextInt(100) : r - RANDOM.nextInt(r / 2);
    return (r << 16) | (g << 8) | b;
  }

  // This is pulled out separately for documentation purposes.
  private static void performHorizontalPlasma(final Graphics g, final int sx, final int tx, final int sy, final int w, final int color, final long time) {
    final long startTime = System.currentTimeMillis();
    int sleepDelay = 100;
    final int length, delta;
    final int d = tx - sx;
    if (d < 0) {
      length = -d;
      delta = -1;
    } else {
      length = d;
      delta = 1;
    }
    final int[] x = new int[length];
    final int[] y = new int[length];
    do {
      final double shift = RANDOM.nextDouble() * Constants.TAU;
      final double scale = RANDOM.nextDouble() * 0.33;
      for (int k = 0, j = sx; k < length; ++k, j += delta) {
        x[k] = j;
        y[k] = sy + (int) (w * Math.sin(k * scale + shift));
      }
      g.setColor(new Color(getRandomColor(color)));
      g.drawPolyline(x, y, length);
      Sleep.sleep(sleepDelay++ / 20);
    } while (System.currentTimeMillis() - startTime < time);
  }

  private static void horizontalPlasma(final Graphics g, final int sx, final int tx, final int sy, final int w, final int color, final long time) {
    performHorizontalPlasma(g, sx, tx, sy, w, color, time);
    g.setColor(Color.BLACK);
    g.fillRect(tx - sx < 0 ? tx : sx, sy - w, Math.abs(tx - sx) + 1, 2 * w);
  }

  private static void verticalPlasma(final Graphics g, final int sy, final int ty, final int sx, final int w, final int color, final long time) {
    final long startTime = System.currentTimeMillis();
    int sleepDelay = 100;
    final int length, delta;
    final int d = ty - sy;
    if (d < 0) {
      length = -d;
      delta = -1;
    } else {
      length = d;
      delta = 1;
    }
    final int[] x = new int[length];
    final int[] y = new int[length];
    do {
      final double shift = RANDOM.nextDouble() * Constants.TAU;
      final double scale = RANDOM.nextDouble() * 0.33;
      for (int k = 0, j = sy; k < length; ++k, j += delta) {
        y[k] = j;
        x[k] = sx + (int) (w * Math.sin(k * scale + shift));
      }
      g.setColor(new Color(getRandomColor(color)));
      g.drawPolyline(x, y, length);
      Sleep.sleep(sleepDelay++ / 20);
    } while (System.currentTimeMillis() - startTime < time);
    g.setColor(Color.BLACK);
    g.fillRect(sx - w, delta == 1 ? sy : ty, 2 * w, length + 1);
  }

  private static void genericPlasma(final Graphics g, int sx, int sy, int tx, int ty, final int w, final int color, final long time) {
    // This is slower than the special cases.
    // First ensure sx is the left end
    if (sx > tx) {
      final int xt = sx;
      sx = tx;
      tx = xt;
      final int yt = sy;
      sy = ty;
      ty = yt;
    }
    final int dx = tx - sx;
    final int dy = ty - sy;
    final double theta = Math.atan(dy / (double) dx);
    final double cosTheta = Math.cos(theta);
    final double sinTheta = Math.sin(theta);
    final int length = (int) (dx / cosTheta);

    // We step along the hypoteneuse
    final long startTime = System.currentTimeMillis();
    int sleepDelay = 100;
    final int[] x = new int[length];
    final int[] y = new int[length];
    do {
      final double shift = RANDOM.nextDouble() * Constants.TAU;
      final double scale = RANDOM.nextDouble() * 0.33;
      for (int k = 0; k < length; ++k) {
        final double displacement = w * Math.sin(k * scale + shift);
        x[k] = sx + (int) (k * cosTheta - displacement * sinTheta);
        y[k] = sy + (int) (k * Math.sin(theta) + displacement * cosTheta);
      }
      g.setColor(new Color(getRandomColor(color)));
      g.drawPolyline(x, y, length);
      Sleep.sleep(sleepDelay++ / 20);
    } while (System.currentTimeMillis() - startTime < time);
    g.setColor(Color.BLACK);
    final int rx = (int) ((w + 2) * sinTheta);
    final int ry = (int) ((w + 2) * cosTheta);
    g.fillPolygon(new int[] {
                    sx + rx,
                    tx + rx,
                    tx - rx,
                    sx - rx,
                  }, new int[] {
                    sy - ry,
                    ty - ry,
                    ty + ry,
                    sy + ry,
                  }, 4);
  }

  /**
   * Render a plasma effect into the specified graphics.  Partially unrenders
   * itself at the end of the effect, but the caller should best take effort to
   * repaint the screen afterwards.
   *
   * @param g place to render
   * @param sx start x-coordinate
   * @param sy start y-coordinate
   * @param tx target x-coordinate
   * @param ty target y-coordinate
   * @param w width parameter
   * @param color color parameter
   * @param time total time in milliseconds to maintain plasma
   * @exception NullPointerException if <code>g</code> is null.
   */
  public static void plasma(final Graphics g, final int sx, final int sy, final int tx, final int ty, final int w, final int color, final long time) {
    if (sy == ty) {
      horizontalPlasma(g, sx, tx, sy, w, color, time);
    } else if (sx == tx) {
      verticalPlasma(g, sy, ty, sx, w, color, time);
    } else {
      genericPlasma(g, sx, sy, tx, ty, w, color, time);
    }
  }

  /**
   * Just exercises the plasma routines a little bit or produce documentation.
   *
   * @param args ignored
   * @throws Exception if an error occurs.
   */
  public static void main(final String[] args) throws Exception {
    if (args.length > 0 && "-doc".equals(args[0])) {
      final BufferedImage b = new BufferedImage(200, 15, BufferedImage.TYPE_INT_ARGB);
      final Graphics g = b.getGraphics();
      if (g != null) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 15);
        for (int k = 1; k <= 10; ++k) {
          try (final FileOutputStream fos = new FileOutputStream("plasma" + k + ".ppm")) {
            performHorizontalPlasma(g, 0, 200, 7, 7, 0xFF0000FF, 0);
            ImageUtils.writePPM(new TileImage(b), fos);
            fos.flush();
          }
        }
        for (int k = 20; k <= 100; k += 10) {
          try (final FileOutputStream fos = new FileOutputStream("plasma" + k + ".ppm")) {
            for (int j = 0; j < 10; ++j) {
              performHorizontalPlasma(g, 0, 200, 7, 7, 0xFF0000FF, 0);
            }
            ImageUtils.writePPM(new TileImage(b), fos);
            fos.flush();
          }
        }
        g.dispose();
      }
    } else {
      final JFrame f = new JFrame("test");
      SwingUtilities.invokeLater(() -> {
        f.setSize(500, 500);
        f.setVisible(true);
        final Graphics g = f.getGraphics();
        if (g != null) {
          plasma(g, 50, 100, 300, 100, 5, 0xFFFF, 5000);
          plasma(g, 300, 200, 50, 200, 12, 0xFFFF00, 2000);
          plasma(g, 100, 50, 100, 300, 10, 0xFF00FF, 5000);
          plasma(g, 200, 300, 200, 50, 6, 0xFF0000, 2000);
          plasma(g, 50, 200, 300, 300, 10, 0xFF00, 2000);
          plasma(g, 300, 200, 50, 300, 10, 0xFF, 2000);
          plasma(g, 50, 300, 300, 200, 10, 0xFF00, 2000);
          plasma(g, 300, 300, 50, 200, 10, 0xFF, 2000);
          g.dispose();
        }
        Sleep.sleep(1000);
        f.dispose();
        System.exit(1);
      });
    }
  }
}
