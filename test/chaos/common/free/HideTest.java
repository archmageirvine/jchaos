package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class HideTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Hide();
  }

  public void test1() {
    final Hide a = new Hide();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    assertEquals(w, world.actor(0));
    a.cast(world, w, world.getCell(0));
    assertNull(world.actor(0));
    for (int i = 0; i < 5; ++i) {
      world.getWarpSpace().warpIn(world);
    }
    int wc = 0;
    for (int j = 0; j < world.size(); ++j) {
      if (world.actor(j) == w) {
        ++wc;
      }
    }
    assertEquals(1, wc);
  }
}
