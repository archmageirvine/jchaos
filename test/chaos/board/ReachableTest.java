package chaos.board;

import java.util.Set;

import chaos.common.inanimate.MagicGlass;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ReachableTest extends TestCase {

  public void testReachable0() {
    final World world = new World(10, 10, new Team());
    final Reachable reachable = new Reachable(world);
    assertEquals(4, reachable.getReachableFlying(25, 1).size());
    assertEquals(8, reachable.getReachableFlying(25, 1.5).size());
    assertEquals(3, reachable.getReachableFlying(0, 1.5).size());
    assertEquals(99, reachable.getReachableFlying(0, 100).size());
    for (int k = 0; k < world.height(); k++) {
      world.getCell(5, k).push(new MagicGlass());
    }
    assertEquals(59, reachable.getReachableFlying(0, 100).size());
  }

  public void testReachable1() {
    final World world = new World(5, 5, new Team());
    final Reachable reachable = new Reachable(world);
    world.getCell(2).push(new MagicGlass());
    world.getCell(7).push(new MagicGlass());
    world.getCell(8).push(new MagicGlass());
    world.getCell(9).push(new MagicGlass());
    world.getCell(16).push(new MagicGlass());
    world.getCell(17).push(new MagicGlass());
    world.getCell(22).push(new MagicGlass());
    final Set<Cell> r = reachable.getReachableFlying(11, 3);
    assertEquals(r.toString(), 17, r.size());
  }
}
