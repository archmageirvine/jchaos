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
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Obscurity.
 * @author Sean A. Irvine
 */
public class Obscurity extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        a.set(PowerUps.ARCHERY, 0);
        cell.notify(new CellEffectEvent(cell, CellEffectType.POWERUP));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepEnemies(targets, t, teams);
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext(); ) {
      final Actor a = it.next().peek();
      if (!(a instanceof Monster)) {
        it.remove();
      } else {
        final Monster m = (Monster) a;
        if (!m.is(PowerUps.ARCHERY)) {
          it.remove();
        }
      }
    }
    CastUtils.keepHighestScoring(targets);
  }
}
