package irvine.world;

/**
 * Provides implementation of a 2-dimensional world where the grid is
 * wrapped from left to right and top to bottom thereby forming a torus.
 *
 * @param <C> underlying cell type
 * @author Sean A. Irvine
 */
public class ToroidalWorld<C> extends AbstractWorld<C> {

  /** Half the width. */
  private final int mHalfWidth;
  /** Half the height. */
  private final int mHalfHeight;

  /**
   * Construct a toroidal world of given width and height.
   *
   * @param width width of world in cells
   * @param height height of world in cells
   * @exception IllegalArgumentException if <code>width</code> or <code>height
   * </code> is less than 1.
   */
  public ToroidalWorld(final int width, final int height) {
    super(width, height);
    mHalfWidth = width / 2;
    mHalfHeight = height / 2;
  }

  /** Temporary workspace. */
  private static final int[] TEMP = new int[2];

  @Override
  public boolean isEdge(final int cellNumber) {
    return false;
  }

  @Override
  public int getCellNumber(int x, int y) {
    x %= width();
    if (x < 0) {
      x += width();
    }
    y %= height();
    if (y < 0) {
      y += height();
    }
    return y * width() + x;
  }

  @Override
  public int getSquaredDistance(final int a, final int b) {
    if (!getCellCoordinates(a, TEMP)) {
      return -1;
    }
    int x = TEMP[0];
    int y = TEMP[1];
    if (!getCellCoordinates(b, TEMP)) {
      return -1;
    }
    x -= TEMP[0];
    y -= TEMP[1];
    x = Math.abs(x);
    if (x > mHalfWidth) {
      x = width() - x;
    }
    y = Math.abs(y);
    if (y > mHalfHeight) {
      y = height() - y;
    }
    return x * x + y * y;
  }
}
