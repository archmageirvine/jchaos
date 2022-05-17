package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Alter reality.
 *
 * @author Sean A. Irvine
 */
public class AlterReality extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 5;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (CastUtils.mutate(cell)) {
      cell.notify(new CellEffectEvent(cell, CellEffectType.TWIRL));
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    } else {
      cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.dropWizardsOrConveyedWizards(targets);
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    // Find highest scoring targets
    int sc = 0;
    Cell best = null;
    final int meanScore = FrequencyTable.DEFAULT.getMeanScore();
    for (final Cell c : targets) {
      final int s = CastUtils.score(c);
      final Actor a = c.peek();
      if (a != null) {
        final int ta = teams.getTeam(a);
        if (ta == t && s < meanScore) {
          final int d = meanScore - s;
          if (d >= sc) {
            sc = d;
            best = c;
          }
        } else if (ta != t && s > meanScore) {
          final int d = s - meanScore;
          if (d >= sc) {
            sc = d;
            best = c;
          }
        }
      }
    }
    // Eliminate everything else
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      if (it.next() != best) {
        it.remove();
      }
    }
  }
}
