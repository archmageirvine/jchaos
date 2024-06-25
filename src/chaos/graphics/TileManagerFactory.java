package chaos.graphics;

/**
 * Factory to get named tile managers.
 * @author Sean A. Irvine
 */
public final class TileManagerFactory {

  /** Prevent instantiation of this class. */
  private TileManagerFactory() {
  }

  /** 32 by 32 animated tiles. */
  public static final String ACTIVE32 = "chaos.graphics.active32.Active32TileManager";
  /** 16 by 16 animated tiles. */
  public static final String ACTIVE16 = "chaos.graphics.active16.Active16TileManager";

  /**
   * Get the tile manager with the specified name.
   * @param name tile manager to instantiate
   * @return the tile manager
   */
  public static TileManager getTileManager(final String name) {
    // this should be smarter and use reflection to find the manager
    if (ACTIVE32.equals(name)) {
      return new chaos.graphics.active32.Active32TileManager();
    } else if (ACTIVE16.equals(name)) {
      return new chaos.graphics.active16.Active16TileManager();
    } else {
      throw new IllegalArgumentException("Unknown tile manager: " + name);
    }
  }

}
