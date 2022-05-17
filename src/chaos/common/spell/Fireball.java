package chaos.common.spell;

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
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

import java.util.Set;

/**
 * Fireball.
 *
 * @author Sean A. Irvine
 */
public class Fireball extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_DEAD | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  /** Damage inflicted by fireball. */
  private static final int DAMAGE = 30;

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && casterCell != null) {
      cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.FIREBALL));
      final Actor a = cell.peek();
      if (a != null) {
        // Have a target.  The exact result of this spells depends on the nature
        // of the target.  Getting here should always happen for valid casts of
        // this spell.
        if (a.getState() == State.DEAD) {
          // Target is a corpse, explode it and give a few points to the caster.
          cell.notify(new CellEffectEvent(cell, CellEffectType.CORPSE_EXPLODE));
          cell.reinstate();
          if (caster instanceof Wizard) {
            ((Wizard) caster).addScore(5);
          }
        } else {
          cell.notify(new CellEffectEvent(cell, CellEffectType.ORANGE_CIRCLE_EXPLODE));
          // Fireball goes after combat, but this is only possible for
          // monsters.  Damage that cannot be applied to combat is carried
          // down to affect the life statistic.
          final int mr = a.get(Attribute.MAGICAL_RESISTANCE) - DAMAGE;
          if (mr >= 0) {
            a.set(Attribute.MAGICAL_RESISTANCE, mr);
          } else {
            a.set(Attribute.MAGICAL_RESISTANCE, 0);
            // Any remaining damage is doubled and applied to the life statistic
            // Note mr is actually negative at this point, which is why we add below
            final int life = a.get(Attribute.LIFE) + (mr << 1);
            if (life > 0) {
              // Actor survives
              a.set(Attribute.LIFE, life);
            } else {
              // Actor dies
              CastUtils.handleScoreAndBonus(caster, a, casterCell);
              cell.reinstate();
            }
          }
        }
      }
      // Finally, force a redraw of the possibly changed cell.
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
