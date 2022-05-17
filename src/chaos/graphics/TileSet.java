package chaos.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import irvine.tile.TileSetReader;

/**
 * Represents a set of images to be used in animating an Actor
 * or for some other tile based animation effect.
 *
 * @author Sean A. Irvine
 */
public class TileSet {

  /** Types for tile sets. */
  public enum TileSetType {
    /** Sequential images. */
    SEQUENTIAL,
    /** Ping pong style images. */
    PING_PONG,
    /** Select one at random. */
    RANDOM_SELECT,
    /** Choose based on context in four cardinal directions. */
    FOUR_CONTEXT,
  }

  /** The images in this tile set. */
  private final BufferedImage[] mImage;
  /** The dead image (null if there is no dead image) */
  private BufferedImage mDeadImage = null;
  private TileSetType mTileSetType;

  /**
   * Construct the chaos tile set from a spec.
   *
   * @param ts generating tile set
   * @param spec spec for tiles
   * @exception IOException if an I/O problem occurs
   */
  public TileSet(final TileSetReader ts, final String spec) throws IOException {
    final String[] parts = spec.split("\\s+");
    switch (parts[0].charAt(0)) {
      case 'p':
        mTileSetType = TileSetType.PING_PONG;
        break;
      case 's':
        mTileSetType = TileSetType.RANDOM_SELECT;
        break;
      case 'q':
        mTileSetType = TileSetType.FOUR_CONTEXT;
        break;
      default:
        mTileSetType = TileSetType.SEQUENTIAL;
        break;
    }
    final String[] im = parts[1].split(",");
    mImage = new BufferedImage[im.length];
    for (int i = 0; i < im.length; ++i) {
      mImage[i] = ts.getImage(Integer.parseInt(im[i], 16));
    }
    if (mTileSetType == TileSetType.FOUR_CONTEXT && mImage.length != 16) {
      throw new IllegalArgumentException();
    }
    if (parts.length > 2) {
      mDeadImage = ts.getImage(Integer.parseInt(parts[2], 16));
    }
  }

  /**
   * Construct the chaos tile set from explicit images.
   * @param images the images
   * @param type tile set type
   */
  public TileSet(final BufferedImage[] images, final TileSetType type) {
    mImage = Arrays.copyOf(images, images.length);
    mTileSetType = type;
  }

  /**
   * Test if the frames of this image should ping-pong rather
   * than cycle.
   *
   * @return true if frames should ping-pong
   */
  public boolean isPingPong() {
    return mTileSetType == TileSetType.PING_PONG;
  }

  /**
   * True if the tile to be displayed should be randomly selected among the
   * available tiles.  For example, spider web.
   *
   * @return true for random selection
   */
  public boolean isRandomSelect() {
    return mTileSetType == TileSetType.RANDOM_SELECT;
  }

  /**
   * Get the type of this tile set.
   * @return tile set type
   */
  public TileSetType getTileSetType() {
    return mTileSetType;
  }

  /**
   * Given a frame index attempt to compute the next frame index
   * taking into account cycling or ping-pong effects. It will
   * try to be sensible even if the <code>currentFrameIndex</code>
   * is invalid.
   *
   * @param currentFrameIndex current frame index
   * @return next frame index
   */
  public int nextFrameIndex(final int currentFrameIndex) {

    // quick exit for 1 frame images
    final int size = mImage.length;
    if (size <= 1) {
      return 0;
    }

    // handle faulty input
    if (currentFrameIndex < 0) {
      return 0;
    }

    // for cyclic images this is easy
    if (!isPingPong()) {
      return (currentFrameIndex + 1) % size;
    }

    // otherwise, the tricky case of ping-pong
    // some frames have extra indices
    if (currentFrameIndex >= 2 * size - 3) {
      return 0;
    }
    return currentFrameIndex + 1;
  }

  /**
   * Return the specified image of the set. This takes into account
   * special numbering for ping-pong frames.
   *
   * @param index which image to get
   * @return the image
   */
  public BufferedImage getImage(final int index) {
    final int size = mImage.length;
    return index < size ? mImage[index] : mImage[2 * size - index - 2];
  }

  /**
   * If this TileSet contains an image of a corpse then return that
   * image, otherwise return null;
   *
   * @return the corpse image or null
   */
  public BufferedImage getDeadImage() {
    return mDeadImage;
  }

  /**
   * Return an image suitable for use in a spell table.
   *
   * @return the spell image
   */
  public BufferedImage getSpellImage() {
    return mImage[0];
  }

  /**
   * Return the total number of images in this set.
   *
   * @return number of images
   */
  public int getNumberOfImages() {
    return mImage.length;
  }

}
