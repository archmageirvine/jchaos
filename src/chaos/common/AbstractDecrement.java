package chaos.common;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ShieldDestroyedEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Abstract superclass for all the attribute decrement spells.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractDecrement extends Castable implements TargetFilter {

  /** Range at which weapon effect should be drawn. */
  private static final int RANGE_LIMIT_FOR_WEAPON_EFFECT = 10;

  /**
   * The decrement to be applied.
   *
   * @return decrement
   */
  public abstract int decrement();

  /**
   * The attribute to be decremented.
   *
   * @return attribute
   */
  public abstract Attribute attribute();

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      final Attribute at = attribute();
      if (a != null) {
        if (casterCell != null && getCastRange() < RANGE_LIMIT_FOR_WEAPON_EFFECT) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.BALL, caster, at));
        }
        cell.notify(new ShieldDestroyedEvent(cell, caster, at));
        if (a.decrement(at, decrement())) {
          // Actor should die as a result of the decrement.
          CastUtils.handleScoreAndBonus(caster, a, casterCell);
          cell.reinstate();
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.preferAnimates(CastUtils.keepEnemies(targets, t, teams));
  }
}
