package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * X-ray.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class XRay extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 9;
  }

  /** Damage inflicted. */
  private static final int DAMAGE = 30;

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        if (a.getState() == State.DEAD) {
          cell.notify(new CellEffectEvent(cell, CellEffectType.CORPSE_EXPLODE));
          cell.reinstate();
          if (caster instanceof Wizard) {
            ((Wizard) caster).addScore(5);
          }
        } else {
          cell.notify(new CellEffectEvent(cell, CellEffectType.WHITE_CIRCLE_EXPLODE));
          final int mr = a.get(Attribute.MAGICAL_RESISTANCE) - DAMAGE;
          if (mr > 0) {
            // survived
            a.set(Attribute.MAGICAL_RESISTANCE, mr);
          } else {
            // no mr
            a.set(Attribute.MAGICAL_RESISTANCE, 0);
            if (-mr >= a.get(Attribute.LIFE)) {
              CastUtils.handleScoreAndBonus(caster, a, casterCell);
              cell.reinstate();
            } else {
              a.set(Attribute.LIFE, a.get(Attribute.LIFE) + mr);
            }
          }
        }
      }
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.preferAnimates(CastUtils.keepEnemies(targets, t, teams));
  }
}
