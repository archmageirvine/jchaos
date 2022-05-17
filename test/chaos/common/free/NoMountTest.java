package chaos.common.free;

import chaos.board.Team;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class NoMountTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new NoMount();
  }

  public void test1() {
    final World world = new World(7, 1);
    final Wizard w = world.getWizardManager().getWizard(3);
    w.setState(State.ACTIVE);
    final Horse h = new Horse();
    h.setOwner(w.getOwner());
    h.setMount(w);
    world.getCell(0).push(h);
    final Wizard w4 = world.getWizardManager().getWizard(4);
    w4.setOwner(4);
    w4.setState(State.ACTIVE);
    world.getCell(6).push(w4);
    final Team teamInformation = world.getTeamInformation();
    teamInformation.separate(4);
    new NoMount().cast(world, w4, world.getCell(6));
    assertEquals(null, h.getMount());
    assertEquals(w, world.actor(1));
    assertEquals(w4, world.actor(6));
    assertEquals(6, w.get(PowerUps.NO_MOUNT));
    assertEquals(0, w4.get(PowerUps.NO_MOUNT));
  }

  public void testWithRide() {
    final World world = new World(7, 1);
    final Wizard w = world.getWizardManager().getWizard(3);
    w.setState(State.ACTIVE);
    w.set(PowerUps.RIDE, 1);
    final Horse h = new Horse();
    h.setOwner(w.getOwner());
    h.setMount(w);
    world.getCell(0).push(h);
    final Wizard w4 = world.getWizardManager().getWizard(4);
    w4.setOwner(4);
    final Team teamInformation = world.getTeamInformation();
    teamInformation.separate(4);
    new NoMount().cast(world, w4, world.getCell(6));
    assertEquals(0, w.get(PowerUps.NO_MOUNT));
  }
}
