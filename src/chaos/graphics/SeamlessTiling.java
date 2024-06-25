package chaos.graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import irvine.simplegames.ImageLoader;
import irvine.util.Point;

/**
 * Seamless tiling of an image.
 * @author Sean A. Irvine
 */
public final class SeamlessTiling {

  private final BufferedImage mImage;
  private final int mWidth;
  private final HashMap<Point, BufferedImage> mCache = new HashMap<>();

  SeamlessTiling(final String resource) {
    mImage = ImageLoader.load(resource);
    mWidth = mImage.getWidth(null);
  }

  BufferedImage getTile(final int x, final int y, final int bits) {
    assert mWidth == 512;
    final int px = (x << bits) % mWidth;
    final int py = (y << bits) % mWidth;
    final Point key = new Point(px, py);
    final BufferedImage res = mCache.get(key);
    if (res != null) {
      return res;
    }
    final int w = 1 << bits;
    final BufferedImage sub = mImage.getSubimage(px, py, w, w);
    mCache.put(key, sub);
    return sub;
  }

}
