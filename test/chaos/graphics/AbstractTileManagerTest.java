package chaos.graphics;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Caster;
import junit.framework.TestCase;

import java.awt.image.BufferedImage;

/**
 * Tests basic functionality that all TileManagers should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractTileManagerTest extends TestCase {

  protected TileManager mTileManager = null;

  // implemented in subclasses to provide objects
  protected abstract TileManager getTileManager();

  @Override
  public void setUp() {
    mTileManager = getTileManager();
  }

  @Override
  public void tearDown() {
    mTileManager = null;
  }

  /**
   * Test width is reasonable
   */
  public void testWidth() {
    assertTrue(mTileManager.getWidth() > 0);
    assertEquals(1 << mTileManager.getWidthBits(), mTileManager.getWidth());
  }

  public void testNull() {
    assertNull(mTileManager.getTile(null, 0, 0, 0));
  }

  private static class MyCastable extends Castable {
    @Override
    public int getCastFlags() {
      return 0;
    }
    @Override
    public int getCastRange() {
      return 5;
    }
    @Override
    public void cast(final World w, final Caster c, final Cell cc, final Cell ccc) { }
  }

  public void testImagery() {
    // attempt to get image for make believe castable
    final BufferedImage unk = mTileManager.getTile(new MyCastable(), 0, 0, 0);
    assertNotNull(unk);
    assertEquals(mTileManager.getWidth(), unk.getWidth());
    assertEquals(mTileManager.getWidth(), unk.getHeight());
  }

}
