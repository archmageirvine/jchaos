package chaos.common.spell;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class DiseaseTest extends AbstractDecrementTest {


  @Override
  public Castable getCastable() {
    return new Disease();
  }

  public void testCast() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(8, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    final Actor h = new Horse();
    h.set(Attribute.LIFE_RECOVERY, 10);
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(2, h.get(Attribute.LIFE_RECOVERY));
    assertEquals(h, world.actor(1));
  }
}
