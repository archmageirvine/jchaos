package chaos.common.free;

import chaos.board.Team;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class CoercionTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Coercion();
  }

  public void testCast() {
    final Coercion x = new Coercion();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    final World world = new World(1, 2, new Team());
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    castAndListenCheck(x, world, w, 1, CellEffectType.POWERUP, CellEffectType.REDRAW_CELL);
    assertEquals(0, w.get(PowerUps.COERCION));
    assertEquals(1, w1.get(PowerUps.COERCION));
    nullParametersCastCheck(x, world, w);
  }

}
