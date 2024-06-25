package irvine.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import irvine.math.IntegerUtils;

/**
 * Routines for maintaining a bunch of tiles packed into a smaller number of
 * larger images.  Supports square tiles with pixel widths of 8, 16, 32, 64,
 * 128, or 256.  Multiple tiles are crammed into actual image files that are
 * 512 by 256 pixels.  The system automatically creates and reads as many of
 * these larger files as necessary.
 *
 * @author Sean A. Irvine
 */
public class TileSet {

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

  private final String mDirectory;

  /**
   * Construct a tile set object for images with width and height given by
   * 2^bits and stored in the specified directory.
   *
   * @param bits bit width
   * @param directory directory for directory holding image files
   * @exception NullPointerException if <code>directory</code> is null.
   * @exception IllegalArgumentException if <code>bits</code> is less than
   * 3 or more than 8.
   */
  public TileSet(final int bits, final String directory) {
    if (bits < 3 || bits > 8) {
      throw new IllegalArgumentException("bad bits");
    }
    if (directory == null) {
      throw new NullPointerException();
    }
    mDirectory = directory;
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
      // get image
      try {
        r = ImageIO.read(new File(mDirectory, String.valueOf(block)));
      } catch (final IIOException e) {
        r = new BufferedImage(1 << (mWidthBits + X_BITS), 1 << (mWidthBits + 4), BufferedImage.TYPE_INT_ARGB);
      }
      mCache.put(block, r);
    }
    return r;
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
   * Set the image to associate with a given index.  Causes the block to
   * be flushed to disk afterwards.
   *
   * @param index index
   * @param image image
   * @exception IOException if an I/O problem occurs
   */
  public void setImage(final int index, final BufferedImage image) throws IOException {
    final BufferedImage i = getBlock(index >>> BLOCK_BITS);
    final int z = index & BLOCK_MASK;
    final Graphics g = i.getGraphics();
    // first make black to make sure alpha is done properly
    g.setColor(Color.BLACK);
    final int x = (z & X_MASK) << mWidthBits;
    final int y = (z >>> X_BITS) << mWidthBits;
    g.fillRect(x, y, mWidth, mWidth);
    g.drawImage(image, x, y, null);
    g.dispose();
    ImageIO.write(i, "PNG", new File(new File(mDirectory), String.valueOf(index >>> BLOCK_BITS)));
  }

  /**
   * Attempt to release any held memory.
   */
  public void flush() {
    mCache.clear();
  }

  // Warning there are scripts and makefiles that depend on this
  /**
   * Retrieve or set specific images in the Chaos tile set.
   *
   * @param args image number
   * @exception Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    // Usage: TileSet [-s] index file.png [bits]
    if ("-s".equals(args[0])) {
      final int bits = args.length > 3 ? Integer.parseInt(args[3]) : 4;
      final TileSet ts = new TileSet(bits, System.getProperty("user.home") + "/jchaos/src/chaos/graphics/active" + (1 << bits) + "/");
      final int index = IntegerUtils.parseValue(args[1]);
      final BufferedImage bi = ImageIO.read(new File(args[2]));
      ts.setImage(index, bi);
    } else {
      final int bits = args.length > 2 ? Integer.parseInt(args[2]) : 4;
      final TileSet ts = new TileSet(bits, System.getProperty("user.home") + "/jchaos/src/chaos/graphics/active" + (1 << bits) + "/");
      final int index = IntegerUtils.parseValue(args[0]);
      final BufferedImage i = ts.getImage(index);
      ImageIO.write(i, "PNG", new File(args[1]));
    }
  }
}
