package chaos.common.free;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PowerUpEvent;

/**
 * Coercion.
 * @author Sean A. Irvine
 */
public class Coercion extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      final Team t = world.getTeamInformation();
      for (final Wizard w : world.getWizardManager().getWizards()) {
        if (w != null && w != caster && t.getTeam(w) != t.getTeam(caster)) {
          w.set(PowerUps.COERCION, 1);
        }
      }
    }
    // Always do the effect on the caster
    if (casterCell != null) {
      casterCell.notify(new PowerUpEvent(casterCell, caster, PowerUps.COERCION));
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
    }
  }
}
