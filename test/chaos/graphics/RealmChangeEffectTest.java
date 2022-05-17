package chaos.graphics;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import junit.framework.TestCase;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.monster.Lion;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RealmChangeEffectTest extends TestCase {

  public void test1() {
    final World w = new World(5, 4);
    final RealmChangeEffect se = new RealmChangeEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 16);
    assertEquals("", screen.toString());
  }

  private static class CellComparator implements Comparator<Cell>, Serializable {
    @Override
    public int compare(final Cell a, final Cell b) {
      return a.getCellNumber() - b.getCellNumber();
    }
  }

  public void test2() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>(new CellComparator());
    c.add(w.getCell(0));
    c.add(w.getCell(19));
    w.getCell(19).push(new Lion());
    final RealmChangeEffect se = new RealmChangeEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals("drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#", screen.toString());
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final RealmChangeEffect se = new RealmChangeEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertTrue(System.currentTimeMillis() - t < 50);
    assertEquals("", screen.toString());
  }

}
