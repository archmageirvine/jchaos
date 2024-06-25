package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.PowerUp;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PowerUpEvent;

/**
 * Convenience class for casting a power up spell.
 * @author Sean A. Irvine
 */
public abstract class AbstractPowerUp extends Castable implements PowerUp, TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE | CAST_LIVING | CAST_NOASLEEP;
  }

  /**
   * Defines the count used with this power up.
   * @return power up count
   */
  protected int getPowerUpCount() {
    return 1;
  }

  /**
   * Should this power up add to any existing count of this power up.
   * @return true to add
   */
  protected boolean cumulative() {
    return false;
  }

  @Override
  public final void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor actor = cell.peek();
      if (actor != null) {
        final int current = cumulative() ? actor.get(getPowerUpType()) : 0;
        actor.set(getPowerUpType(), current + getPowerUpCount());
        cell.notify(new PowerUpEvent(cell, caster, getPowerUpType()));
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
