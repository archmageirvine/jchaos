package chaos.graphics;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class OwnerChangeEffectTest extends TestCase {

  String expected() {
    return "drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#";
  }

  AbstractEffect effect(final World w) {
    return new OwnerChangeEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16));
  }

  public void test1() {
    final World w = new World(5, 4);
    final AbstractEffect se = effect(w);
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
    final AbstractEffect se = effect(w);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals(expected(), screen.toString());
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final AbstractEffect se = effect(w);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertTrue(System.currentTimeMillis() - t < 50);
    assertEquals("", screen.toString());
  }

}
