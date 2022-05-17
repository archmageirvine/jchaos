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
public class CurseTest extends AbstractFreeDecrementTest {

  @Override
  public Castable getCastable() {
    return new Curse();
  }

  public void test1() {
    final Curse a = new Curse();
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(2);
    l.set(Attribute.LIFE, 4);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(2, l.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, l.getState());
    a.cast(world, w, null);
    assertEquals(0, l.get(Attribute.LIFE));
    assertEquals(State.DEAD, l.getState());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
    final StoneGiant g = new StoneGiant();
    g.set(Attribute.LIFE, 2);
    world.getCell(0).push(g);
    a.cast(world, w, null);
    assertEquals(State.DEAD, g.getState());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
  }
}
