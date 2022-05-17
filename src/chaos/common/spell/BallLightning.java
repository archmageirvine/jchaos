package chaos.common.spell;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
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
 * Ball lightning.
 *
 * @author Sean A. Irvine
 */
public class BallLightning extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_DEAD | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  /** Damage inflicted by lightning. */
  private static final int DAMAGE = 6;

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
          // Ball lightning goes after combat, but this is only possible for
          // monsters.  Damage that cannot be applied to combat is carried
          // down to affect the life statistic.
          int damage = DAMAGE;
          if (a instanceof Monster) {
            // We need to be careful here.  Healers have negative combat, and
            // these should increase rather than decrease.  At same time we
            // have to make sure carry over damage will decrease life.
            final Monster m = (Monster) a;
            final boolean healer = m.getDefault(Attribute.COMBAT) < 0;
            final int combat = (healer ? -m.get(Attribute.COMBAT) : m.get(Attribute.COMBAT)) - damage;
            if (combat >= 0) {
              // Monster had sufficiently high combat to absorb all the blast
              m.set(Attribute.COMBAT, healer ? -combat : combat);
              damage = 0;
            } else {
              // Combat is zeroed as a result; but there may still be some
              // damage yet to be applied.
              damage = -combat;
              m.set(Attribute.COMBAT, 0);
            }
          }
          // Any remaining damage is doubled and applied to the life statistic
          damage <<= 1;
          final int life = a.get(Attribute.LIFE) - damage;
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
