package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import chaos.board.World;
import chaos.util.Sleep;

/**
 * Routines to draw various beam weapons.
 * @author Sean A. Irvine
 */
public class Beam {

  /** Size of fixed-point arithmetic. */
  private static final int FIXED_POINTS_BITS = 16;
  /** Half in above fixed-point arithmetic. */
  private static final int HALF = 1 << (FIXED_POINTS_BITS - 1);

  private final int mWidthBits;
  private final int mHalfCellWidth;
  private final ChaosScreen mScreen;
  private final World mWorld;
  private final Graphics mGraphics;

  /**
   * Construct a new beam drawing object for the given world.  Any
   * synchronization required must occur before calling this class.
   * @param world the world
   * @param screen the screen
   * @param graphics graphics object
   * @param widthBits with of a cell in bits
   */
  public Beam(final World world, final ChaosScreen screen, final Graphics graphics, final int widthBits) {
    mWorld = world;
    mWidthBits = widthBits;
    mHalfCellWidth = 1 << (mWidthBits - 1);
    mScreen = screen;
    mGraphics = graphics;
  }

  /** Return true if <code>|dx|&gt;=|dy|</code>. */
  private static boolean absGreater(final int dx, final int dy) {
    return (dx >= 0 ? dx : -dx) >= (dy >= 0 ? dy : -dy);
  }

  /** For details of Blizzard see the LineOfSight documentation. */
  private void line(final int source, final int target, final Plotter plotter) {
    if (source == target) {
      return;
    }
    // get pixel coordinates of lower-left of each cell
    final int[] sxy = new int[2];
    mWorld.getCellCoordinates(source, sxy);
    final int xo = mScreen.getXOffset();
    final int yo = mScreen.getYOffset();
    final int osx = xo + (sxy[0] << mWidthBits);
    final int osy = yo + (sxy[1] << mWidthBits);
    final int[] txy = new int[2];
    mWorld.getCellCoordinates(target, txy);
    int tx = xo + (txy[0] << mWidthBits);
    int ty = yo + (txy[1] << mWidthBits);

    // adjust (sx,sy) to centre pixel of source closest to (tx,ty)
    // this is critical for correct functioning of what follows!
    int sx = osx + (osx >= tx ? mHalfCellWidth - 1 : mHalfCellWidth);
    int sy = osy + (osy >= ty ? mHalfCellWidth - 1 : mHalfCellWidth);
    // adjust (tx,ty) to centre pixel of target closest to (sx,sy)
    // less critical, but needed for technical correctness of slope
    tx += osx <= tx ? mHalfCellWidth - 1 : mHalfCellWidth;
    ty += osy <= ty ? mHalfCellWidth - 1 : mHalfCellWidth;

    final int dx = tx - sx;
    final int dy = ty - sy;

    if (absGreater(dx, dy)) {
      // x-axis movement is the longer
      final int slope = dx == 0 ? 0 : (dy << FIXED_POINTS_BITS) / dx;
      sy = (sy << FIXED_POINTS_BITS) + HALF;
      if (dx >= 0) {
        while (sx <= tx) {
          plotter.plot(sx, sy >>> FIXED_POINTS_BITS);
          sy += slope;
          ++sx;
        }
      } else {
        while (sx >= tx) {
          plotter.plot(sx, sy >>> FIXED_POINTS_BITS);
          sy -= slope;
          --sx;
        }
      }
    } else {
      // y-axis movement is the longer
      final int slope = (dx << FIXED_POINTS_BITS) / dy;
      sx = (sx << FIXED_POINTS_BITS) + HALF;
      if (dy >= 0) {
        while (sy <= ty) {
          plotter.plot(sx >>> FIXED_POINTS_BITS, sy);
          sx += slope;
          ++sy;
        }
      } else {
        while (sy >= ty) {
          plotter.plot(sx >>> FIXED_POINTS_BITS, sy);
          sx -= slope;
          --sy;
        }
      }
    }
    Animator.sync();
  }

