package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class WizardWingsTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new WizardWings();
  }

  public void testWWSpecial() {
    final WizardWings x = new WizardWings();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    x.cast(world, w, world.getCell(0));
    assertEquals(1, w.get(PowerUps.FLYING));
    assertTrue(w.is(PowerUps.FLYING));
  }
}
