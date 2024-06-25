package chaos.graphics;

/**
 * Provides access to a set of tiles.  Given an arbitrary castable
 * will produce an image to be used to represent the castable.
 * @author Sean A. Irvine
 */
public abstract class AbstractTileManager implements TileManager {

  @Override
  public int getWidth() {
    return 1 << getWidthBits();
  }

}
