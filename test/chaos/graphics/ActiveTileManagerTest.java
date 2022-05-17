package chaos.graphics;

import chaos.graphics.active16.Active16TileManager;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ActiveTileManagerTest extends AbstractActiveTileManagerTest {

  @Override
  protected TileManager getTileManager() {
    return new Active16TileManager();
  }

  @Override
  protected int getWidth() {
    return 16;
  }
}
