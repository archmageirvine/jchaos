package irvine.simplegames;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Load an image from a resource.
 *
 * @author Sean A. Irvine
 */
public final class ImageLoader {

  private ImageLoader() { }

  /**
   * Load an image from a resource. It is assumed this will work,
   * and I/O error is converted into a <code>RuntimeException</code>.
   *
   * @param resource resource to load from
   * @return resulting image
   */
  public static BufferedImage load(final String resource) {
    try {
      try (final InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(resource)) {
        return ImageIO.read(is);
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
