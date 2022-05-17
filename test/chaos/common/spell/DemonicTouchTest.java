package chaos.common.spell;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class DemonicTouchTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new DemonicTouch();
  }

  public void testCast() {
    final DemonicTouch x = new DemonicTouch();
    assertEquals(2, x.getMultiplicity());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(11, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    final Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.COMBAT));
    assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h, world.actor(1));
  }
}
