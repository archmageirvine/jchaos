package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Promotion;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.CombatUtils;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Promote.
 * @author Sean A. Irvine
 */
public class Promote extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 100;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      if (casterCell != null) {
        cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.PROMOTION));
      }
      final Actor m = cell.peek();
      cell.notify(new CellEffectEvent(cell, CellEffectType.REINCARNATE));
      if (m instanceof Promotion) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.MONSTER_CAST_EVENT, CombatUtils.promote(m, cell, ((Promotion) m).promotion())));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    targets.removeIf(cell -> !(cell.peek() instanceof Promotion));
  }
}
