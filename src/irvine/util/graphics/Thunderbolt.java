package irvine.util.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Random;

import chaos.graphics.ChaosScreen;

/**
 * Draw a thunderbolt effect.
 *
 * @author Sean A. Irvine
 */
public final class Thunderbolt {

  private Thunderbolt() { }

  /** Used for color selection. */
  private static final Random RANDOM = new Random();
  private static final Color ORANGE = new Color(0xFFA500);
  private static final Color DARK_RED = Color.RED.darker();

  /**
   * Render a lightning effect into the specified graphics.  Partially unrenders
   * itself at the end of the effect, but the caller should best take effort to
   * repaint the screen afterwards.
   *
   * @param screen chaos screen
   * @param graphics place to render
   * @param sx start x-coordinate
   * @param sy start y-coordinate
   * @param tx target x-coordinate
   * @param ty target y-coordinate
   * @param time total time in milliseconds to maintain lightning
   */
  public static void draw(final ChaosScreen screen, final Graphics2D graphics, final int sx, final int sy, final int tx, final int ty, final int time) {
    final Stroke oldStroke = graphics.getStroke();
    final int dx = tx - sx;
    final int dy = ty - sy;
    double r = Math.sqrt(dx * dx + dy * dy);
    double theta = Math.acos(Math.abs(dx) / (r + 0.01));
    if (tx < sx) {
      theta = Math.PI - theta;
    }
    if (sy < ty) {
      theta = -theta;
    }
    r /= 2;
    final double tp = Math.cos(theta + 0.2);
    final double tm = Math.cos(theta - 0.2);
    final double sp = Math.sin(theta + 0.2);
    final double sm = Math.sin(theta - 0.2);
    final int[] x = {sx, sx + (int) ((r + 5) * tp), sx + (int) ((r - 5) * tm), tx};
    final int[] y = {sy, sy - (int) ((r + 5) * sp), sy - (int) ((r - 5) * sm), ty};
    final long endTime = System.currentTimeMillis() + time;
    final BasicStroke stroke2 = new BasicStroke(2);
    final BasicStroke stroke3 = new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    final Shape oldClip = screen == null ? null : screen.clipToArena(graphics);
    do {
      graphics.setStroke(stroke3);
      graphics.setColor(RANDOM.nextBoolean() ? Color.RED : DARK_RED);
      graphics.drawPolyline(x, y, x.length);
      graphics.setStroke(stroke2);
      graphics.setColor(ORANGE);
      graphics.drawPolyline(x, y, x.length);
      // We ideally want to have a wait for a very short random period of time
      // at this point.  However, Thread.sleep() is notoriously unreliable for
      // very short sleeps.  Instead we opt for a simple yield.  This will give
      // other threads a chance to do useful work, but hopefully still return
      // control to us quickly.
      Thread.yield();
    } while (System.currentTimeMillis() < endTime);
    graphics.setColor(Color.BLACK);
    graphics.setStroke(stroke3);
    graphics.drawPolyline(x, y, x.length);
    graphics.setStroke(oldStroke);
    graphics.setClip(oldClip);
  }
}
