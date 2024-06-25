package irvine.util.graphics;

import java.util.Random;

import irvine.tile.TileImage;

/**
 * Produce stippled images with particular colours.  The images are deterministic in the
 * sense that asking for an image for the same region will produce the same image, and
 * that subimages of a larger region will match the corresponding larger region.
 * @author Sean A. Irvine
 */
public final class Stipple {

  private Stipple() {
  }

  private static final Random RANDOM = new Random();
  private static final long DEFAULT_SEED = 42;

  /** Default stipple colors. */
  static final int[] BGC = {
    0xFF000080,
    0xFF000050,
  };

  private static long mix(long a) {
    a ^= a << 21;
    a ^= a >>> 35;
    a ^= a << 4;
    return a;
  }

  /**
   * Deterministic random noise in the plane.  Given the same set of parameters this
   * method always returns the same result.
   * @param seed a seed value
   * @param x x-coordinate
   * @param y y-coordinate
   * @param colors color values
   * @return the selected color
   */
  public static int getColor(final long seed, final int x, final int y, final int... colors) {
    final long mix = mix(x) + Long.rotateLeft(mix(y), 32) + 0xCAFEBABE;
    RANDOM.setSeed(mix(mix) ^ seed);
    return colors[RANDOM.nextInt(colors.length)];
  }

  /**
   * Return a random stippled image of the specified size starting at the specified coordinates.
   * @param seed seed value
   * @param x x-coordinate offset
   * @param y y-coordinate offset
   * @param w width of image
   * @param h height of image
   * @param colors colors to stipple with
   * @return the stippled image
   */
  public static TileImage stipple(final long seed, final int x, final int y, final int w, final int h, final int... colors) {
    final TileImage i = new TileImage(w, h);
    for (int row = 0; row < h; ++row) {
      for (int col = 0; col < w; ++col) {
        i.setPixel(col, row, getColor(seed, x + col, y + row, colors));
      }
    }
    return i;
  }

  /**
   * Return a random stippled image of the specified size starting at the specified coordinates.
   * @param x x-coordinate offset
   * @param y y-coordinate offset
   * @param w width of image
   * @param h height of image
   * @param colors colors to stipple with
   * @return the stippled image
   */
  public static TileImage stipple(final int x, final int y, final int w, final int h, final int... colors) {
    return stipple(DEFAULT_SEED, x, y, w, h, colors);
  }

  /**
   * Return a random stippled image of given size in default colors.
   * @param x x-coordinate offset
   * @param y y-coordinate offset
   * @param w width of image
   * @param h height of image
   * @return the stippled image
   */
  public static TileImage stipple(final int x, final int y, final int w, final int h) {
    return stipple(x, y, w, h, BGC);
  }
}
