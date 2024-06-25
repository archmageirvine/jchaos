package chaos.selector;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.State;
import chaos.common.wizard.Wizard1;

/**
 * Tests this selector.
 * @author Sean A. Irvine
 */
public class RankerTest extends AbstractSelectorTest {

  @Override
  public Selector getSelector() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    return new Ranker(wiz1, w, new CastMaster(w));
  }
}

