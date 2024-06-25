package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class LevelTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Level();
  }

  public void testCast() {
    final Level x = new Level();
    assertEquals(0, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    world.getCell(1).push(w);
    castAndListenCheck(x, world, w, 1, CellEffectType.REDRAW_CELL, CellEffectType.POWERUP);
    assertEquals(1, w.get(PowerUps.LEVEL));
    x.cast(world, w, world.getCell(1));
    assertEquals(2, w.get(PowerUps.LEVEL));
    nullParametersCastCheck(x, world, w);
  }
}
