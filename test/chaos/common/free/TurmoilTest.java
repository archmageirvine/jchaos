package chaos.common.free;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class TurmoilTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Turmoil();
  }

  public void test1() {
    boolean lionNonZero = false;
    for (int k = 0; k < 10; ++k) {
      final Turmoil a = new Turmoil();
      final World world = new World(3, 3);
      final Wizard1 w = new Wizard1();
      w.setState(State.ACTIVE);
      w.setOwner(7);
      world.getCell(4).push(w);
      final Lion l = new Lion();
      l.setState(State.DEAD);
      world.getCell(0).push(l);
      a.cast(world, w, world.getCell(4));
      boolean sawLion = false;
      boolean sawWiz = false;
      for (int i = 0; i < world.size(); ++i) {
        final Actor ac = world.actor(i);
        if (ac == l) {
          assertFalse(sawLion);
          sawLion = true;
          if (i != 0) {
            lionNonZero = true;
          }
        } else if (ac == w) {
          assertFalse(sawWiz);
          sawWiz = true;
        }
      }
      assertTrue(sawLion);
      assertTrue(sawWiz);
      a.cast(null, null, null);
      a.cast(world, null, null);
      a.cast(null, w, null);
    }
    assertTrue(lionNonZero);
  }

}
