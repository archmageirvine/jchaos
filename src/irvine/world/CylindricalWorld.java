package irvine.world;

/**
 * Provides implementation of a 2-dimensional world where the grid is
 * wrapped from left to right thereby forming a cylinder.
 * @param <C> underlying cell type
 * @author Sean A. Irvine
 */
public class CylindricalWorld<C> extends AbstractWorld<C> {

  /** Half the width. */
  private final int mHalfWidth;
  /** Height minus 1. */
  private final int mHeightM;

  /**
   * Construct a cylindrical world of given width and height.
   * @param width width of world in cells
   * @param height height of world in cells
   * @throws IllegalArgumentException if <code>width</code> or <code>height
   * </code> is less than 1.
   */
  public CylindricalWorld(final int width, final int height) {
    super(width, height);
    mHalfWidth = width / 2;
    mHeightM = height - 1;
  }

  private static final int[] TEMP = new int[2];

  @Override
  public boolean isEdge(final int cellNumber) {
    return getCellCoordinates(cellNumber, TEMP) && (TEMP[1] == 0 || TEMP[1] == mHeightM);
  }

  @Override
  public int getCellNumber(int x, final int y) {
    if (y < 0 || y >= height()) {
      return -1;
    }
    x %= width();
    if (x < 0) {
      x += width();
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
    return x * x + y * y;
  }
}
