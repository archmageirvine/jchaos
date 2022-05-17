package chaos.graphics;

import chaos.common.Castable;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DrawCastingTilesTest extends TestCase {

  public void test() {
    final MockGraphics g = new MockGraphics();
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE32);
    DrawCastingTiles.drawCastableTiles(new Wizard1(), tm, g, 32, 0, 0, 0);
    assertEquals("", g.toString());
    DrawCastingTiles.drawCastableTiles(new Wizard1(), tm, g, 32, 0, 0, Castable.CAST_DEAD | Castable.CAST_LOS | Castable.CAST_INANIMATE | Castable.CAST_MUSTBEUNDEAD);
    assertEquals("I(0,0,null)#I(32,0,null)#I(64,0,null)#", g.toString());
  }
}
