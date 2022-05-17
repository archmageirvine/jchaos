package chaos.common.spell;

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
 * Cloak.
 * @author Sean A. Irvine
 */
public class Cloak extends Castable implements TargetFilter {

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
        cell.notify(new AudioEvent(cell, target, "cloak", caster));
        target.set(PowerUps.CLOAKED, 1);
        cell.notify(new CellEffectEvent(cell, CellEffectType.SHIELD_GRANTED));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    CastUtils.dropPowerUp(targets, PowerUps.CLOAKED);
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(targets));
  }
}
