package chaos.graphics.active16;

import chaos.graphics.AbstractActiveTileManagerTest;
import chaos.graphics.TileManager;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class Active16TileManagerTest extends AbstractActiveTileManagerTest {

  @Override
  protected TileManager getTileManager() {
    return new Active16TileManager();
  }

  @Override
  protected int getWidth() {
    return 16;
  }

}
