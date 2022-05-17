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
import chaos.util.ShieldDestroyedEvent;

/**
 * Cursed sword.
 * @author Sean A. Irvine
 */
public class CursedSword extends Castable implements TargetFilter {

  private static final int CURSED_SWORD_FACTOR = 10;

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
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        // Target is valid. Need to be careful about healers.
        final Monster m = (Monster) a;
        cell.notify(new ShieldDestroyedEvent(cell, caster, Attribute.COMBAT));
        final int x = m.get(Attribute.COMBAT);
        if (m.getDefault(Attribute.COMBAT) >= 0) {
          if (x > 0) {
            m.set(Attribute.COMBAT, Math.max(0, x - CURSED_SWORD_FACTOR));
          }
        } else if (x < 0) {
          m.set(Attribute.COMBAT, Math.min(0, x + CURSED_SWORD_FACTOR));
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.keepEnemies(targets, t, teams));
  }
}
