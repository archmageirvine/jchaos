package chaos.common.spell;

import static chaos.common.Attribute.RANGE;
import static chaos.common.Attribute.RANGED_COMBAT;
import static chaos.common.Attribute.RANGED_COMBAT_RECOVERY;
import static chaos.common.Attribute.RANGE_RECOVERY;

import java.util.Iterator;
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
import chaos.util.ShieldGrantedEvent;

/**
 * Range boost.
 *
 * @author Sean A. Irvine
 */
public class RangeBoost extends Castable implements TargetFilter {

  private static final int INCREMENT = 4;

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
    // Implementation is based on AbstractShield, but needs to handle two attributes.
    if (cell != null) {
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        if (a.get(RANGE) != 0 || a.getDefault(RANGE) != 0) {
          final int oldRange = a.get(RANGE);
          a.increment(RANGE, INCREMENT);
          if (a.get(RANGE) == oldRange) {
            a.increment(RANGE_RECOVERY, 1);
            cell.notify(new ShieldGrantedEvent(cell, caster, RANGE_RECOVERY));
          } else {
            cell.notify(new ShieldGrantedEvent(cell, caster, RANGE));
          }
          final int oldRC = a.get(RANGED_COMBAT);
          a.increment(RANGED_COMBAT, INCREMENT);
          if (a.get(RANGED_COMBAT) == oldRC) {
            a.increment(RANGED_COMBAT_RECOVERY, 1);
          }
        } else {
          // The monster had no ranged combat ability to start with
          cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    // Want friendly creatures that already have some shooting ability
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    for (final Iterator<Cell> it = CastUtils.preferAnimates(CastUtils.keepFriends(targets, t, teams)).iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (!(a instanceof Monster) || a.get(Attribute.RANGE) == 0) {
        it.remove();
      }
    }
  }
}
