package chaos.graphics.active64;

import chaos.graphics.AbstractActiveTileManagerTest;
import chaos.graphics.TileManager;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class Active64TileManagerTest extends AbstractActiveTileManagerTest {

  @Override
  protected TileManager getTileManager() {
    return new Active64TileManager();
  }

  @Override
  protected int getWidth() {
    return 64;
  }

}
