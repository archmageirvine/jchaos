package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class CountermandTest extends AbstractFreeDecrementTest {

  @Override
  public Castable getCastable() {
    return new Countermand();
  }

  public void test1() {
    final Countermand a = new Countermand();
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(2);
    l.set(Attribute.AGILITY, 12);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(2, l.get(Attribute.AGILITY));
    assertEquals(State.ACTIVE, l.getState());
    a.cast(world, w, null);
    assertEquals(0, l.get(Attribute.AGILITY));
  }
}
