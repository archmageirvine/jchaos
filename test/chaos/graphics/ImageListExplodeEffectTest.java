package chaos.graphics;

import chaos.board.World;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ImageListExplodeEffectTest extends TestCase {

  public void test() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen(new MockGraphics());
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    final Animator a = new Animator(w, screen, tm);
    final ImageListExplodeEffect effect = new ImageListExplodeEffect(w, a, ImageList.getList("fireball", 4), null, 0);
    effect.performEffect(screen, screen.getGraphics(), w.getCell(1), 16);
    final String s = screen.toString();
    assertTrue(s.startsWith("fillMain(java.awt.Color[r=0,g=0,b=0])#"));
    assertTrue(s.contains("drawCell(1,1)#"));
    assertTrue(s.contains("drawCell(1,0)#"));
    assertTrue(s.contains("drawCell(0,1)#"));
    assertTrue(s.contains("drawCell(0,0)#"));
    assertTrue(s.contains("I(15,0,null)"));
    assertTrue(s.contains("I(14,0,null)"));
    assertTrue(s.contains("I(15,1,null)"));
    assertTrue(s.contains("I(14,2,null)"));
    assertTrue(s.contains("I(2,0,null)"));
    assertTrue(s.contains("I(1,0,null)"));
    assertTrue(s.contains("I(2,14,null)"));
    assertTrue(s.contains("I(1,15,null)"));
  }
}
