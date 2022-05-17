package chaos.common.spell;

import java.util.HashSet;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class RequestTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Request();
  }

  public void testCast() {
    final World world = new World(1, 2);
    final Request a = new Request();
    assertEquals(1, a.getCastRange());
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY, a.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    world.getCell(0).push(w);
    final HashSet<Class<? extends Actor>> cl = new HashSet<>();
    for (int i = 0; i < 5000; ++i) {
      world.getCell(1).pop();
      a.cast(world, w, world.getCell(1), world.getCell(0));
      final Actor act = world.getCell(1).peek();
      assertEquals(act.getOwner(), w.getOwner());
      assertTrue(act.getClass().toString(), act instanceof Monster);
      cl.add(act.getClass());
    }
    assertTrue(cl.size() > 50);
  }
}
