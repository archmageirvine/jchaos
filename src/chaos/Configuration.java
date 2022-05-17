package chaos;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.Serializable;

/**
 * Looks at the display device and determines the overall game shape.
 *
 * @author Sean A. Irvine
 */
public class Configuration implements Serializable {

  private static final int MINIMUM_HORIZONTAL_CELLS = 24;
  private static final int MINIMUM_VERTICAL_CELLS = 18;
  private static final int[] CELL_BITS = {6, 5, 4};

  private final int mScreenWidth;
  private final int mScreenHeight;
  private final int mDepth;
  private final int mWidth;
  private final int mHeight;
  private final int mCellBits;
  private final int mCellWidth;
  private final int mWorldRows;
  private final int mWorldCols;

  /**
   * Construct a new configuration.
   * @param minCols minimum number of columns
   * @param minRows minimum number of rows
   * @param headless true if this configuration is without a graphical display
   * @param worldRows height of world in cells
   * @param worldCols width of world in cells
   */
  public Configuration(final int minCols, final int minRows, final boolean headless, final int worldRows, final int worldCols) {
    mWorldRows = worldRows;
    mWorldCols = worldCols;
    if (headless) {
      mDepth = 0;
      mScreenWidth = 0;
      mScreenHeight = 0;
      mCellBits = 0;
      mCellWidth = 0;
      mWidth = minCols;
      mHeight = minRows;
    } else {
      if (GraphicsEnvironment.isHeadless()) {
        throw new UnsupportedOperationException("No graphics device found.");
      }
      final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      final GraphicsDevice gd = ge.getDefaultScreenDevice();
      final DisplayMode mode = gd.getDisplayMode();
      mDepth = mode.getBitDepth();
      mScreenWidth = mode.getWidth();
      mScreenHeight = mode.getHeight();
      // Up to 80% of screen space dedicated to main board
      final int usableX = getPixelWidth() * 8 / 10;
      final int usableY = getPixelHeight() * 8 / 10;
      for (final int b : CELL_BITS) {
        if (usableX >> b >= minCols && usableY >> b >= minRows) {
          mCellBits = b;
          mCellWidth = 1 << b;
          mWidth = usableX / mCellWidth;
          mHeight = usableY / mCellWidth;
          return;
        }
      }
      throw new UnsupportedOperationException("Cannot meet screen requirements.");
    }
  }

  /**
   * New configuration.
   * @param worldRows number of rows
   * @param worldCols number of columns
   */
  public Configuration(final int worldRows, final int worldCols) {
    this(MINIMUM_HORIZONTAL_CELLS, MINIMUM_VERTICAL_CELLS, true, worldRows, worldCols);
  }

  /**
   * New default configuration.
   */
  public Configuration() {
    this(-1, -1);
  }

  public int getPixelWidth() {
    return mScreenWidth;
  }

  public int getPixelHeight() {
    return mScreenHeight;
  }

  public int getDepth() {
    return mDepth;
  }

  public int getCols() {
    return mWidth;
  }

  public int getRows() {
    return mHeight;
  }

  public int getCellWidth() {
    return mCellWidth;
  }

  public int getCellBits() {
    return mCellBits;
  }

  public int getWorldRows() {
    return mWorldRows == -1 ? getRows() : mWorldRows;
  }

  public int getWorldCols() {
    return mWorldCols == -1 ? getCols() : mWorldCols;
  }

  @Override
  public String toString() {
    return getPixelWidth() + "x" + getPixelHeight() + " @ " + getDepth() + " -> " + getCols() + "x" + getRows() + " cw=" + getCellWidth();
  }


  /**
   * Dump default configuration.
   *
   * @param args ignored
   */
  public static void main(final String[] args) {
    System.out.println(new Configuration(MINIMUM_HORIZONTAL_CELLS, MINIMUM_VERTICAL_CELLS, false, -1, -1).toString());
  }
}

