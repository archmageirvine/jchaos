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
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Earthbind.
 * @author Sean A. Irvine
 */
public class Earthbind extends Castable implements Multiplicity, TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }

  @Override
  public int getCastRange() {
    return 14;
  }

  @Override
  public int getMultiplicity() {
    return 2;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        a.set(PowerUps.FLYING, 0);
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
      if (!(a instanceof Monster) || !a.is(PowerUps.FLYING)) {
        it.remove();
      }
    }
    CastUtils.keepFastest(targets);
  }
}
