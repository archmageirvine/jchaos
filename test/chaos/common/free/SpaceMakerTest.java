package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.DireWolf;
import chaos.common.wizard.Wizard1;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class SpaceMakerTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new SpaceMaker();
  }

  public void test1() {
    final SpaceMaker spaceMaker = new SpaceMaker();
    assertEquals(Castable.CAST_SINGLE, spaceMaker.getCastFlags());
    final World world = new World(5, 5);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(3);
    world.getCell(12).push(w);
    final DireWolf direWolf = new DireWolf();
    direWolf.setOwner(3);
    world.getCell(13).push(direWolf);
    spaceMaker.cast(world, w, world.getCell(12));
    //System.out.println(IntelligentMeditationCasterTest.dumpWorld(world));
    // Dire wolf should end up pushed either top right or bottom right
    int dw = 0;
    if (world.actor(4) instanceof DireWolf) {
      ++dw;
    }
    if (world.actor(24) instanceof DireWolf) {
      ++dw;
    }
    assertEquals(1, dw);
    spaceMaker.cast(null, null, null);
    spaceMaker.cast(world, null, null);
    spaceMaker.cast(null, w, null);
  }
}
