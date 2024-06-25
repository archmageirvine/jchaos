package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class PointsTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Points();
  }

  public void test1() {
    final Points a = new Points();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    assertEquals(200, w.getScore());
    castAndListenCheck(a, world, w, 0, CellEffectType.AUDIO);
    assertEquals(300, w.getScore());
  }
}
