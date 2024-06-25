package irvine.tile;

/**
 * An effect that fades one image into another.
 * @author Sean A. Irvine
 */
public class FadeEffect extends AbstractTileEffect {

  /** Mask for red and blue color fields. */
  private static final int RB = 0xFF00FF;
  /** Mask for alpha and green color fields. */
  private static final int AG = 0xFF00FF00;
  /** The original image. */
  private final TileImage mImage1;
  /** The target image. */
  private final TileImage mImage2;
  /** The current step number. */
  private int mCurrent = 0;

  /**
   * Fade the first image into the second image.  This transformation always
   * takes exactly 8 steps but could be modified for other lengths if required.
   * @param image1 first image
   * @param image2 second image
   * @throws NullPointerException if either image is null.
   * @throws IllegalArgumentException if the images have different sizes
   */
  public FadeEffect(final TileImage image1, final TileImage image2) {
    if (image1.getWidth() != image2.getWidth()
      || image1.getHeight() != image2.getHeight()) {
      throw new IllegalArgumentException("Size mismatch");
    }
    mImage1 = image1;
    mImage2 = image2;
  }

  /**
   * Fade the first image to the given color.  This transformation always
   * takes exactly eight steps but could be modified for other lengths if required.
   * @param image initial image
   * @param color target color
   * @throws NullPointerException if the image is null.
   */
  public FadeEffect(final TileImage image, final int color) {
    mImage2 = new TileImage(image.getWidth(), image.getHeight());
    mImage2.fill(color);
    mImage1 = image;
  }

  @Override
  public TileImage next() {
    if (mCurrent < 8) {
      ++mCurrent;
      final int w = mImage1.getWidth();
      final int h = mImage1.getHeight();
      final TileImage r = new TileImage(w, h);
      for (int y = 0; y < h; ++y) {
        for (int x = 0; x < w; ++x) {
          final int a = mImage1.getPixel(x, y);
          final int b = mImage2.getPixel(x, y);
          final int rb = (((a & RB) * (8 - mCurrent) + (b & RB) * mCurrent) >>> 3) & RB;
          final int ag = ((((a >>> 8) & RB) * (8 - mCurrent)
            + ((b >>> 8) & RB) * mCurrent) << 5) & AG;
          r.setPixel(x, y, rb | ag);
        }
      }
      return r;
    }
    return null;
  }
}
