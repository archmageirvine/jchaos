package chaos.common.free;

import chaos.board.Team;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class TormentTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Torment();
  }

  void check(final FreeCastable x, final PowerUps power) {
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    final Wizard2 w2 = new Wizard2();
    final World world = new World(1, 3, new Team());
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getWizardManager().setWizard(2, w2);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    world.getCell(2).push(w2);
    world.getTeamInformation().setTeam(w2.getOwner(), w.getOwner());
    castAndListenCheck(x, world, w, 1, CellEffectType.REDRAW_CELL, CellEffectType.AUDIO, CellEffectType.POWERUP);
    assertEquals(0, w.get(power));
    assertEquals(1, w1.get(power));
    assertEquals(0, w2.get(power));
    nullParametersCastCheck(x, world, w);
  }

  public void testCast() {
    check(new Torment(), PowerUps.TORMENT);
  }

}
