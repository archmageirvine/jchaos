package chaos.graphics;

import java.util.TreeSet;

import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.board.World;
import chaos.util.ChaosProperties;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class BombEffectTest extends TestCase {

  public void test1() {
    ChaosProperties.properties().setProperty("chaos.bomb.decay", "100");
    final World w = new World(5, 4);
    final BombEffect se = new BombEffect(w, 0xFFFFE030, null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 8);
    assertEquals("", screen.toString());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 8);
    assertTrue(screen.toString().startsWith("|I(8,0,null)#I(8,0,null)#I(8,0,null)#I(8,0,null)#I(8,0,null)#I(8,0,null)#I(8,0,null)#I(8,0,null)#"));
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final BombEffect se = new BombEffect(w, 0xFFFFE030, null);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 8);
    assertTrue(System.currentTimeMillis() - t < 50);
    assertEquals("", screen.toString());
  }

}
