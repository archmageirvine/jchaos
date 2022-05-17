package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Rock;
import chaos.common.monster.Orc;
import chaos.common.wizard.Wizard;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class FleshToStoneTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new FleshToStone();
  }

  public void testCast() {
    final World world = new World(7, 2);
    final FleshToStone a = new FleshToStone();
    assertEquals(7, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_NOWIZARDCELL, a.getCastFlags());
    final Wizard w = world.getWizardManager().getWizard(1);
    w.setState(State.ACTIVE);
    world.getCell(6).push(w);
    final Orc orc = new Orc();
    orc.setOwner(w.getOwner() + 1);
    world.getCell(2).push(orc);
    a.cast(world, w, world.getCell(2), world.getCell(6));
    assertTrue(world.actor(2) instanceof Rock);
  }
}
