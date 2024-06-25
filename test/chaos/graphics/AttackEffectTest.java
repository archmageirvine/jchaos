package chaos.graphics;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.dragon.RedDragon;
import chaos.common.monster.Lion;
import chaos.util.CombatUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AttackEffectTest extends TestCase {

  public void test1() {
    final World w = new World(5, 4);
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 16);
    assertEquals("drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#highlight(null)#", screen.toString());
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
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals("drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#", screen.toString());
  }

  public void test3() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>(new CellComparator());
    c.add(w.getCell(0));
    c.add(w.getCell(19));
    w.getCell(19).push(new Lion());
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 2, CombatUtils.RANGED);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals("drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#", screen.toString());
  }

  public void test4() {
    final World w = new World(5, 4);
    w.getCell(19).push(new RedDragon());
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), w.getCell(19), 16);
    assertEquals("highlight(realms)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#highlight(null)#", screen.toString());
  }

  public void test5() {
    final World w = new World(5, 4);
    w.getCell(19).push(new RedDragon());
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.RANGED);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), w.getCell(19), 16);
    assertEquals("highlight(realms)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#highlight(null)#", screen.toString());
  }

  public void test6() {
    final World w = new World(5, 4);
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null, 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), w.getCell(19), 16);
    assertEquals("drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#drawCell(4,3)#highlight(null)#", screen.toString());
  }

  public void test7() {
    final World w = new World(5, 4);
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), null, 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.NORMAL);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertTrue(System.currentTimeMillis() - t < 50);
    assertEquals("", screen.toString());
  }

  private static final float[] VOLS = {
    0.5F,
    0.533333F,
    0.566666F,
    0.599999F,
    0.633332F,
    0.666665F,
    0.699998F,
    0.73333097F,
    0.766664F,
    0.799997F,
    0.83333004F,
    0.866663F,
    0.899996F,
    0.933329F,
    0.966662F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
    1.0F,
  };

  public void testSelectVolume() {
    final World w = new World(5, 4);
    final AttackEffect se = new AttackEffect(w, TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), new Lion(), 1, CombatUtils.NORMAL);
    for (int k = -30; k < 30; ++k) {
      final float vol = se.selectVolume(k);
      //System.err.println(vol + ",");
      assertTrue(Math.abs(VOLS[Math.abs(k)] - vol) < 0.000001);
    }
  }

}
