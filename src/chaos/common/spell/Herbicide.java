package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Growth;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Herbicide.
 *
 * @author Sean A. Irvine
 */
public class Herbicide extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_GROWTH;
  }
  @Override
  public int getCastRange() {
    return 20;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && caster != null) {
      final Actor a = cell.peek();
      if (a instanceof Growth) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.POISON));
        a.set(PowerUps.NO_GROW, 1);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a == null || !(a instanceof Growth)) {
        i.remove();
      } else {
        final Growth g = (Growth) a;
        if (a.is(PowerUps.NO_GROW) || g.getGrowthType() != Growth.GROW_OVER) {
          i.remove();
        }
      }
    }
    CastUtils.keepClosest(CastUtils.keepEnemies(targets, t, teams), caster, world);
  }
}
