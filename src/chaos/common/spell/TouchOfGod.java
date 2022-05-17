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
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Touch of God.
 *
 * @author Sean A. Irvine
 */
public class TouchOfGod extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 4;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      cell.notify(new AudioEvent(cell, a, "touch_of_god", caster));
      a.set(Attribute.LIFE, Attribute.LIFE.max());
      a.increment(Attribute.LIFE_RECOVERY, 7);
      a.set(Attribute.MAGICAL_RESISTANCE, Attribute.MAGICAL_RESISTANCE.max());
      if (a instanceof Monster) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.SHIELD_GRANTED));
        final Monster m = (Monster) a;
        m.set(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
        m.set(Attribute.AGILITY, Attribute.AGILITY.max());
        m.set(Attribute.MOVEMENT, Attribute.MOVEMENT.max());
        m.set(Attribute.RANGE, Math.max(15, m.get(Attribute.RANGE)));
        m.increment(Attribute.SHOTS, 1);
        if (m.getDefault(Attribute.RANGED_COMBAT) >= 0) {
          m.set(Attribute.RANGED_COMBAT, Math.max(15, m.get(Attribute.RANGED_COMBAT)));
        } else {
          m.set(Attribute.RANGED_COMBAT, Math.min(-15, m.get(Attribute.RANGED_COMBAT)));
        }
        if (m.getDefault(Attribute.COMBAT) >= 0) {
          m.set(Attribute.COMBAT, Math.max(15, m.get(Attribute.COMBAT)));
        } else {
          m.set(Attribute.COMBAT, Math.min(-15, m.get(Attribute.COMBAT)));
        }
        if (m.getDefault(Attribute.SPECIAL_COMBAT) >= 0) {
          m.set(Attribute.SPECIAL_COMBAT, Math.max(15, m.get(Attribute.SPECIAL_COMBAT)));
        } else {
          m.set(Attribute.SPECIAL_COMBAT, Math.min(-15, m.get(Attribute.SPECIAL_COMBAT)));
        }
        m.setCombatApply(Attribute.LIFE);
        m.setRangedCombatApply(Attribute.LIFE);
        m.setSpecialCombatApply(Attribute.LIFE);
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
