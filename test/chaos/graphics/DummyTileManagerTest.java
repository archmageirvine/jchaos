package chaos.graphics;

import chaos.common.Castable;
import junit.framework.TestCase;

import java.awt.image.BufferedImage;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DummyTileManagerTest extends TestCase {

  private static class MyManager extends AbstractTileManager {
    @Override
    public BufferedImage getTile(final Castable c, final int x, final int y, final int context) {
      return null;
    }

    @Override
    public BufferedImage getSpellTile(final Castable c) {
      return null;
    }

    @Override
    public int getWidthBits() {
      return 2;
    }
  }

  public void test() {
    assertEquals(4, new MyManager().getWidth());
  }
}
