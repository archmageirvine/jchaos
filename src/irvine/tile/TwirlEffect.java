package irvine.tile;

/**
 * A rotating four-armed twirl that decays with the arm thickness being
 * an eighth of the image width.
 *
 * @author Sean A. Irvine
 */
public class TwirlEffect extends AbstractTileEffect {

  /** Width of image. */
  private final int mWidth;
  /** Background color. */
  private final int mBack;
  /** Foreground color. */
  private final int mFore;
  /** Shrink rate. */
  private final int mShrink;
  /** Angle in degrees. */
  private final double mAngle;
  /** Scratch space used to compute unrotated image. */
  private final TileImage mScratch;
  /** Spacing from the origin and thickness of arms. */
  private final int mThickness;
  /** Current length of arm in twirl. */
  private int mCurrent;
  /** Current rotation angle. */
  private double mCurrentAngle = 0.0;
  /** Current shrink count. */
  private int mShrinkCount = 0;
  /** Is the current scratch image invalid. */
  private boolean mScratchInvalid = true;

  /**
   * Construct a new twirl of given size and colors.  Produces images until the
   * result would be an empty image, at which point null is returned.
   *
   * @param width with of image, must be a power of 2
   * @param back background color
   * @param fore foreground color
   * @param shrink number of steps to make before shrinking arms by one unit
   * @param angle angle to rotate by at each step
   * @exception IllegalArgumentException if <code>shrink</code> is less than 1 or
   * <code>width</code> is not a positive power of 2.
   */
  public TwirlEffect(final int width, final int back, final int fore,
                     final int shrink, final double angle) {
    if (width < 2 || (width & (width - 1)) != 0) {
      throw new IllegalArgumentException("Width must be positive power of 2.");
    }
    if (shrink < 1) {
      throw new IllegalArgumentException("Illegal shrink.");
    }
    mWidth = width;
    mBack = back;
    mFore = fore;
    mShrink = shrink;
    mAngle = angle;
    mScratch = new TileImage(width, width);
    mThickness = mWidth >> 3;
    mCurrent = mWidth / 2 - mThickness;
  }

  @Override
  public TileImage next() {
    if (mCurrent > 0) {
      // produce non-rotated image of correct length
      if (mScratchInvalid) {
        final int half = mWidth / 2;
        mScratch.fill(mBack);
        final int offset = half - mThickness / 2;
        for (int i = 0; i < mCurrent; ++i) {
          for (int j = 0; j < mThickness; ++j) {
            mScratch.setPixel(half - mThickness - i - 1, offset + j, mFore);
            mScratch.setPixel(half + mThickness + i, offset + j, mFore);
            mScratch.setPixel(offset + j, half - mThickness - i - 1, mFore);
            mScratch.setPixel(offset + j, half + mThickness + i, mFore);
          }
        }
      }
      // rotate to correct angle
      final TileImage result = mScratch.rotate(mCurrentAngle, mBack, false);
      // update angle and arm length for next image
      mCurrentAngle += mAngle;
      if (++mShrinkCount >= mShrink) {
        mShrinkCount = 0;
        --mCurrent;
        mScratchInvalid = true;
      }
      return result.copy(); // make sure return is immutable
    }
    return null;
  }
}
