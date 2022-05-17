package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Thunderbolt.
 * @author Sean A. Irvine
 */
public class Thunderbolt extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      if (casterCell != null) {
        cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.THUNDERBOLT));
      }
      final Actor a = cell.peek();
      if (a != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.WHITE_CIRCLE_EXPLODE));
        for (final Attribute attr : Attribute.values()) {
          a.set(attr, Math.min(1, a.get(attr)));
        }
      }
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(CastUtils.keepEnemies(targets, t, teams)));
  }
}
