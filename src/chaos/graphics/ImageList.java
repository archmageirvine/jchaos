package chaos.graphics;

import irvine.util.Pair;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A list of images obtained via a resource.
 * @author Sean A. Irvine
 */
public final class ImageList extends ArrayList<BufferedImage> {

  private final BufferedImage mMask;

  private ImageList(final String baseName, final int bits) {
    final String base = "chaos/graphics/active" + (1 << bits) + "/" + baseName;
    BufferedImage image;
    while ((image = ImageLoader.getImage(base + size() + ".png")) != null) {
      add(image);
    }
    mMask = ImageLoader.getImage(base + "_mask.png"); // ok for this to be null
  }

  /**
   * Return a masking image for this sequence.
   * @return mask image
   */
  public BufferedImage getMask() {
    return mMask;
  }

  private static final HashMap<Pair<String, Integer>, ImageList> CACHE = new HashMap<>();

  /**
   * Return the specified image list.
   * @param baseName name of the list
   * @param bits pixel bits to get
   * @return image list
   */
  public static ImageList getList(final String baseName, final int bits) {
    final Pair<String, Integer> key = new Pair<>(baseName, bits);
    final ImageList value = CACHE.get(key);
    if (value != null) {
      return value;
    }
    final ImageList create = new ImageList(baseName, bits);
    CACHE.put(key, create);
    return create;
  }
}
