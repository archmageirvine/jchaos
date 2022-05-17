package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Poison dagger.
 *
 * @author Sean A. Irvine
 */
public class PoisonDagger extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }
  @Override
  public int getCastRange() {
    return 11;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        final Monster m = (Monster) a;
        m.set(Attribute.COMBAT, Math.max(5, m.get(Attribute.COMBAT)));
        m.setCombatApply(Attribute.LIFE_RECOVERY);
        cell.notify(new CellEffectEvent(cell, CellEffectType.POWERUP));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.preferAnimates(CastUtils.keepFriends(targets, t, teams));
  }
}
