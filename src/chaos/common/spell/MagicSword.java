package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PowerUpEvent;

/**
 * Magic sword.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicSword extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 10;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor target = cell.peek();
      if (target != null) {
        cell.notify(new PowerUpEvent(cell, caster, PowerUps.SWORD));
        target.increment(Attribute.COMBAT, 5);
        target.set(PowerUps.SWORD, 1);
        target.set(PowerUps.ATTACK_ANY_REALM, 1);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    CastUtils.dropPowerUp(targets, PowerUps.ATTACK_ANY_REALM);
    CastUtils.preferAnimates(targets);
    CastUtils.keepHighestScoring(targets);
  }
}