  /**
   * Draw the standard casting beam in the specified color.
   * @param source source cell
   * @param target target cell
   * @param color beam color
   * @param noDelay no delay in drawing
   */
  public void tribeam(final int source, final int target, final Color color, final boolean noDelay) {
    final Plotter plotter = new Plotter() {
      private int mOldX = -10;
      private int mOldY = -10;
      private final boolean mMode;
      private final int mLimit = mWidthBits >> 2;
      private final int mDelta;
      private final int mSleep;

      {
        // get pixel coordinates of lower-left of each cell
        final int[] sxy = new int[2];
        mWorld.getCellCoordinates(source, sxy);
        final int[] txy = new int[2];
        mWorld.getCellCoordinates(target, txy);
        final int dx = txy[0] - sxy[0];
        final int dy = txy[1] - sxy[1];
        mMode = absGreater(dx, dy);
        if (mMode) {
          mDelta = sxy[0] > txy[0] ? mLimit : -mLimit;
        } else {
          mDelta = sxy[1] > txy[1] ? mLimit : -mLimit;
        }
        // The longer the line, the shorter the sleep, if we are going a really
        // long way, don't sleep at all.
        final int sqLen = dx * dx + dy * dy;
        mSleep = sqLen > 30 || noDelay ? 0 : Math.max(0, (int) Math.round((1 + 6.0 / (1 + sqLen)) / (mWidthBits - 3)));
      }

      @Override
      public void plot(final int x, final int y) {
        if (mMode) {
          mGraphics.drawLine(mOldX, mOldY - 1, mOldX + mDelta, mOldY - mLimit);
          mGraphics.drawLine(mOldX, mOldY + 1, mOldX + mDelta, mOldY + mLimit);
        } else {
          mGraphics.drawLine(mOldX + 1, mOldY, mOldX + mLimit, mOldY + mDelta);
          mGraphics.drawLine(mOldX - 1, mOldY, mOldX - mLimit, mOldY + mDelta);
        }
        mGraphics.drawLine(x, y, x, y);
        mOldX = x;
        mOldY = y;
        Sleep.sleep(mSleep);
      }
    };
    mGraphics.setColor(color);
    line(source, target, plotter);
    Sleep.sleep(80); // Leave whole line on screen briefly
    mGraphics.setColor(Color.BLACK);
    line(source, target, plotter);
  }

  private static class BreathPlotter implements Plotter {
    private int mCount = 0;
    private final Graphics mGraphics;
    private final Color[] mColor;
    private final int mSleep;
    private final Random mRandom = new Random();

    BreathPlotter(final Graphics graphics, final int sleep, final Color... color) {
      mGraphics = graphics;
      mColor = color;
      mSleep = sleep;
    }

    private void point(final int x, final int y) {
      mGraphics.setColor(mColor[mRandom.nextInt(mColor.length)]);
      mGraphics.drawLine(x, y, x, y);
    }

    @Override
    public void plot(int x, int y) {
      if (mCount++ % 5 == 0) {
        point(x++, y);
        point(x, y++);
        point(x, y);
        x -= 2;
        point(x, y++);
        x -= 2;
        point(x, y++);
        x += 2;
        point(x, y);
        x += 2;
        point(x++, y);
        y -= 2;
        point(x, y);
        y -= 2;
        point(x, y--);
        x -= 2;
        point(x, y);
        x -= 2;
        point(x--, y);
        y += 2;
        point(x, y);
      }
      // sleep gives some machine independence
      if (mColor[0] != Color.BLACK) {
        Sleep.sleep(mSleep);
      }
    }
  }

  /**
   * Draw the dragon breath style weapon from source to target in the
   * specified color.
   * @param source source cell
   * @param target target cell
   * @param color color to draw with
   */
  public void breathWeapon(final int source, final int target, final Color... color) {
    line(source, target, new BreathPlotter(mGraphics, 8 / (mWidthBits - 3), color));
  }

