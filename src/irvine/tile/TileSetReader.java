package irvine.tile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Like <code>TileSet</code> but provides for read only access from a source
 * specified as a system resource.
 *
 * @author Sean A. Irvine
 */
public class TileSetReader {

  private static final int X_BITS = 5;
  private static final int X_MASK = (1 << X_BITS) - 1;
  private static final int BLOCK_BITS = 9;
  private static final int BLOCK_MASK = (1 << BLOCK_BITS) - 1;

  /** Width of tiles in bits. */
  private final int mWidthBits;
  /** Width of tile images. */
  private final int mWidth;
  /** Cache of blocks. */
  private final HashMap<Integer, BufferedImage> mCache = new HashMap<>();

  private final String mResource;

  /**
   * Construct a tile set object for images with width and height given by
   * 2^bits and stored in the specified resource.
   *
   * @param bits bit width
   * @param resource resource for resource holding image files
   * @exception NullPointerException if <code>resource</code> is null.
   * @exception IllegalArgumentException if <code>bits</code> is less than
   * 3 or more than 8.
   */
  public TileSetReader(final int bits, final String resource) {
    if (bits < 3 || bits > 8) {
      throw new IllegalArgumentException("bad bits");
    }
    if (resource == null) {
      throw new NullPointerException();
    }
    if (resource.endsWith("/")) {
      mResource = resource;
    } else {
      mResource = resource + "/";
    }
    mWidthBits = bits;
    mWidth = 1 << bits;
  }

  /**
   * Retrieve a block of images, using the cached version if possible.
   *
   * @param block block to retrieve
   * @return the block
   * @exception IOException if an I/O problem occurs
   */
  private BufferedImage getBlock(final int block) throws IOException {
    BufferedImage r = mCache.get(block);
    if (r == null) {
      final String resource = mResource + block;
      final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
      if (is == null) {
        throw new IOException("Failed to get " + resource);
      }
      try {
        r = ImageIO.read(is);
        mCache.put(block, r);
      } finally {
        is.close();
      }
    }
    return r;
  }

  public int getWidth() {
    return mWidth;
  }

  /**
   * Retrieve the image with the given index.  Asking for images beyond
   * those currently defined can result in either an empty image or
   * null.
   *
   * @param index image index
   * @return the image
   * @exception IOException if an I/O problem occurs
   */
  public BufferedImage getImage(final int index) throws IOException {
    final BufferedImage i = getBlock(index >>> BLOCK_BITS);
    if (i != null) {
      final int z = index & BLOCK_MASK;
      return i.getSubimage((z & X_MASK) << mWidthBits, (z >>> X_BITS) << mWidthBits, mWidth, mWidth);
    }
    return null;
  }

  /**
   * Attempt to release any held memory.
   */
  public void flush() {
    mCache.clear();
  }
}
