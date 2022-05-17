package chaos.common.spell;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class BowBreakerTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new BowBreaker();
  }

  public void testCast() {
    final BowBreaker x = new BowBreaker();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(9, x.getCastRange());
    assertEquals(Attribute.RANGE, x.attribute());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    final Monster h = new RedDragon();
    h.set(Attribute.RANGE, Attribute.RANGE.max());
    h.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.RANGED_COMBAT));
    assertEquals(0, h.get(Attribute.RANGE));
    assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h, world.actor(1));
  }
}
