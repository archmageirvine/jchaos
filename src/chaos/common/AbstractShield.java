package chaos.common;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ShieldGrantedEvent;

/**
 * Abstract superclass for all the attribute increment spells.
 * @author Sean A. Irvine
 */
public abstract class AbstractShield extends Castable implements TargetFilter {

  /**
   * The increment to be applied.
   * @return increment
   */
  public abstract int increment();

  /**
   * The attribute to be incremented.
   * @return attribute
   */
  public abstract Attribute attribute();

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      final Attribute at = attribute();
      if (a != null && at != null) {
        // Target is valid.  Normally we just increase the relevant statistic, but
        // if it is already at the maximum, then the corresponding recovery statistic
        // is incremented instead.  However, the exact checking of this is done in
        // a roundabout way to allow for different attributes having different maximums
        final int oldValue = a.get(at);
        a.increment(at, increment());
        if (a.get(at) == oldValue) {
          // Value was not changed, use recovery instead
          final Attribute attr = at.recovery();
          if (attr != null) {
            cell.notify(new ShieldGrantedEvent(cell, caster, attr));
            a.increment(attr, 1);
          }
        } else {
          cell.notify(new ShieldGrantedEvent(cell, caster, at));
        }
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
