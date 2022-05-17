package irvine.world;

/**
 * Provides implementation of a flat 2-dimensional world.
 *
 * @param <C> underlying cell type
 * @author Sean A. Irvine
 */
public class FlatWorld<C> extends AbstractWorld<C> {

  /** Width minus 1. */
  private final int mWidthM;
  /** Height minus 1. */
  private final int mHeightM;

  /**
   * Construct a world of given width and height.
   *
   * @param width width of world in cells
   * @param height height of world in cells
   * @exception IllegalArgumentException if <code>width</code> or <code>height
   * </code> is less than 1.
   */
  public FlatWorld(final int width, final int height) {
    super(width, height);
    mWidthM = width - 1;
    mHeightM = height - 1;
  }

  /** Temporary workspace. */
  private static final int[] TEMP = new int[2];

  @Override
  public boolean isEdge(final int cellNumber) {
    return getCellCoordinates(cellNumber, TEMP) && (TEMP[0] == 0 || TEMP[0] == mWidthM || TEMP[1] == 0 || TEMP[1] == mHeightM);
  }

  @Override
  public int getCellNumber(final int x, final int y) {
    return x < 0 || y < 0 || x >= width() || y >= height() ? -1 : y * width() + x;
  }

  @Override
  public int getSquaredDistance(final int a, final int b) {
    if (a < 0 || b < 0 || a >= size() || b >= size()) {
      return -1;
    }
    final int ca = mCellToCoords[a];
    final int cb = (~mCellToCoords[b] & ~0x8000) + 0x10001;
    int d = ca + cb;         // d is now (ya-yb:xa-xb)
    int dx = d & 0x7FFF;
    dx |= -(dx & 0x4000);    // sign extend dx
    d >>= 16;
    return dx * dx + d * d;
  }

}
