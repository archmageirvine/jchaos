package chaos.common;

import chaos.board.World;
import chaos.common.free.AbstractFreeCastableTest;
import chaos.common.free.Arborist;
import chaos.common.free.Level;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class FreeCastableTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Arborist();
  }

  public void testCast() {
    final Level x = new Level();
    assertEquals(0, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(1));
    assertEquals(1, w.get(PowerUps.LEVEL));
    x.cast(world, w, world.getCell(1));
    assertEquals(2, w.get(PowerUps.LEVEL));
    x.cast(world, w, null);
    x.cast(world, null, world.getCell(0));
    x.cast(null, w, world.getCell(0));
  }

}
