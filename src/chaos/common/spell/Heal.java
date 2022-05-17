package chaos.common.spell;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
 * Heal.
 *
 * @author Sean A. Irvine
 */
public class Heal extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.SHIELD_GRANTED));
        a.set(Attribute.LIFE, max(a.get(Attribute.LIFE), a.getDefault(Attribute.LIFE)));
        a.set(Attribute.LIFE_RECOVERY, max(a.get(Attribute.LIFE_RECOVERY), a.getDefault(Attribute.LIFE_RECOVERY)));
        a.set(Attribute.MAGICAL_RESISTANCE, max(a.get(Attribute.MAGICAL_RESISTANCE), a.getDefault(Attribute.MAGICAL_RESISTANCE)));
        a.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, max(a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY)));
        if (a instanceof Monster) {
          final Monster m = (Monster) a;
          m.set(Attribute.INTELLIGENCE, max(m.get(Attribute.INTELLIGENCE), m.getDefault(Attribute.INTELLIGENCE)));
          m.set(Attribute.INTELLIGENCE_RECOVERY, max(m.get(Attribute.INTELLIGENCE_RECOVERY), m.getDefault(Attribute.INTELLIGENCE_RECOVERY)));
          m.set(Attribute.AGILITY, max(m.get(Attribute.AGILITY), m.getDefault(Attribute.AGILITY)));
          m.set(Attribute.AGILITY_RECOVERY, max(m.get(Attribute.AGILITY_RECOVERY), m.getDefault(Attribute.AGILITY_RECOVERY)));
          m.set(Attribute.RANGE, max(m.get(Attribute.RANGE), m.getDefault(Attribute.RANGE)));
          m.set(Attribute.RANGE_RECOVERY, max(m.get(Attribute.RANGE_RECOVERY), m.getDefault(Attribute.RANGE_RECOVERY)));
          m.set(Attribute.MOVEMENT, max(m.get(Attribute.MOVEMENT), m.getDefault(Attribute.MOVEMENT)));
          m.set(Attribute.MOVEMENT_RECOVERY, max(m.get(Attribute.MOVEMENT_RECOVERY), m.getDefault(Attribute.MOVEMENT_RECOVERY)));
          if (m.getDefault(Attribute.COMBAT) >= 0) {
            m.set(Attribute.COMBAT, max(m.get(Attribute.COMBAT), m.getDefault(Attribute.COMBAT)));
          } else {
            m.set(Attribute.COMBAT, min(m.get(Attribute.COMBAT), m.getDefault(Attribute.COMBAT)));
          }
          m.set(Attribute.COMBAT_RECOVERY, max(m.get(Attribute.COMBAT_RECOVERY), m.getDefault(Attribute.COMBAT_RECOVERY)));
          if (m.getDefault(Attribute.RANGED_COMBAT) >= 0) {
            m.set(Attribute.RANGED_COMBAT, max(m.get(Attribute.RANGED_COMBAT), m.getDefault(Attribute.RANGED_COMBAT)));
          } else {
            m.set(Attribute.RANGED_COMBAT, min(m.get(Attribute.RANGED_COMBAT), m.getDefault(Attribute.RANGED_COMBAT)));
          }
          m.set(Attribute.RANGED_COMBAT_RECOVERY, max(m.get(Attribute.RANGED_COMBAT_RECOVERY), m.getDefault(Attribute.RANGED_COMBAT_RECOVERY)));
          if (m.getDefault(Attribute.SPECIAL_COMBAT) >= 0) {
            m.set(Attribute.SPECIAL_COMBAT, max(m.get(Attribute.SPECIAL_COMBAT), m.getDefault(Attribute.SPECIAL_COMBAT)));
          } else {
            m.set(Attribute.SPECIAL_COMBAT, min(m.get(Attribute.SPECIAL_COMBAT), m.getDefault(Attribute.SPECIAL_COMBAT)));
          }
          m.set(Attribute.SPECIAL_COMBAT_RECOVERY, max(m.get(Attribute.SPECIAL_COMBAT_RECOVERY), m.getDefault(Attribute.SPECIAL_COMBAT_RECOVERY)));
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepSickest(CastUtils.keepFriends(targets, t, teams));
  }
}
