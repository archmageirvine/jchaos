package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.DireWolf;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class ForceSlamTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new ForceSlam();
  }

  public void test1() {
    final ForceSlam forceSlam = new ForceSlam();
    assertEquals(Castable.CAST_SINGLE, forceSlam.getCastFlags());
    final World world = new World(5, 5);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(3);
    world.getCell(12).push(w);
    final Horse horse = new Horse();
    world.getCell(5).push(horse);
    final Lion lion = new Lion();
    lion.setState(State.DEAD);
    world.getCell(3).push(lion);
    final DireWolf direWolf = new DireWolf();
    direWolf.setOwner(3);
    world.getCell(13).push(direWolf);
    forceSlam.cast(world, w, world.getCell(12));
    //System.out.println(IntelligentMeditationCasterTest.dumpWorld(world));
    assertEquals(horse, world.actor(0));
    assertEquals(lion, world.actor(4));
    assertEquals(direWolf, world.actor(13));
    forceSlam.cast(null, null, null);
    forceSlam.cast(world, null, null);
    forceSlam.cast(null, w, null);
  }
}
