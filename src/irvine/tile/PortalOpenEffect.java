package irvine.tile;

import irvine.util.graphics.BufferedImageUtils;

/**
 * Starting with a gray scale form of the given image.  Gradually reveal the (color) form of
 * the image by expanding outwards in a column-wise manner from the center.  If the image
 * is width <code>w</code> it will take <code>w/2</code> steps to reveal.
 * @author Sean A. Irvine
 */
public class PortalOpenEffect extends AbstractTileEffect {

  private final TileImage mImage;
  private int mCurrent = -1;

  /**
   * Construct a portal opening effect.
   * @param image underlying image
   * @throws NullPointerException if <code>image</code> is <code>null</code>.
   */
  public PortalOpenEffect(final TileImage image) {
    if (image == null) {
      throw new NullPointerException();
    }
    mImage = image;
  }

  private TileImage gray(final int gap) {
    final int w = mImage.getWidth();
    if (2 * gap >= w - 1) {
      return mImage;
    }
    final int h = mImage.getHeight();
    final TileImage r = mImage.copy();
    final int lim = w / 2 - gap;
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < lim; ++x) {
        r.setPixel(x, y, BufferedImageUtils.makeGray(mImage.getPixel(x, y)));
        r.setPixel(w - x - 1, y, BufferedImageUtils.makeGray(mImage.getPixel(w - x - 1, y)));
      }
    }
    return r;
  }

  @Override
  public TileImage next() {
    if (2 * ++mCurrent > mImage.getWidth()) {
      return null;
    }
    return gray(mCurrent);
  }
}
