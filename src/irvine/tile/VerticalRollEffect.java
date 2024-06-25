package irvine.tile;

/**
 * An effect which rolls a tile one pixel at a time.
 * @author Sean A. Irvine
 */
public class VerticalRollEffect extends AbstractTileEffect {

  /** The image. */
  private final TileImage mImage;
  /** The frames of the underlying effect. */
  private final boolean mDir;
  /** Counts the iterations. */
  private int mCurrent = 0;

  /**
   * Construct a vertical roll effect for the specified direction.
   * @param image underlying image
   * @param dir true for downwards, false for upwards
   * @throws NullPointerException if <code>image</code> is <code>null</code>.
   */
  public VerticalRollEffect(final TileImage image, final boolean dir) {
    if (image == null) {
      throw new NullPointerException();
    }
    mImage = image;
    mDir = dir;
  }

  @Override
  public TileImage next() {
    return mCurrent < mImage.getHeight() ? mImage.vroll(mDir ? ++mCurrent : -++mCurrent) : null;
  }
}
