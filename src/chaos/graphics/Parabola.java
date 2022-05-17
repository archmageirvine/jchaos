package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;

import chaos.board.World;

/**
 * Routines to draw parabolas.
 *
 * @author Sean A. Irvine
 */
public class Parabola {

  /**
   * Ratio of height to length
   */
  private static final float HEIGHT_RATIO = 0.6F;

  /**
   * Place to draw.
   */
  private final ChaosScreen mScreen;
  private final Graphics mGraphics;

  /**
   * Width of a cell in pixels.
   */
  private final int mWidth;

  /**
   * Positioning arrays.
   */
  private final int[] mXPixel;
  private final int[] mYPixel;

  /**
   * Construct a new parabola drawing facility for the given world.
   *
   * @param world world
   * @param screen where to draw
   * @param graphics where to draw
   * @param widthBits cell width bits
   */
  public Parabola(final World world, final ChaosScreen screen, final Graphics graphics, final int widthBits) {
    mScreen = screen;
    mGraphics = graphics;
    mWidth = 1 << widthBits;
    final int hw = mWidth >>> 1;
    final int dx = hw + screen.getXOffset();
    final int dy = hw + screen.getYOffset();
    final int s = world.size();
    mXPixel = new int[s];
    mYPixel = new int[s];
    for (int i = 0; i < s; ++i) {
      mXPixel[i] = ((i % world.width()) << widthBits) + dx;
      mYPixel[i] = ((i / world.width()) << widthBits) + dy;
    }
  }


  /**
   * Draw an upwards parabola simulating a projectile flight from the
   * source cell to the target in the given color.  Does not undraw
   * itself; thus, you will generally want to call this with black
   * to undraw.
   *
   * @param source source cell
   * @param target target cell
   * @param color color to draw in
   */
  public void parabola(final int source, final int target, final Color color) {
    if (source == target) {
      return; // nothing to draw
    }

    // get x-coordinate for source and target
    final int sx = mXPixel[source];
    final int tx = mXPixel[target];
    // get y-coordinate for source and target
    final int sy = mYPixel[source];
    final int ty = mYPixel[target];
    // slope
    final int dx = (sx - tx) * (sx - tx);

    mGraphics.setColor(color);
    // Vertical shots and shots on top line of board are represented
    // by straight lines, in the first case because there is no other
    // sensible option, and in the second to prevent the shot being
    // completely clipped.
    if (sx == tx || (sy == ty && sy < mWidth)) {
      mGraphics.drawLine(sx, sy, tx, ty);
    } else {
      final int miny = mScreen.getYOffset();
      // compute square of length of straight line between source and target
      // work out direction
      final int e = tx > sx ? 1 : -1;
      // compute mid-point in x direction
      final int mx = (tx + sx) >> 1;
      // height control
      final int h = (int) (HEIGHT_RATIO * Math.sqrt(dx + (sy - ty) * (sy - ty)));
      final float d = 4.0F * h / dx;
      // compute slope of line
      final float dy = e * (float) (ty - sy) / (float) (tx - sx);
      int ox = sx;
      int oy = sy;
      int px = sx;
      float py = (float) sy - h;
      while (px != tx) {
        final float y = (float) (mx - px);
        final int ppy = (int) (py + d * y * y);
        if (oy >= miny || ppy >= miny) {
          // at least one end of the line is on the screen
          mGraphics.drawLine(ox, Math.max(oy, miny), px, Math.max(ppy, miny));
        }
        ox = px;
        oy = ppy;
        px += e;
        py += dy;
      }
      // Ensure we handle any left over vertical piece
      mGraphics.drawLine(ox, Math.max(oy, miny), ox, ty);
    }
  }
}
