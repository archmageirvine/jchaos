package irvine.tile;

import java.util.Random;

/**
 * A twinkling star effect.
 *
 * @author Sean A. Irvine
 */
public class TwinkleEffect extends AbstractTileEffect {

  /** Filled background version of image. */
  private final TileImage mImage;
  /** Background color. */
  private final int mBg;
  /** Foreground color. */
  private final int mFg;
  /** Modified foreground color. */
  private final int mDim;
  /** Half magnitude foreground color. */
  private final int mHalfMagFg;
  /** Steps to perform. */
  private final int mSteps;
  /** Current step number. */
  private int mCurrentStep = 0;
  /** Coordinates and states of twinkles. */
  private final int[] mX, mY, mS;
  /** Random number generator to use. */
  private final Random mRandom;

  /**
   * Construct a new twinkle of given size and colors.  Produces images until the
   * result would be an empty image, at which point null is returned. For further
   * documentation on how the effect is produced see tile.tex.
   *
   * @param width with of image, must be a power of 2
   * @param bg background color
   * @param fg primary foreground color
   * @param steps number of steps to make
   * @param random random number generator
   * @exception IllegalArgumentException if <code>width</code> is less than 1.
   * @exception NullPointerException if <code>random</code> is null
   */
  public TwinkleEffect(final int width, final int bg, final int fg, final int steps, final Random random) {
    if (random == null) {
      throw new NullPointerException();
    }
    if (width < 1) {
      throw new IllegalArgumentException("bad width");
    }
    mImage = new TileImage(width, width);
    mImage.fill(bg);
    mFg = fg;
    mBg = bg;
    mDim = (mBg & 0xFFFFFF) > 0x808080 ? (mFg & 0x7F7F7F) : (mFg | 0x808080);
    mHalfMagFg = (mBg & 0xFFFFFF) > 0x808080 ? (mFg | 0x3F3F3F) : (mFg & ~0x3F3F3F);
    mSteps = steps;
    final int twinkles = width >>> 1;
    mX = new int[twinkles];
    mY = new int[twinkles];
    mS = new int[twinkles];
    mRandom = random;
  }

  /**
   * Construct a new twinkle of given size and colors.  Produces images until the
   * result would be an empty image, at which point null is returned. For further
   * documentation on how the effect is produced see tile.tex.
   *
   * @param width with of image, must be a power of 2
   * @param bg background color
   * @param fg primary foreground color
   * @param steps number of steps to make
   * @exception IllegalArgumentException if <code>width</code> is less than 1.
   */
  public TwinkleEffect(final int width, final int bg, final int fg, final int steps) {
    this(width, bg, fg, steps, new Random());
  }

  /** Set pixel provided it is inside image. */
  private void set(final TileImage i, final int x, final int y, final int w, final int rgb) {
    if (x >= 0 && y >= 0 && x < w && y < w) {
      i.setPixel(x, y, rgb == mBg ? rgb : (rgb ^ mRandom.nextInt() & 0x00303030));
    }
  }

  @Override
  public TileImage next() {
    if (mCurrentStep < mSteps) {
      ++mCurrentStep;
      final int w = mImage.getWidth();
      for (int j = 0; j < mS.length; ++j) {
        switch (mS[j]) {
        case 0:
          if (mRandom.nextInt(3) == 0) {
            mX[j] = mRandom.nextInt(w);
            mY[j] = mRandom.nextInt(w);
            mS[j] = 1;
            set(mImage, mX[j], mY[j], w, mFg);
          }
          break;
        case 1:
          switch (mRandom.nextInt(6)) {
          case 0:
            set(mImage, mX[j], mY[j], w, mBg);
            mS[j] = 0;
            break;
          case 2:
          case 3:
            set(mImage, mX[j] - 1, mY[j], w, mFg);
            set(mImage, mX[j] + 1, mY[j], w, mFg);
            set(mImage, mX[j], mY[j] - 1, w, mFg);
            set(mImage, mX[j], mY[j] + 1, w, mFg);
            mS[j] = 2;
            break;
          default:
            break;
          }
          break;
        case 2:
          switch (mRandom.nextInt(7)) {
          case 0:
            set(mImage, mX[j] - 1, mY[j], w, mBg);
            set(mImage, mX[j] + 1, mY[j], w, mBg);
            set(mImage, mX[j], mY[j] - 1, w, mBg);
            set(mImage, mX[j], mY[j] + 1, w, mBg);
            mS[j] = 1;
            break;
          case 1:
          case 2:
            set(mImage, mX[j], mY[j], w, mDim);
            break;
          case 3:
          case 4:
            set(mImage, mX[j] - 2, mY[j], w, mFg);
            set(mImage, mX[j] + 2, mY[j], w, mFg);
            set(mImage, mX[j], mY[j] - 2, w, mFg);
            set(mImage, mX[j], mY[j] + 2, w, mFg);
            set(mImage, mX[j] - 1, mY[j] + 1, w, mHalfMagFg);
            set(mImage, mX[j] + 1, mY[j] + 1, w, mHalfMagFg);
            set(mImage, mX[j] - 1, mY[j] - 1, w, mHalfMagFg);
            set(mImage, mX[j] + 1, mY[j] - 1, w, mHalfMagFg);
            mS[j] = 4;
            break;
          case 5:
            set(mImage, mX[j], mY[j] - 2, w, mFg);
            mS[j] = 5;
            break;
          default:
            break;
          }
          break;
        case 4:
          switch (mRandom.nextInt(4)) {
          case 0:
            set(mImage, mX[j] - 2, mY[j], w, mBg);
            set(mImage, mX[j] + 2, mY[j], w, mBg);
            set(mImage, mX[j], mY[j] - 2, w, mBg);
            set(mImage, mX[j], mY[j] - 2, w, mBg);
            set(mImage, mX[j] - 1, mY[j] + 1, w, mBg);
            set(mImage, mX[j] + 1, mY[j] + 1, w, mBg);
            set(mImage, mX[j] - 1, mY[j] - 1, w, mBg);
            set(mImage, mX[j] + 1, mY[j] - 1, w, mBg);
            mS[j] = 2;
            break;
          case 1:
            set(mImage, mX[j] - 1, mY[j], w, mDim);
            set(mImage, mX[j] + 1, mY[j], w, mDim);
            set(mImage, mX[j], mY[j] - 1, w, mDim);
            set(mImage, mX[j], mY[j] + 1, w, mDim);
            set(mImage, mX[j], mY[j], w, ~mBg);
            break;
          default:
            break;
          }
          break;
        case 5:
          if (mRandom.nextBoolean()) {
            set(mImage, mX[j], mY[j] - 2, w, mBg);
            mS[j] = 2;
          }
          break;
        default:
          break;
        }
      }
      // copy to make sure no one can monkey with our internal image
      return mImage.copy();
    }
    return null;
  }
}
