package irvine.util.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/**
 * Draw a simple lightning effect.
 * @author Sean A. Irvine
 */
public final class Lightning {

  private Lightning() {
  }

  /** Used for color selection. */
  private static final Random RANDOM = new Random();

  /**
   * Render a lightning effect into the specified graphics.  Partially unrenders
   * itself at the end of the effect, but the caller should best take effort to
   * repaint the screen afterwards.
   * @param g place to render
   * @param sx start x-coordinate
   * @param sy start y-coordinate
   * @param tx target x-coordinate
   * @param ty target y-coordinate
   * @param time total time in milliseconds to maintain lightning
   * @throws NullPointerException if <code>g</code> is null.
   */
  public static void draw(final Graphics g, final int sx, final int sy, final int tx, final int ty, final int time) {
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
    assert x.length == y.length;
    final long endTime = System.currentTimeMillis() + time;
    do {
      g.setColor(RANDOM.nextBoolean() ? Color.WHITE : Color.CYAN);
      g.drawPolyline(x, y, x.length);
      // We ideally want to have a wait for a very short random period of time
      // at this point.  However, Thread.sleep() is notoriously unreliable for
      // very short sleeps.  Instead we opt for a simple yield.  This will give
      // other threads a chance to do useful work, but hopefully still return
      // control to us quickly.
      Thread.yield();
    } while (System.currentTimeMillis() < endTime);
    g.setColor(Color.BLACK);
    g.drawPolyline(x, y, x.length);
  }

}