  /**
   * Draw fireball style effect
   * @param source source cell
   * @param target target cell
   */
  public void fireball(final int source, final int target) {
    final ImageList fireball = ImageList.getList("fireball", mWidthBits);
    line(source, target, new ImageListPlotter(fireball, mGraphics, 15 / (mWidthBits - 3)));
  }

  /**
   * Draw spinner style effect
   * @param source source cell
   * @param target target cell
   */
  public void spinner(final int source, final int target) {
    final ImageList spinner = ImageList.getList("spinner", mWidthBits);
    line(source, target, new ImageListPlotter(spinner, mGraphics, 15 / (mWidthBits - 3)));
  }

  private static class BoltPlotter implements Plotter {
    /**
     * Drawing control.
     */
    private int mCount = 0;
    /**
     * Old coordinates (used for undrawing).
     */
    private int mOldX = -1;
    private int mOldY;
    private final Graphics mGraphics;
    private final Color mColor;
    private final int mSleep;

    BoltPlotter(final Graphics graphics, final Color color, final int sleep) {
      mGraphics = graphics;
      mColor = color;
      mSleep = sleep;
    }

    private static void motif(final Graphics g, int x, int y) {
      g.drawLine(x, y, x, y);
      --x;
      ++y;
      g.drawLine(x, y, x, y);
      y += 2;
      g.drawLine(x, y, x, y);
      x += 2;
      --y;
      g.drawLine(x, y, x, y);
      y -= 2;
      ++x;
      g.drawLine(x, y, x, y);
      y -= 2;
      x -= 2;
      g.drawLine(x, y, x, y);
      x -= 2;
      ++y;
      g.drawLine(x, y, x, y);
      y += 2;
      --x;
      g.drawLine(x, y, x, y);
    }

    @Override
    public void plot(final int x, final int y) {
      if ((mCount++ & 3) == 0) {
        if (mOldX != -1) {
          mGraphics.setColor(Color.BLACK);
          motif(mGraphics, mOldX, mOldY);
        }
        mOldX = x;
        mOldY = y;
        mGraphics.setColor(mColor);
        motif(mGraphics, x, y);
        Sleep.sleep(mSleep);
      }
    }
  }

  /**
   * Draw the magic bolt weapon effect.  Also can be used for ranged
   * combat (e.g. pyrohydra).
   * @param source source cell
   * @param target target cell
   * @param color color to draw with
   */
  public void boltWeapon(final int source, final int target, final Color color) {
    line(source, target, new BoltPlotter(mGraphics, color, 50 / (mWidthBits - 3)));
  }

  /**
   * Draw the bird shit weapon effect.
   * @param source source cell
   * @param target target cell
   * @param color color to draw with
   */
  public void birdShit(final int source, final int target, final Color color) {
    final Plotter plotter = new Plotter() {
      /**
       * Old coordinates (used for undrawing).
       */
      private int mOldX = -1;
      private int mOldY;
      private final int mSleep;

      {
        // get pixel coordinates of lower-left of each cell
        final int[] sxy = new int[2];
        mWorld.getCellCoordinates(source, sxy);
        final int[] txy = new int[2];
        mWorld.getCellCoordinates(target, txy);
        final int dx = txy[0] - sxy[0];
        final int dy = txy[1] - sxy[1];
        // The longer the line, the shorter the sleep, if we are going a really
        // long way, don't sleep at all.
        final int sqLen = dx * dx + dy * dy;
        mSleep = sqLen > 30 ? 0 : 8 / (mWidthBits - 3);
      }

      @Override
      public void plot(final int x, final int y) {
        if (mOldX != -1) {
          mGraphics.setColor(Color.BLACK);
          mGraphics.fillOval(mOldX, mOldY, 6, 6);
        }
        mOldX = x;
        mOldY = y;
        mGraphics.setColor(color);
        mGraphics.fillOval(x, y, 6, 6);
        Sleep.sleep(mSleep);
      }
    };
    line(source, target, plotter);
  }
}
