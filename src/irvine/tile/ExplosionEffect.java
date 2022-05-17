package irvine.tile;

import java.util.HashMap;

/**
 * An effect that explodes a square image sending pixels outwards and eventually
 * outside of the tile.
 *
 * @author Sean A. Irvine
 */
public class ExplosionEffect extends AbstractTileEffect {

  /** Cache the vector fields, as needed, for each image size. */
  private static final HashMap<Integer, TileImage> CACHE = new HashMap<>();

  /** Current image. */
  private TileImage mImage;
  /** Background color. */
  private final int mBg;
  /** Dust style explosion when false. */
  private final boolean mDust;

  /**
   * Precompute and cache the vector field for a given image size.  The vector
   * field itself is represented as an image with the high 16 bits given the
   * y-coordinate and the low 16-bits the x-coordinate of the new pixel that
   * the given pixel will move to.  Pixels at the edge have value -1 indicating
   * that they disappear off the image.  The main part of the calculation is
   * done for one octant, and the result reflected by symmetry to the other
   * octants.
   *
   * @param width image size
   * @return vector field
   */
  static synchronized TileImage getVectorField(final int width) {
    TileImage r = CACHE.get(width);
    if (r == null) {
      r = new TileImage(width, width);
      r.fill(-1);
      final int w = width - 1;
      final int ww = width / 2;
      for (int y = ww; y > 0; --y) {
        r.setPixel(y, y, ((y - 1) << 16) + y - 1);
        r.setPixel(w - y, y, ((y - 1) << 16) + w - y + 1);
        r.setPixel(y, w - y, ((w - y + 1) << 16) + y - 1);
        r.setPixel(w - y, w - y, ((w - y + 1) << 16) + w - y + 1);
        for (int x = y - 1, dx = ww - y + 1; x > 0; --x, ++dx) {
          final int ny = ww - ((dx + 1) * (ww - y) + dx / 2) / dx;
          r.setPixel(x, y, (ny << 16) + x - 1);
          r.setPixel(w - x, y, (ny << 16) + w - x + 1);
          r.setPixel(x, w - y, ((w - ny) << 16) + x - 1);
          r.setPixel(w - x, w - y, ((w - ny) << 16) + w - x + 1);
          r.setPixel(y, x, ((x - 1) << 16) + ny);
          r.setPixel(w - y, x, ((x - 1) << 16) + w - ny);
          r.setPixel(y, w - x, ((w - x + 1) << 16) + ny);
          r.setPixel(w - y, w - x, ((w - x + 1) << 16) + w - ny);

        }
      }
      CACHE.put(width, r); // add it to the cache
    }
    return r;
  }

  /**
   * Construct a new explosion effect.  The background color is used to fill
   * otherwise unset pixels and in dust mode is used to decide if a pixel can
   * be moved at all.  If <code>dust</code> is false, then all pixels always
   * move according to the vector field, otherwise they move only if the
   * destination pixel in the current image is the background color.
   *
   * @param baseImage image to be exploded
   * @param bg background color
   * @param dust controls when a pixel can move
   * @exception NullPointerException if <code>baseImage</code> is null.
   * @exception IllegalArgumentException if either dimension of
   * <code>baseImage</code> exceeds 32767 or if either dimension has
   * an odd size.
   */
  public ExplosionEffect(final TileImage baseImage, final int bg, final boolean dust) {
    final int w = baseImage.getWidth();
    if (w != baseImage.getHeight() || (w & ~0xFFFE) != 0) {
      throw new IllegalArgumentException("bad dimension");
    }
    mImage = baseImage.copy();
    mBg = bg;
    mDust = !dust;
  }

  @Override
  public TileImage next() {
    if (mImage != null) {
      final int w = mImage.getWidth();
      final TileImage vectorField = getVectorField(w);
      final TileImage r = new TileImage(w, w);
      r.fill(mBg);
      boolean ok = false;
      for (int y = 1; y < w - 1; ++y) {
        for (int x = 1; x < w - 1; ++x) {
          final int p = mImage.getPixel(x, y);
          if (p != mBg) {
            ok = true; // new image has at least one non-bg pixel
            final int t = vectorField.getPixel(x, y);
            final int nx = t & 0xFFFF;
            final int ny = t >>> 16;
            if (mDust || mImage.getPixel(nx, ny) == mBg) {
              r.setPixel(nx, ny, p);
            } else {
              r.setPixel(x, y, p);
            }
          }
        }
      }
      mImage = ok ? r : null;
    }
    return mImage;
  }
}
