package irvine.tile;

import java.util.Set;

import irvine.util.Point;
import irvine.world.FlatWorld;

/**
 * A graphical effect in which a circle expands from the centre to fill most
 * of the tile, then disappears from the centre outwards.
 * @author Sean A. Irvine
 */
public class ExplodingCircleEffect extends AbstractTileEffect {

  /** Used to compute the pixels to set. */
  private final FlatWorld<Point> mGrid;
  /** Current state. */
  private int mCurrent = 0;
  /** Background color. */
  private final int mBack;
  /** Foreground color. */
  private final int mFore;
  /** Origin cell number, the centre cell in this case. */
  private final int mOrigin;
  /** The maximum radius of the effect. */
  private final int mRadius;

  /**
   * A new exploding circle effect using the given color.
   * @param width width of tile
   * @param back background color
   * @param fore foreground color
   * @throws IllegalArgumentException if <code>width</code> is less than 1.
   */
  public ExplodingCircleEffect(final int width, final int back, final int fore) {
    if (width < 1) {
      throw new IllegalArgumentException("Bad width.");
    }
    mBack = back;
    mFore = fore;
    mGrid = new FlatWorld<>(width, width);
    for (int c = 0, y = 0; y < width; ++y) {
      for (int x = 0; x < width; ++x, ++c) {
        mGrid.setCell(c, new Point(x, y));
      }
    }
    mOrigin = mGrid.getCellNumber(width / 2, (width - 1) / 2);
    mRadius = width - width / 2 - 1;
  }

  @Override
  public TileImage next() {
    final int max, min, w = mGrid.width();
    if (mCurrent <= mRadius) {
      min = 0;
      max = mCurrent;
    } else if (mCurrent - mRadius <= mRadius) {
      min = mCurrent - mRadius;
      max = mRadius;
    } else {
      return null;
    }
    ++mCurrent;
    final TileImage image = new TileImage(w, w);
    image.fill(mBack);
    final Set<Point> cells = mGrid.getCells(mOrigin, min, max, null);
    for (final Point c : cells) {
      image.setPixel(c.left(), c.right(), mFore);
    }
    return image;
  }
}
