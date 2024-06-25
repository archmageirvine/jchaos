package chaos.graphics.active32;

import chaos.graphics.AbstractActiveTileManagerTest;
import chaos.graphics.TileManager;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class Active32TileManagerTest extends AbstractActiveTileManagerTest {

  @Override
  protected TileManager getTileManager() {
    return new Active32TileManager();
  }

  @Override
  protected int getWidth() {
    return 32;
  }

}
