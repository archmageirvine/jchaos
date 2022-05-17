package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Restoration.
 *
 * @author Sean A. Irvine
 */
public class Restoration extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_NOWIZARDCELL;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      cell.notify(new CellEffectEvent(cell, CellEffectType.SHIELD_GRANTED));
      final Actor a = cell.pop();
      if (a != null && a.getState() != State.DEAD && !(a instanceof Wizard)) {
        // create a new instance
        final Actor newa = (Actor) FrequencyTable.instantiate(a.getClass());
        newa.setOwner(a.getOwner());
        cell.push(newa);
      }
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepSickest(CastUtils.dropWizards(CastUtils.keepFriends(targets, t, teams)));
  }
}
