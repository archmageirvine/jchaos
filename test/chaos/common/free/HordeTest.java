package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class HordeTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Horde();
  }

  public void test1() {
    final Horde horde = new Horde();
    final World world = new World(8, 8);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(7);
    final Cell wc = world.getCell(4, 4);
    wc.push(wiz);
    final int c = wc.getCellNumber();
    final Lion l = new Lion();
    l.setState(State.DEAD);
    world.getCell(4, 3).push(l);
    horde.cast(world, wiz, wc);
    final Class<? extends Monster> expected = horde.getSpawn().getClass();
    for (int k = 0; k < world.size(); ++k) {
      final Actor actor = world.actor(k);
      if (k == c) {
        assertEquals(wiz, actor);
      } else if (world.getSquaredDistance(c, k) < 13) {
        assertEquals(expected, actor.getClass());
        assertEquals(7, actor.getOwner());
      } else {
//        if (actor != null) {
//          System.out.println(actor.toString() + " " + expected + world.getSquaredDistance(c, k));
//        }
//        for (int j = 0; j < world.size(); ++j) {
//          System.out.print(world.actor(j) == null ? "." : "X");
//          if (j % 8 == 7) {
//            System.out.println();
//          }
//        }
        assertNull(actor);
      }
    }
  }
}
