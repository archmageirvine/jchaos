package irvine.tile;

/**
 * An effect that rotates an image successively through the specified angle
 * for a given number of steps.
 * @author Sean A. Irvine
 */
public class RotationEffect extends AbstractTileEffect {
  /** The original image. */
  private final TileImage mImage;
  /** Background color. */
  private final int mBg;
  /** Angle to turn at each step. */
  private final double mTheta;
  /** Total number of steps. */
  private final int mSteps;
  /** The current step number. */
  private int mCurrent = 0;
  /** Whether or not blending should be used. */
  private final boolean mBlending;

  /**
   * Construct a new effect to rotate a given image.  Positive angles correspond
   * to clockwise rotation.
   * @param baseImage image to be rotated
   * @param angle angle in degrees to rotate per step
   * @param steps number of steps
   * @param bg background color for revealed pixels
   * @param blending true to use blending, false preserves palette
   * @throws NullPointerException if <code>baseImage</code> is null.
   */
  public RotationEffect(final TileImage baseImage, final double angle, final int steps,
                        final int bg, final boolean blending) {
    if (baseImage == null) {
      throw new NullPointerException();
    }
    mImage = baseImage;
    mTheta = angle;
    mSteps = steps;
    mBlending = blending;
    mBg = bg;
  }

  @Override
  public TileImage next() {
    return mCurrent < mSteps
      ? mImage.rotate(++mCurrent * mTheta, mBg, mBlending)
      : null;
  }
}
