package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Reveal.
 *
 * @author Sean A. Irvine
 */
public class Reveal extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor target = cell.peek();
      if (target instanceof Monster) {
        // In theory should always get here, so there is no need for any effect if
        // that turns out not to be the case.
        cell.notify(new AudioEvent(cell, target, "reveal", caster));
        target.set(PowerUps.CLOAKED, 0);
        cell.notify(new CellEffectEvent(cell, CellEffectType.WHITE_CIRCLE_EXPLODE));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepEnemies(targets, t, teams);
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (!(a instanceof Monster) || !a.is(PowerUps.CLOAKED)) {
        it.remove();
      }
    }
    CastUtils.keepHighestScoring(targets);
  }
}
