package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class StupidityTest extends AbstractFreeDecrementTest {

  @Override
  public Castable getCastable() {
    return new Stupidity();
  }

  public void test1() {
    final Stupidity a = new Stupidity();
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(2);
    l.set(Attribute.INTELLIGENCE, 3);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(1, l.get(Attribute.INTELLIGENCE));
    assertEquals(State.ACTIVE, l.getState());
    a.cast(world, w, null);
    assertEquals(0, l.get(Attribute.INTELLIGENCE));
    assertEquals(State.DEAD, l.getState());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
    final StoneGiant g = new StoneGiant();
    g.set(Attribute.INTELLIGENCE, 1);
    world.getCell(0).push(g);
    a.cast(world, w, null);
    assertEquals(State.DEAD, g.getState());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
  }
}
