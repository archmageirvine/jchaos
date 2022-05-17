package irvine.tile;

import java.util.Random;

/**
 * An effect that fades one image into another.
 *
 * @author Sean A. Irvine
 */
public class PixelFadeEffect extends AbstractTileEffect {

  /** The original image. */
  private final TileImage mImage1;
  /** The target image. */
  private final TileImage mImage2;
  /** Number of steps to make in the fade. */
  private final int mSteps;
  /** Number of pixels per step to change. */
  private final int mPixelsPerStep;
  /** Permutation array. */
  private final int[] mPerm;
  /** The current step number. */
  private int mCurrent = 0;

  /** Create a filled image of given size. */
  private static TileImage getFilledImage(final TileImage src, final int color) {
    final TileImage i = new TileImage(src.getWidth(), src.getHeight());
    i.fill(color);
    return i;
  }

  /**
   * Fade the first image into the second image.  This transformation always
   * takes exactly 8 steps but could be modified for other lengths if required.
   *
   * @param image1 first image
   * @param image2 second image
   * @param steps number of steps in effect
   * @exception NullPointerException if either image is null.
   * @exception IllegalArgumentException if the images have different sizes or
   * if the number of steps does not divide the number of pixels in the images
   * or the number of steps is less than 1.
   */
  public PixelFadeEffect(final TileImage image1, final TileImage image2, final int steps) {
    final int w = image1.getWidth();
    int h = image1.getHeight();
    if (w != image2.getWidth() || h != image2.getHeight()) {
      throw new IllegalArgumentException("Size mismatch");
    }
    if (steps < 1 || (w * h) % steps != 0) {
      throw new IllegalArgumentException("Bad steps");
    }
    mSteps = steps;
    mImage1 = image1;
    mImage2 = image2;
    // random permutation array
    mPerm = new int[w * h];
    h <<= 16;
    for (int y = 0, c = 0; y < h; y += 65536) {
      for (int x = 0; x < w; ++x, ++c) {
        mPerm[c] = x | y;
      }
    }
    final Random r = new Random();
    for (int c = 0; c < mPerm.length; ++c) {
      final int j = r.nextInt(mPerm.length);
      final int t = mPerm[c];
      mPerm[c] = mPerm[j];
      mPerm[j] = t;
    }
    mPixelsPerStep = mPerm.length / mSteps;
  }

  /**
   * Fade the first image to the given color.  This transformation always
   * takes exactly eight steps but could be modified for other lengths if required.
   *
   * @param image initial image
   * @param color target color
   * @param steps number of steps in effect
   * @exception NullPointerException if the number of steps does not divide the
   * number of pixels in the images or the number of steps is less than 1.
   */
  public PixelFadeEffect(final TileImage image, final int color, final int steps) {
    this(image, getFilledImage(image, color), steps);
  }

  @Override
  public TileImage next() {
    if (mCurrent < mSteps) {
      ++mCurrent;
      final TileImage r = mImage1.copy();
      for (int c = 0; c < mPixelsPerStep * mCurrent; ++c) {
        int x = mPerm[c];
        final int y = x >>> 16;
        x &= 0xFFFF;
        r.setPixel(x, y, mImage2.getPixel(x, y));
      }
      return r;
    }
    return null;
  }
}
