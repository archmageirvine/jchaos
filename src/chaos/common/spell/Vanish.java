package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;

/**
 * Vanish.
 * @author Sean A. Irvine
 */
public class Vanish extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH | CAST_DEAD | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (world != null && cell != null) {
      world.getWarpSpace().warpOut(cell, caster);
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.keepEnemies(targets, t, teams));
  }
}
