package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Elm;
import chaos.common.inanimate.WeakWall;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class HasteTest extends AbstractIncrementTest {

  @Override
  public Castable getCastable() {
    return new Haste();
  }

  public void testTree() {
    final Haste a = new Haste();
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    final Cell cc = world.getCell(1);
    cc.push(w);
    final Elm elm = new Elm();
    elm.setOwner(3);
    world.getCell(0).push(elm);
    final WeakWall ww = new WeakWall();
    ww.setOwner(3);
    world.getCell(1).push(ww);
    a.cast(world, w, cc);
    assertEquals(0, elm.get(Attribute.MOVEMENT));
    assertEquals(0, ww.get(Attribute.MOVEMENT));
  }
}
