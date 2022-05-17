package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Realm;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Demonify.
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class Demonify extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.CHANGE_REALM));
        a.setRealm(Realm.DEMONIC);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext(); ) {
      final Actor a = it.next().peek();
      if (a == null || a.getRealm() == Realm.DEMONIC) {
        it.remove();
      }
    }
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(targets));
  }
}
