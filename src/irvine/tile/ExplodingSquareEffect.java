package irvine.tile;

/**
 * A graphical effect in which a square expands from the centre to fill
 * the tile, then disappears from the centre outwards.
 * @author Sean A. Irvine
 */
public class ExplodingSquareEffect extends AbstractTileEffect {

  /** Current state. */
  private int mCurrent = 0;
  /** Image width in pixels. */
  private final int mWidth;
  /** Background color. */
  private final int mBack;
  /** Foreground colors. */
  private final int[] mFore;

  /**
   * A new exploding square effect using the given colors.  Each of the
   * foreground colors is used in turn for consecutive squares during
   * the expansion phase.  They are all eventually replaced with the
   * background color.  A square of a single color can be produced by
   * passing a single color in the <code>fore</code> array.
   * @param width width of tile
   * @param back background color
   * @param fore foreground colors
   * @throws IllegalArgumentException if <code>width</code> is less than 2
   * or odd.
   * or <code>fore</code> has length 0.
   * @throws NullPointerException if <code>fore</code> is null.
   */
  public ExplodingSquareEffect(final int width, final int back, final int[] fore) {
    if ((width & 0x80000001) != 0 || width == 0) {
      throw new IllegalArgumentException("Bad width.");
    }
    if (fore.length == 0) {
      throw new IllegalArgumentException("Bad colors.");
    }
    mWidth = width;
    mBack = back;
    mFore = new int[fore.length];
    System.arraycopy(fore, 0, mFore, 0, fore.length);
  }

  @Override
  public TileImage next() {
    if (mCurrent < mWidth - 1) {
      final TileImage image = new TileImage(mWidth, mWidth);
      image.fill(mBack);
      final int halfw = (mWidth >> 1) - 1;
      for (int i = Math.max(0, mCurrent - halfw); i <= Math.min(mCurrent, halfw); ++i) {
        final int lim = i << 1;
        final int c = mFore[i % mFore.length];
        final int d = halfw - i;
        final int e = halfw + i + 1;
        for (int j = 0; j <= lim; ++j) {
          image.setPixel(d + j, d, c);
          image.setPixel(d, d + j + 1, c);
          image.setPixel(d + j + 1, e, c);
          image.setPixel(e, d + j, c);
        }
      }
      ++mCurrent;
      return image;
    }
    return null;
  }
}
