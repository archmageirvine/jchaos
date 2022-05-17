package irvine.util.graphics;

import java.awt.image.BufferedImage;
import java.io.PrintStream;

import irvine.util.string.PostScript;

/**
 * Various utility functions for manipulating images of the class
 * <code>java.awt.image.BufferedImage</code>.
 *
 * @author Sean A. Irvine
 */
public final class BufferedImageUtils {

  private BufferedImageUtils() { }

  /**
   * Make a copy of an image.
   * @param image image to copy
   * @return copy
   */
  public static BufferedImage copy(final BufferedImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, image.getRGB(0, 0, w, h, null, 0, w), 0, w);
    return result;
  }

  /**
   * Write an Encapsulated PostScript (EPS) version of the given image to the
   * output stream.  The image is expanded and optionally gridded.  The purpose is for
   * the display of small icon or tile like images, so that the individual
   * pixel detail can be seen.
   * @param image image to write
   * @param out stream to write to
   * @param grid whether or not to write a grid on top of the image
   * @exception NullPointerException if either argument is null
   */
  public static void writeEps(final BufferedImage image, final PrintStream out, final boolean grid) {
    final int w = image.getWidth();
    final int h = image.getHeight();

    PostScript.header(out, BufferedImageUtils.class, "image", 10 * w, 10 * h);
    out.println("10 10 scale");
    out.println(".05 setlinewidth");

    // draw all the cells
    for (int x = 0; x < w; ++x) {
      for (int y = 0; y < h; ++y) {
        final int c = image.getRGB(x, y);
        out.println(((c >> 16) & 0xFF) / 256.0 + " " + ((c >> 8) & 0xFF) / 256.0 + " " + (c & 0xFF) / 256.0 + " setrgbcolor");
        out.println("newpath");
        out.println(x + " " + (h - y - 1) + " moveto");
        out.println((x + 1) + " " + (h - y - 1) + " lineto");
        out.println((x + 1) + " " + (h - y) + " lineto");
        out.println(x + " " + (h - y) + " lineto");
        out.println("closepath fill");
      }
    }

    if (grid) {
      // overlay grid
      out.println(".5 .5 .5 setrgbcolor");
      for (int x = 0; x <= w; ++x) {
        for (int y = 0; y <= h; ++y) {
          out.println("newpath " + x + " 0 moveto " + x + " " + h + " lineto stroke");
          out.println("newpath 0 " + y + " moveto " + w + " " + y + " lineto stroke");
        }
      }
    }
    PostScript.trailer(out);
  }

  /**
   * Construct a new buffered image which is a darker version of the supplied
   * image. Roughly speaking, each pixel will be 50% darker. The output image
   * type is <code>TYPE_INT_ARGB</code>.
   * @param image image to darken
   * @return darkened image
   */
  public static BufferedImage darken(final BufferedImage image) {
    // In theory this should be accomplishable with something like
    // return new RescaleOp(0.5F, 0, null).filter(image, null);
    // but this seems to reliably crash the JVM and Linux and Windows
    // especially with older JVMs. Therefore ...
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    for (int k = 0; k < argb.length; ++k) {
      // Fully opaque remains fully opaque and fully transparent remains
      // fully transparent. Otherwise each color component is set to
      // half intensity.
      argb[k] = (argb[k] >> 1) & 0xFF7F7F7F;
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Construct a new buffered image which is a redder version of the supplied
   * image. The output image type is <code>TYPE_INT_ARGB</code>.
   * @param image image to redden
   * @param scale scaling (bigger than 1)
   * @return redden image
   * @exception IllegalArgumentException if scale is less than 1.
   */
  public static BufferedImage redden(final BufferedImage image, final double scale) {
    if (scale < 1) {
      throw new IllegalArgumentException();
    }
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    final double inv = 1.0 / scale;
    for (int k = 0; k < argb.length; ++k) {
      final int v = argb[k];
      final int alpha = v & 0xFF000000;
      final int r = (v >>> 16) & 0xFF;
      final int red = Math.min((int) (r * scale), 0xFF) << 16;
      final int g = (v >> 8) & 0xFF;
      final int green = (int) (g * inv) << 8;
      final int b = v & 0xFF;
      final int blue = (int) (b * inv);
      argb[k] = alpha | red | green | blue;
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Construct a new buffered image which is a bluer version of the supplied
   * image. The output image type is <code>TYPE_INT_ARGB</code>.
   * @param image image to make bluer
   * @param scale scaling (bigger than 1)
   * @return bluer image
   * @exception IllegalArgumentException if scale is less than 1.
   */
  public static BufferedImage bluen(final BufferedImage image, final double scale) {
    if (scale < 1) {
      throw new IllegalArgumentException();
    }
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    final double inv = 1.0 / scale;
    for (int k = 0; k < argb.length; ++k) {
      final int v = argb[k];
      final int alpha = v & 0xFF000000;
      final int r = (v >>> 16) & 0xFF;
      final int red = (int) (r * inv) << 16;
      final int g = (v >> 8) & 0xFF;
      final int green = (int) (g * inv) << 8;
      final int b = v & 0xFF;
      final int blue = Math.min((int) (b * scale), 0xFF);
      argb[k] = alpha | red | green | blue;
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Convert a color pixel value to gray scale.
   * @param argb colour pixel
   * @return gray scale version
   */
  public static int makeGray(final int argb) {
    final int alpha = argb & 0xFF000000;
    final int r = (argb >>> 16) & 0xFF;
    final int g = (argb >> 8) & 0xFF;
    final int b = argb & 0xFF;
    final int c = (r + g + b) / 3;
    return alpha | (c << 16) | (c << 8) | c;
  }

  /**
   * Convert to gray scale image
   * @param image image to make gray scale
   * @return gray scale image
   */
  public static BufferedImage grayScale(final BufferedImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    for (int k = 0; k < argb.length; ++k) {
      argb[k] = makeGray(argb[k]);
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Delete all the blue pigmentation from an image.
   * The output image type is <code>TYPE_INT_ARGB</code>.
   * @param image image to make bluer
   * @return no blue image
   */
  public static BufferedImage removeBlue(final BufferedImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    for (int k = 0; k < argb.length; ++k) {
      argb[k] &= 0xFFFFFF00;
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Construct a new buffered image which replaces one colour.
   * @param image original image
   * @param currentColor current color
   * @param replacementColor replacement color
   * @return new image
   */
  public static BufferedImage replace(final BufferedImage image, final int currentColor, final int replacementColor) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int c = currentColor & 0xFFFFFF;
    final int r = replacementColor & 0xFFFFFF;
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    for (int k = 0; k < argb.length; ++k) {
      final int v = argb[k];
      if ((v & 0xFFFFFF) == c) {
        argb[k] = r | (v & 0xFF000000); // preserves alpha
      }
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, argb, 0, w);
    return result;
  }

  /**
   * Flip the image left-to-right.
   * @param image image to flip
   * @return the horizontally flipped image
   */
  public static BufferedImage flip(final BufferedImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    final int[] argb = image.getRGB(0, 0, w, h, null, 0, w);
    final int[] flip = new int[argb.length];
    for (int x = 0; x < w; ++x) {
      for (int y = 0; y < h; ++y) {
        flip[y * w + x] = argb[y * w + w - 1 - x];
      }
    }
    final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    result.setRGB(0, 0, w, h, flip, 0, w);
    return result;
  }

}
