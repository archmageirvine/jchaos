package chaos.engine;

import chaos.board.Team;
import chaos.board.World;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Pit;
import chaos.common.monster.GoblinBomb;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class WizardFieldTest extends TestCase {

  public void test() {
    final World world = new World(5, 5, new Team());
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    world.getCell(2, 2).push(wiz);
    world.getCell(3, 4).push(new GoblinBomb());
    world.getCell(4, 3).push(new Pit());
    world.getCell(1, 3).push(new MagicWood());
    final WizardField field = new WizardField(world, wiz);
    assertEquals(0.0, field.weight(2));
    assertEquals(0.0128333, field.weight(12), 1E-4);
    assertEquals(0.0063, field.weight(19), 1E-4);
    assertEquals(0.0, field.weight(25));
  }
}
