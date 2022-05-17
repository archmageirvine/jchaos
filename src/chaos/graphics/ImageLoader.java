package chaos.graphics;

import irvine.util.graphics.Stipple;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Convenience for loading images and certain other predefined images.
 *
 * @author Sean A. Irvine
 */
public final class ImageLoader {

  private ImageLoader() { }

  /**
   * Retrieve a named image resource.
   * @param resource image resource
   * @return image
   */
  public static BufferedImage getImage(final String resource) {
    try (final InputStream is = ImageLoader.class.getClassLoader().getResourceAsStream(resource)) {
      if (is == null) {
        return null;
      }
      return ImageIO.read(is);
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final Polygon UP = new Polygon(new int[] {6, 12, 0}, new int[] {0, 6, 6}, 3);
  private static BufferedImage sUpArrow = null;

  /**
   * Return a small upwards pointing arrow with stippled background.
   * @return up arrow
   */
  public static BufferedImage upArrow() {
    if (sUpArrow == null) {
      final BufferedImage upArrow = Stipple.stipple(0, 0, 12, 6).toBufferedImage();
      final Graphics g = upArrow.getGraphics();
      if (g != null) {
        g.setColor(Color.CYAN);
        g.fillPolygon(UP);
        g.dispose();
      }
      sUpArrow = upArrow;
    }
    return sUpArrow;
  }

  private static final Polygon DOWN = new Polygon(new int[] {1, 11, 6}, new int[] {0, 0, 6}, 3);
  private static BufferedImage sDownArrow = null;

  /**
   * Return a small downwards pointing arrow with stippled background.
   * @return down arrow
   */
  public static BufferedImage downArrow() {
    if (sDownArrow == null) {
      final BufferedImage downArrow = Stipple.stipple(0, 0, 12, 6).toBufferedImage();
      final Graphics g = downArrow.getGraphics();
      if (g != null) {
        g.setColor(Color.CYAN);
        g.fillPolygon(DOWN);
        g.dispose();
      }
      sDownArrow = downArrow;
    }
    return sDownArrow;
  }

  private static BufferedImage imageFromArray(final int width, final int[] argb) {
    final BufferedImage overlay = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
    overlay.setRGB(0, 0, width, width, argb, 0, width);
    return overlay;
  }

  static BufferedImage translucentOverlay(final int width, final int rgb) {
    final int[] argb = new int[width * width];
    Arrays.fill(argb, 0x60000000 | rgb);
    return imageFromArray(width, argb);
  }

  static BufferedImage translucentOverlayWithBorder(final int width, final int rgbMiddle, final int rgbEdge) {
    final int[] argb = new int[width * width];
    Arrays.fill(argb, 0x60000000 | rgbMiddle);
    final int c = 0x60000000 | rgbEdge;
    for (int k = 0; k < width; ++k) {
      argb[k] = c;
      argb[argb.length - 1 - k] = c;
      argb[k * width] =  c;
      argb[(k + 1) * width - 1] =  c;
    }
    return imageFromArray(width, argb);
  }
}
