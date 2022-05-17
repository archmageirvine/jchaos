package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Demote.
 * @author Sean A. Irvine
 */
public class Demote extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      if (casterCell != null) {
        cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.DEMOTION));
      }
      final Actor m = cell.peek();
      cell.notify(new CellEffectEvent(cell, CellEffectType.REINCARNATE));
      if (m instanceof Monster) {
        final int prevCount = m.get(PowerUps.REINCARNATE);
        m.set(PowerUps.REINCARNATE, ((Monster) m).reincarnation() != null ? 1 : 0);
        cell.reinstate();
        final Actor x = cell.peek();
        if (x instanceof Monster) {
          x.set(PowerUps.REINCARNATE, prevCount); // restore previous reincarnation status
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(CastUtils.keepEnemies(targets, t, teams)));
  }
}
