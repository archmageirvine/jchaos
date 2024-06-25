package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Convert.
 * @author Sean A. Irvine
 */
public class Convert extends Castable implements Multiplicity, TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 16;
  }

  @Override
  public int getMultiplicity() {
    return 2;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.CHANGE_REALM));
        a.setRealm(Realm.MATERIAL);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(CastUtils.dropRealm(CastUtils.keepEnemies(targets, t, teams), Realm.MATERIAL)));
  }
}
