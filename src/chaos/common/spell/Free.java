package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Free.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class Free extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH | CAST_INANIMATE | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 14;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null && !(a instanceof Wizard) && cell.getMount() == null) {
        if (casterCell != null) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.BRAIN_BEAM_EVENT));
        }
        if (a.getOwner() == Actor.OWNER_INDEPENDENT) {
          cell.notify(new CellEffectEvent(cell, CellEffectType.SLEEP));
          a.setOwner(0);
          a.setState(State.ASLEEP);
        } else {
          cell.notify(new CellEffectEvent(cell, CellEffectType.OWNER_CHANGE));
          a.setOwner(Actor.OWNER_INDEPENDENT);
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestSubversionScoring(CastUtils.dropWizardsOrConveyedWizards(CastUtils.keepEnemies(targets, t, teams)));
  }
}
