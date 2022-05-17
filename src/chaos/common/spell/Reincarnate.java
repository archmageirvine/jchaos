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
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Reincarnate.
 *
 * @author Sean A. Irvine
 */
public class Reincarnate extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 10;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor target = cell.peek();
      cell.notify(new AudioEvent(cell, target, "reincarnate", caster));
      cell.notify(new CellEffectEvent(cell, CellEffectType.TWIRL));
      if (target instanceof Monster) {
        target.set(PowerUps.REINCARNATE, ((Monster) target).reincarnation() != null ? 1 : 0);
      }
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }

  private int score(Actor a) {
    int sc = CastUtils.score(a);
    while (a instanceof Monster) {
      final Class<? extends Monster> c = ((Monster) a).reincarnation();
      if (c == null) {
        a = null;
      } else {
        a = (Actor) FrequencyTable.instantiate(c);
      }
      sc += CastUtils.score(a);
    }
    return sc;
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    CastUtils.dropPowerUp(targets, PowerUps.REINCARNATE);
    // Find best score of sensible targets
    int bestScore = -1;
    for (final Cell c : targets) {
      final int s = score(c.peek());
      if (s > bestScore) {
        bestScore = s;
      }
    }
    // Remove lower scoring targets
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      if (score(it.next().peek()) < bestScore) {
        it.remove();
      }
    }
    CastUtils.preferAnimates(targets);
  }
}
