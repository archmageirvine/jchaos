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
import chaos.util.ShieldGrantedEvent;

/**
 * Combat.
 * @author Sean A. Irvine
 */
public class Combat extends Castable implements TargetFilter {

  /** Nominal maximum combat. */
  private static final int NOMINAL_MAX_COMBAT = 15;

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 9;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a instanceof Monster) {
        // Technically combat can be as high as MAX_VALUE, but by convention is does not
        // exceed NOMINAL_MAX_COMBAT.  Therefore this spell will only exceed NOMINAL_MAX_COMBAT
        // if the monster already has maximum combat
        final int c = a.get(Attribute.COMBAT);
        final int absCombat = Math.abs(c);
        if (absCombat == Attribute.COMBAT.max()) {
          // It is very hard to achieve this next line, you would have to cast this
          // spell a large number of times on the same monster.
          cell.notify(new ShieldGrantedEvent(cell, caster, Attribute.COMBAT_RECOVERY));
          a.increment(Attribute.COMBAT_RECOVERY, 1);
        } else {
          cell.notify(new ShieldGrantedEvent(cell, caster, Attribute.COMBAT));
          if (absCombat >= NOMINAL_MAX_COMBAT) {
            a.increment(Attribute.COMBAT, 5);
          } else {
            a.increment(Attribute.COMBAT, NOMINAL_MAX_COMBAT - absCombat);
          }
        }
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
