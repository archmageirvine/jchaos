package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Kill.
 * @author Sean A. Irvine
 */
public class Kill extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_NOEXPOSEDWIZARD;
  }

  @Override
  public int getCastRange() {
    return 6;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && caster != null) {
      final Actor a = cell.peek();
      if (a != null) {
        CastUtils.handleScoreAndBonus(caster, a, casterCell);
        cell.notify(new CellEffectEvent(cell, CellEffectType.CORPSE_EXPLODE));
        cell.reinstate();
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(CastUtils.keepEnemies(targets, t, teams)));
  }
}
