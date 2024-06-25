package irvine.tile;

import java.util.HashMap;

/**
 * A graphical effect for an attack.
 * @author Sean A. Irvine
 */
public class AttackEffect extends AbstractTileEffect {

  /** Cache of character images. */
  private static final HashMap<String, TileImage> CACHE = new HashMap<>();

  /**
   * Get a black and white image for the given character at the given size.
   * The resulting image will have height size and a width determined by
   * size / phi, where phi is the golden ratio.  The pixels of the background
   * are white and of the character are black.  Results are cached so that
   * images can be returned quickly on subsequent calls.
   * @param s string to draw
   * @param size height of image
   * @return black and white image of character
   * @throws IllegalArgumentException if <code>size</code> is less than 8
   * or the string is longer than 2 characters.
   */
  private static TileImage getStringImage(final String s, final int size) {
    if (s == null) {
      return null;
    }
    if (s.length() > 2) {
      throw new IllegalArgumentException();
    }
    final String sc = s + "_" + size;
    TileImage ci = CACHE.get(sc);
    if (ci == null) {
      switch (s.length()) {
        case 1:
          ci = ImageUtils.getCharImage(s.charAt(0), size);
          break;
        case 2:
          final TileImage left = getStringImage(s.substring(0, 1), size);
          final TileImage right = getStringImage(s.substring(1), size);
          final int lw = left.getWidth() - 1;
          ci = new TileImage(lw + right.getWidth(), left.getHeight()).jam(0, 0, left).over(lw, 0, right);
          break;
        default:
          // handle empty string
          ci = new TileImage(size, size);
          ci.fill(~0);
          break;
      }
      CACHE.put(sc, ci);
    }
    return ci;
  }

  /** Width of effect. */
  private final int mWidth;

  private int mCurrent = 0;

  /** The text image to jam on top of the effect. */
  private final TileImage mTextImage;
  /** Background color. */
  private final int mBg;
  /** Foreground color. */
  private final int mFg;
  /** Emphasis color. */
  private final int mEmph;
  /** Number of steps. */
  private final int mSteps;

  /**
   * Construct an attack effect.<p>
   *
   * The effect supports the display of an optional string of up to two characters
   * in length.  If no string is to be displayed <code>null</code> can be used.
   * @param w width of effect
   * @param s string to display in effect
   * @param bg background color for effect
   * @param fg primary foreground color for effect
   * @param tc color for the text
   * @param ec emphasis color for star boundary
   * @param steps number of steps
   * @throws IllegalArgumentException if the length of the string exceeds 2 or
   * the number of steps is negative.
   */
  public AttackEffect(final int w, final String s, final int bg, final int fg, final int tc, final int ec, final int steps) {
    if (w < 16) {
      throw new IllegalArgumentException("bad width");
    }
    if (steps < 0) {
      throw new IllegalArgumentException("bad steps");
    }
    mWidth = w;
    mSteps = steps;
    mBg = bg;
    mFg = fg;
    mEmph = ec;
    mTextImage = getStringImage(s, Math.max(6, w / 3)).copy().replaceColor(0xFF000000, tc).replaceColor(~0, 0);
  }

  /** Tangent of 27 degrees. */
  private static final double TAN_ANGLE = 0.50952;

  /** Set a pixel value, even if coordinates are outside the image. */
  private static void setPixel(final TileImage i, final int x, final int y, final int color) {
    if (x >= 0 && y >= 0 && x < i.getWidth() && y < i.getHeight()) {
      i.setPixel(x, y, color);
    }
  }

  private static void drawFourPointStar(final TileImage r, final int w, final int color) {
    final int ihw = r.getWidth() / 2;
    final int dw = (int) (w * TAN_ANGLE / 2 + 0.5);
    for (int i = 0; i <= dw; ++i) {
      final int y = (int) ((dw - i) / TAN_ANGLE + 0.5) - 1;
      for (int j = 0; j <= y; ++j) {
        setPixel(r, ihw - i, ihw - j, color);
        setPixel(r, ihw + i, ihw - j, color);
        setPixel(r, ihw - i, ihw + j, color);
        setPixel(r, ihw + i, ihw + j, color);
        setPixel(r, ihw - j, ihw - i, color);
        setPixel(r, ihw + j, ihw - i, color);
        setPixel(r, ihw - j, ihw + i, color);
        setPixel(r, ihw + j, ihw + i, color);
      }
    }
  }

  /** Color emphasis at boundary pixels. */
  private void emphasize(final TileImage r, final int c1, final int c2, final int c3) {
    final int w = r.getWidth();
    for (int y = 0; y < w; ++y) {
      for (int x = 1, p = r.getPixel(0, y); x < w - 1; ++x) {
        final int q = r.getPixel(x + 1, y);
        if (p == c1 && q == c2) {
          r.setPixel(x + 1, y, c3);
        } else if (p == c2 && q == c1) {
          r.setPixel(x, y, c3);
        }
        p = q;
      }
    }
    for (int x = 0; x < w; ++x) {
      for (int y = 1, p = r.getPixel(0, x); y < w - 1; ++y) {
        final int q = r.getPixel(x, y + 1);
        if (p == c1 && q == c2) {
          r.setPixel(x, y + 1, c3);
        } else if (p == c2 && q == c1) {
          r.setPixel(x, y, c3);
        }
        p = q;
      }
    }
  }

  /** Cached star-point images. */
  private TileImage mImageA = null;
  private TileImage mImageB = null;

  /** First form of the image. */
  private TileImage getImageA() {
    if (mImageA == null) {
      mImageA = new TileImage(mWidth, mWidth);
      mImageA.fill(mBg);
      drawFourPointStar(mImageA, mWidth - (mWidth >>> 2), mFg);
      mImageA = mImageA.rotate(45, mBg, false);
      drawFourPointStar(mImageA, mWidth + 1, mFg);
      emphasize(mImageA, mBg, mFg, mEmph);
    }
    return mImageA.textureColor(mFg, 5);
  }

  /** Second form of the image. */
  private TileImage getImageB() {
    if (mImageB == null) {
      mImageB = new TileImage(mWidth, mWidth);
      mImageB.fill(mBg);
      drawFourPointStar(mImageB, mWidth + 2, mFg);
      mImageB = mImageB.rotate(45, mBg, false);
      drawFourPointStar(mImageB, mWidth - (mWidth >>> 2), mFg);
      emphasize(mImageB, mBg, mFg, mEmph);
    }
    return mImageB.textureColor(mFg, 5);
  }

  @Override
  public TileImage next() {
    if (mCurrent < mSteps) {
      ++mCurrent;
      return ((mCurrent & 1) == 0 ? getImageA() : getImageB()).over(mWidth / 2 - mTextImage.getWidth() / 2, mWidth / 2 - mTextImage.getHeight() / 2 + 1, mTextImage);
    }
    return null;
  }
}
