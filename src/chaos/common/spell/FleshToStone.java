package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.common.inanimate.Rock;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Flesh to stone.
 * @author Sean A. Irvine
 */
public class FleshToStone extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 7;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor current = cell.peek();
      // In theory it is not possible to cast this spell on empty cells, but for
      // robustness we check anyway.
      if (current != null) {
        if (!(current instanceof Wizard) && cell.getMount() == null) {
          final Actor a = new Rock();
          a.setState(current.getState());
          a.setOwner(current.getOwner());
          cell.pop();
          cell.push(a);
          cell.notify(new CellEffectEvent(cell, CellEffectType.TWIRL));
          cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
        } else {
          // The spell fails on cells containing a wizard because there is too
          // much complication changing such a cell to something valid
          cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.dropWizardsOrConveyedWizards(targets);
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepEnemies(targets, t, teams);
    CastUtils.keepHighestScoring(targets);
  }
}
