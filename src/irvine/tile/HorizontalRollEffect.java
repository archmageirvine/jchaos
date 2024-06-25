package irvine.tile;

/**
 * An effect which rolls a tile one pixel at a time.
 * @author Sean A. Irvine
 */
public class HorizontalRollEffect extends AbstractTileEffect {

  private final TileImage mImage;
  private final boolean mDirection;
  private int mCurrent = 0;

  /**
   * Construct a horizontal roll effect for the specified direction.
   * @param image underlying image
   * @param dir true for left, false for right
   * @throws NullPointerException if <code>image</code> is <code>null</code>.
   */
  public HorizontalRollEffect(final TileImage image, final boolean dir) {
    if (image == null) {
      throw new NullPointerException();
    }
    mImage = image;
    mDirection = dir;
  }

  @Override
  public TileImage next() {
    return mCurrent < mImage.getWidth() ? mImage.hroll(mDirection ? ++mCurrent : -++mCurrent) : null;
  }
}
