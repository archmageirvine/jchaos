package irvine.tile;

import static java.lang.Math.PI;
import static java.lang.Math.min;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;

/**
 * A lightweight image structure without all the baggage of the standard
 * Java image classes.  Provides a simple &alpha;RGB value per pixel.
 * The data can be accessed directly for maximum speed or converted to
 * a standard Java BufferedImage.
 *
 * @author Sean A. Irvine
 */
public class TileImage {

  /** Width of the image. */
  private final int mWidth;

  /** Height of the image. */
  private final int mHeight;

  /** Underlying pixel data. */
  private final int[] mAlphaRGB;

  /**
   * Construct a new opaque black image of the given dimensions.
   *
   * @param width width in pixels
   * @param height height in pixels
   * @exception IllegalArgumentException if either dimension is less than 1.
   */
  public TileImage(final int width, final int height) {
    if (width < 1) {
      throw new IllegalArgumentException("width must be at least 1");
    }
    if (height < 1) {
      throw new IllegalArgumentException("height must be at least 1");
    }
    mWidth = width;
    mHeight = height;
    mAlphaRGB = new int[mWidth * mHeight];
  }

  /**
   * Construct a new image for a buffered image.
   *
   * @param image the image
   * @exception NullPointerException if <code>raster</code> is <code>null</code>.
   */
  public TileImage(final BufferedImage image) {
    mWidth = image.getWidth();
    mHeight = image.getHeight();
    mAlphaRGB = image.getRGB(0, 0, mWidth, mHeight, new int[mWidth * mHeight], 0, mWidth);
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof TileImage) {
      final TileImage i = (TileImage) o;
      if (i.mWidth != mWidth || i.mHeight != mHeight) {
        return false;
      }
      for (int j = 0; j < mAlphaRGB.length; ++j) {
        if (mAlphaRGB[j] != i.mAlphaRGB[j]) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int r = (mWidth << 16) + mHeight;
    for (int i = 0; i < mAlphaRGB.length;) {
      r ^= mAlphaRGB[i++] * i;
    }
    return r;
  }

  /**
   * Return the width of the image in pixels.
   *
   * @return width of image
   */
  public int getWidth() {
    return mWidth;
  }

  /**
   * Return the height of the image in pixels.
   *
   * @return height of image
   */
  public int getHeight() {
    return mHeight;
  }

  /**
   * Return a copy of this image.  The copy is distinct in the sense
   * that changes pixels in the copy will not affect the original.
   * However, prior to such changes the two images are equal.
   *
   * @return copy
   */
  public TileImage copy() {
    final TileImage r = new TileImage(getWidth(), getHeight());
    System.arraycopy(mAlphaRGB, 0, r.mAlphaRGB, 0, mAlphaRGB.length);
    return r;
  }

  /**
   * Set the given pixel to the given color value.  No bounds checking is
   * performed, so this method should be used with care.
   *
   * @param x <code>x</code>-coordinate
   * @param y <code>y</code>-coordinate
   * @param argb &alpha;RGB value (8-bits per component)
   */
  public void setPixel(final int x, final int y, final int argb) {
    mAlphaRGB[y * mWidth + x] = argb;
  }

  /**
   * Get the color value of a given pixel.
   *
   * @param x <code>x</code>-coordinate
   * @param y <code>y</code>-coordinate
   * @return color value
   */
  public int getPixel(final int x, final int y) {
    return mAlphaRGB[y * mWidth + x];
  }

  /**
   * Fill the entire image with the given color.
   *
   * @param argb color
   */
  public void fill(final int argb) {
    Arrays.fill(mAlphaRGB, argb);
  }

  /**
   * Return an image which is this image flipped vertically.  That is, the
   * top row of this image is the bottom row of the returned image and
   * vice versa.
   *
   * @return the flipped image
   */
  public TileImage vFlip() {
    final int w = getWidth();
    final TileImage r = new TileImage(w, getHeight());
    for (int y = 0, z = getHeight() - 1; z >= 0; ++y, --z) {
      for (int x = 0; x < w; ++x) {
        r.setPixel(x, y, getPixel(x, z));
      }
    }
    return r;
  }

  /**
   * Return an image which is this image flipped horizontally.  That is, the
   * left row of this image is the right row of the returned image and
   * vice versa.
   *
   * @return the flipped image
   */
  public TileImage hFlip() {
    final int h = getHeight();
    final TileImage r = new TileImage(getWidth(), h);
    for (int x = 0, z = getWidth() - 1; z >= 0; ++x, --z) {
      for (int y = 0; y < h; ++y) {
        r.setPixel(x, y, getPixel(z, y));
      }
    }
    return r;
  }

  /**
   * Return this image rotated 90 degrees clockwise about the centre.  This
   * rotation is perfect in that no color modification occurs.
   *
   * @return rotated image.
   */
  public TileImage rotate90() {
    final int w = getWidth();
    final int h = getHeight();
    final TileImage r = new TileImage(h, w);
    for (int y = 0, z = h - 1; y < h; ++y, --z) {
      for (int x = 0; x < w; ++x) {
        r.setPixel(z, x, getPixel(x, y));
      }
    }
    return r;
  }

  /**
   * Return this image rotated 180 degrees about the centre.  This rotation
   * is perfect in that no color modification occurs.
   *
   * @return rotated image.
   */
  public TileImage rotate180() {
    // can be done with vflip().hflip(), but it is slightly faster
    // to do this directly especially when we can directly reverse
    // the underlying image array
    final TileImage r = new TileImage(getWidth(), getHeight());
    for (int i = 0, j = mAlphaRGB.length - 1; j >= 0; ++i, --j) {
      r.mAlphaRGB[j] = mAlphaRGB[i];
    }
    return r;
  }

  /**
   * Return this image rotated 270 degrees clockwise about the centre.  This
   * rotation is perfect in that no color modification occurs.
   *
   * @return rotated image.
   */
  public TileImage rotate270() {
    final int w = getWidth();
    final int h = getHeight();
    final TileImage r = new TileImage(h, w);
    for (int y = 0, z = w - 1; y < w; ++y, --z) {
      for (int x = 0; x < h; ++x) {
        r.setPixel(x, y, getPixel(z, x));
      }
    }
    return r;
  }

  /**
   * Extract a subimage from this image.  All the bounds must within this
   * image or an exception occurs.
   *
   * @param x x-offset
   * @param y y-offset
   * @param w width
   * @param h height
   * @return subimage
   * @exception IllegalArgumentException if any parameter would cause a
   * pixel access outside this image.
   */
  public TileImage getSubimage(final int x, int y, final int w, final int h) {
    if (x < 0 || y < 0 || w < 1 || h < 1 || x + w > getWidth() || y + h > getHeight()) {
      throw new IllegalArgumentException("bad bounds");
    }
    final TileImage r;
    if (x == 0 && y == 0 && w == getWidth() && h == getHeight()) {
      r = this;
    } else {
      r = new TileImage(w, h);
      for (int i = 0; i < h; ++i, ++y) {
        for (int j = 0, k = x; j < w; ++j, ++k) {
          r.setPixel(j, i, getPixel(k, y));
        }
      }
    }
    return r;
  }

  /** Constant to convert from degrees to radians. */
  private static final double DEGREES_TO_RADIANS = PI / 180;

  /**
   * Constant used to determine when a shear will not cause any significant
   * change to the image.  For shears less than this amount, the image is
   * returned immediately and without modification.
   */
  private static final double NEAR_PERFECT = 0.001;

  /** Mask out two color values. */
  private static final int C = 0xFF00FF;
  private static final int D = 0xFF00FF00;
  /** (128 &lt;&lt; 16) + 128, fraction scale for two colors. */
  private static final int S = 0x800080;

  /** Get a pixel value, even if coordinates are outside the image. */
  private int rotateGetPixel(final int x, final int y, final int bg) {
    return x < 0 || y < 0 || x >= getWidth() || y >= getHeight() ? bg : getPixel(x, y);
  }

  /**
   * Rotate this image by the specified angle with optional blending.
   *
   * @param angleInDegrees angle
   * @param bg color to use for any exposed pixels
   * @param blend true for blending
   * @return the rotated image
   */
  public TileImage rotate(final double angleInDegrees, final int bg, final boolean blend) {
    // get angle in the range [-360 .. 360]; then reduce to [0 .. 360]
    double theta = angleInDegrees % 360;
    if (theta < 0) {
      theta += 360;
    }
    assert theta >= 0 && theta <= 360;
    // check for a near multiple of 90 degrees and handle exactly
    final double m90 = theta % 90;
    if (m90 < NEAR_PERFECT || m90 > 90 - NEAR_PERFECT) {
      if (theta < 1) {
        return this;
      } else if (theta < 91) {
        return rotate90();
      } else if (theta < 181) {
        return rotate180();
      } else if (theta < 271) {
        return rotate270();
      } else {
        return this;
      }
    }
    theta *= DEGREES_TO_RADIANS; // convert to radians
    final float sinTheta = 16F * (float) Math.sin(theta);
    final float cosTheta = 16F * (float) Math.cos(theta);
    final int w = getWidth();
    final int h = getHeight();
    final int hw = w / 2;
    final int hh = h / 2;
    final TileImage r = new TileImage(w, h);
    if (blend) {
      for (int y = 0; y < h; ++y) {
        final int ydelta = y - hh;
        final float sy = ydelta * sinTheta;
        final float cy = ydelta * cosTheta;
        for (int x = 0; x < w; ++x) {
          final int xdelta = hw - x;
          final int xpm = (int) (sy - xdelta * cosTheta + 0.5);
          final int ypm = (int) (cy + xdelta * sinTheta + 0.5);
          final int xp = hw + (xpm >> 4);
          final int yp = hh + (ypm >> 4);
          final int xf = xpm & 0x0F;
          final int yf = ypm & 0x0F;
          final int ma = (16 - xf) * (16 - yf);
          final int mb = xf * (16 - yf);
          final int mc = (16 - xf) * yf;
          final int md = xf * yf;
          // get the four pixels
          final int a = rotateGetPixel(xp, yp, bg);
          final int b = rotateGetPixel(xp + 1, yp, bg);
          final int c = rotateGetPixel(xp, yp + 1, bg);
          final int d = rotateGetPixel(xp + 1, yp + 1, bg);
          // compute the weighted blend, because of the packing we are able to do
          // this for two color values at a time, change at your own risk
          final int redBlue = ((ma * (a & C) + mb * (b & C) + mc * (c & C) + md * (d & C) + S) & D) >>> 8;
          final int alphaGreen = (ma * ((a >>> 8) & C) + mb * ((b >>> 8) & C) + mc * ((c >>> 8) & C) + md * ((d >>> 8) & C) + S) & D;
          r.setPixel(x, y, redBlue | alphaGreen);
        }
      }
    } else {
      for (int y = 0; y < h; ++y) {
        final int ydelta = y - hh;
        final float sy = ydelta * sinTheta;
        final float cy = ydelta * cosTheta;
        for (int x = 0; x < w; ++x) {
          final int xdelta = hw - x;
          final int xpm = (int) (sy - xdelta * cosTheta + 0.5);
          final int ypm = (int) (cy + xdelta * sinTheta + 0.5);
          r.setPixel(x, y, rotateGetPixel(hw + (xpm >> 4), hh + (ypm >> 4), bg));
        }
      }
    }
    return r;
  }

  /**
   * Roll the image vertically by the indicated number of pixels.  Positive values
   * correspond to downwards rolls and negative values to upwards rolls.  Rolls
   * are interpreted modulo the image height, so it is possible to roll by
   * amounts greater than the image height and a roll of -1 and h-1 are
   * equivalent.
   *
   * @param delta amount to roll
   * @return rolled image
   */
  public TileImage vroll(int delta) {
    final int h = getHeight();
    delta %= h;
    if (delta == 0) {
      return this;
    }
    if (delta < 0) {
      delta += h;
    }
    final int w = getWidth();
    final TileImage r = new TileImage(w, h);
    final int offset = delta * w;
    System.arraycopy(mAlphaRGB, offset, r.mAlphaRGB, 0, mAlphaRGB.length - offset);
    System.arraycopy(mAlphaRGB, 0, r.mAlphaRGB, mAlphaRGB.length - offset, offset);
    return r;
  }

  /**
   * Roll the image horizontally by the indicated number of pixels.  Positive values
   * correspond to leftwards rolls and negative values to rightwards rolls.  Rolls
   * are interpreted modulo the image width, so it is possible to roll by
   * amounts greater than the image width and a roll of -1 and w-1 are
   * equivalent.
   *
   * @param delta amount to roll
   * @return rolled image
   */
  public TileImage hroll(int delta) {
    final int w = getWidth();
    delta %= w;
    if (delta == 0) {
      return this;
    }
    if (delta < 0) {
      delta += w;
    }
    final int h = getHeight();
    final TileImage r = new TileImage(w, h);
    // quickest to block copy entire image then correct certain broken pixels
    System.arraycopy(mAlphaRGB, delta, r.mAlphaRGB, 0, mAlphaRGB.length - delta);
    final int dd = w - delta;
    for (int y = 0; y < h; ++y) {
      for (int x = dd, z = 0; x < w; ++x, ++z) {
        r.setPixel(x, y, getPixel(z, y));
      }
    }
    return r;
  }

  /**
   * Replace all occurrences of a given color with another color in this image.
   *
   * @param oldColor old color
   * @param newColor new color
   * @return image with color replacement
   */
  public TileImage replaceColor(final int oldColor, final int newColor) {
    for (int i = 0; i < mAlphaRGB.length; ++i) {
      if (mAlphaRGB[i] == oldColor) {
        mAlphaRGB[i] = newColor;
      }
    }
    return this;
  }

  /**
   * Randomize the specified number of bits of any pixel of the given color in
   * this image.  This is sometimes useful to break up large block of monotonous
   * color.  Does not modify the alpha channel.
   *
   * @param color color to texture
   * @param bits number of bits to affect
   * @return image with color replacement
   * @exception IllegalArgumentException if <code>bits</code> is not in the range
   * 0 to 8.
   */
  public TileImage textureColor(final int color, final int bits) {
    if (bits > 8 || bits < 0) {
      throw new IllegalArgumentException("bad bits");
    }
    if (bits != 0) { // if for efficiency only
      final int mask = (0x00010101 << bits) - 0x00010101;
      final Random r = new Random();
      for (int i = 0; i < mAlphaRGB.length; ++i) {
        if (mAlphaRGB[i] == color) {
          mAlphaRGB[i] = color ^ (mask & r.nextInt());
        }
      }
    }
    return this;
  }

  /**
   * Jam another image into this image.  The content of <code>image</code> is used
   * to replace a rectangle of pixels in this image with top-left coordinates
   * <code>(x,y)</code>.  No account of the alpha channel is made, the pixels of
   * this image are simply replaced with those of the given image.  Any pixels
   * outside the rectangle are unmodified.  As a convenience the jammed image is
   * returned.
   *
   * @param x0 x-offset
   * @param y0 y-offset
   * @param image image to jam in
   * @return jammed image
   * @exception NullPointerException if <code>image</code> is null
   * @exception IllegalArgumentException if the bounding rectangle will exceed
   * in any way the dimensions of this image.
   */
  public TileImage jam(final int x0, final int y0, final TileImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    if (x0 < 0 || y0 < 0 || x0 + w > getWidth() || y0 + h > getHeight()) {
      throw new IllegalArgumentException("bad rectangle");
    }
    // for large images, it would be more efficient to use arraycopy on each x
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        setPixel(x0 + x, y0 + y, image.getPixel(x, y));
      }
    }
    return this;
  }

  /**
   * Return a pixel value for color <code>a</code> over color <code>b</code>.
   * //Assumes the RGB components are premultiplied by alpha.
   *
   * @param b lower color
   * @param a upper color
   * @return combined color
   */
  private static int over(final int b, final int a) {
    final int alphaa = a >>> 24;
    if (alphaa == 255) {
      // quick return if a is completely opaque
      return a;
    }
    if (alphaa == 0) {
      // quick return if a is completely transparent
      return b;
    }
    final int ma = 256 - alphaa;
    // this could be made very fast on a GPU
    final int blue = min(((a & 0xFF) * alphaa + (b & 0xFF) * ma) >>> 8, 0xFF);
    final int green = min((((a & 0xFF00) * alphaa + (b & 0xFF00) * ma) & ~0xFFFF) >>> 8, 0xFF00);
    final int red = min((((a & 0xFF0000) * alphaa + (b & 0xFF0000) * ma) & ~0xFFFFFF) >>> 8, 0xFF0000);
    final int alpha = min((alphaa * alphaa + (b >>> 24) * ma) >>> 8, 0xFF) << 24;
    return alpha | red | green | blue;
  }

  /**
   * Place the given image over this image using the alpha channel information.
   * Assumes the color values have been premultiplied by alpha. As a convenience
   * the blended image is returned.
   *
   * @param x0 x-offset
   * @param y0 y-offset
   * @param image image to over in
   * @return blend image
   * @exception NullPointerException if <code>image</code> is null
   * @exception IllegalArgumentException if the bounding rectangle will exceed
   * in any way the dimensions of this image.
   */
  public TileImage over(final int x0, final int y0, final TileImage image) {
    final int w = image.getWidth();
    final int h = image.getHeight();
    if (x0 < 0 || y0 < 0 || x0 + w > getWidth() || y0 + h > getHeight()) {
      throw new IllegalArgumentException("bad rectangle");
    }
    for (int y = 0, yo = y0; y < h; ++y, ++yo) {
      for (int x = 0, xo = x0; x < w; ++x, ++xo) {
        setPixel(xo, yo, over(getPixel(xo, yo), image.getPixel(x, y)));
      }
    }
    return this;
  }

  /**
   * Convert this image into a standard Java BufferedImage.
   *
   * @return a BufferedImage
   */
  public BufferedImage toBufferedImage() {
    final BufferedImage r = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
    r.setRGB(0, 0, getWidth(), getHeight(), mAlphaRGB, 0, getWidth());
    return r;
  }
}
