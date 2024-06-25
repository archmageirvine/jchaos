package chaos.selector;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.State;
import chaos.common.free.Arborist;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this selector.
 * @author Sean A. Irvine
 */
public class CreaturologistTest extends AbstractSelectorTest {

  @Override
  public Selector getSelector() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    return new Creaturologist(wiz1, w, new CastMaster(w));
  }

  public void testGetScore() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    final Creaturologist c = new Creaturologist(wiz1, w, new CastMaster(w));
    final int[] s = new int[OrdinarySelector.MAX_STAT];
    s[OrdinarySelector.EMPTY] = 1;
    assertEquals(1000, c.getScore(new Lion(), s, w.getCell(2)));
    assertTrue(c.getScore(new Arborist(), s, w.getCell(2)) < 1000);
  }

}

