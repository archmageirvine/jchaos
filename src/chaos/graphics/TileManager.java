package chaos.graphics;

import java.awt.image.BufferedImage;

import chaos.common.Castable;

/**
 * Provides access to a set of tiles.  Given an arbitrary castable
 * will produce an image to be used to represent the castable.
 * @author Sean A. Irvine
 */
public interface TileManager {

  /**
   * Get an image for the specified castable.
   * @param c castable to get image for
   * @param x cell <code>x</code>-coordinate
   * @param y cell <code>y</code>-coordinate
   * @param context four-direction context
   * @return the image
   */
  BufferedImage getTile(Castable c, int x, int y, final int context);

  /**
   * Get an image for the specified castable suitable for a spell list.
   * @param c castable to get image for
   * @return the image
   */
  BufferedImage getSpellTile(Castable c);

  /**
   * Get the number of bits in the width of a tile.
   * @return tile width in bits
   */
  int getWidthBits();

  /**
   * Get the width of the tile set.
   * @return width
   */
  int getWidth();

}
