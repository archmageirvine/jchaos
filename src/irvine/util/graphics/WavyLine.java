package irvine.util.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import chaos.util.Sleep;

/**
 * Draw a wavy line effect.
 * @author Sean A. Irvine
 */
public final class WavyLine {

  private WavyLine() { }

  private static final int STEP_SIZE = 3;

  /**
   * Render a lightning effect into the specified graphics.  Partially unrenders
   * itself at the end of the effect, but the caller should best take effort to
   * repaint the screen afterwards.
   *
   * @param g place to render
   * @param sx start x-coordinate
   * @param sy start y-coordinate
   * @param tx target x-coordinate
   * @param ty target y-coordinate
   * @param time total time in milliseconds to maintain lightning
   * @param color color to draw with
   * @exception NullPointerException if <code>g</code> is null.
   */
  public static void draw(final Graphics2D g, final int sx, final int sy, final int tx, final int ty, final int time, final Color color) {
    final Stroke oldStroke = g.getStroke();
    g.setStroke(new BasicStroke(3));
    g.setColor(color);
    final int dx = tx - sx;
    final int dy = ty - sy;
    final double r = Math.sqrt(dx * dx + dy * dy);
    double theta = Math.acos(Math.abs(dx) / (r + 0.01));
    if (tx < sx) {
      theta = Math.PI - theta;
    }
    if (sy < ty) {
      theta = -theta;
    }
    final int pts = (int) (r / STEP_SIZE);
    final int[] x = new int[pts];
    final int[] y = new int[pts];
    final double cos = Math.cos(theta);
    final double sin = Math.sin(theta);
    for (int z = 0, k = 0; k < pts; z += STEP_SIZE, ++k) {
      final double px = z * cos;
      final double py = z * sin;
      final double l = 5 * Math.sin(z * 0.2);
      x[k] = (int) (sx + px - l * sin);
      y[k] = (int) (sy - py - l * cos);
    }
    g.drawPolyline(x, y, x.length);
    Sleep.sleep(time);
    g.setColor(Color.BLACK);
    g.drawPolyline(x, y, x.length);
    g.setStroke(oldStroke);
  }

}
