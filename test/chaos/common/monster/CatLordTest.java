package chaos.common.monster;

import chaos.board.World;
import chaos.common.AbstractPolycasterTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.wizard.Wizard1;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class CatLordTest extends AbstractPolycasterTest {

  @Override
  public Castable getCastable() {
    return new CatLord();
  }

  public void testMultiCat() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    final CatLord cl = new CatLord();
    cl.setOwner(2);
    w.getCell(0).push(cl);
    w.getCell(1).push(cl);
    final CatLord cl2 = new CatLord();
    cl2.cast(w, wiz, w.getCell(2), w.getCell(1));
    assertNull(w.actor(2));
  }
}
