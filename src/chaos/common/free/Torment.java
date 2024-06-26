package chaos.common.free;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Torment.
 * @author Sean A. Irvine
 */
public class Torment extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      final Team t = world.getTeamInformation();
      for (final Wizard w : world.getWizardManager().getWizards()) {
        if (w != null && t.getTeam(w) != t.getTeam(caster)) {
          w.increment(PowerUps.TORMENT);
        }
      }
    }
    // Always do the effect on the caster
    if (casterCell != null) {
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.POWERUP));
      casterCell.notify(new AudioEvent(casterCell, caster, "torment"));
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
    }
  }
}
