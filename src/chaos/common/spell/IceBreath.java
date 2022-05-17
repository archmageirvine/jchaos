package chaos.common.spell;

import java.util.Iterator;
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
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Ice breath.
 *
 * @author Sean A. Irvine
 */
public class IceBreath extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_DEAD | CAST_INANIMATE | CAST_GROWTH | CAST_EMPTY;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  private static final int CENTER_DAMAGE = 30;
  private static final int SIDE_DAMAGE = 12;

  private Set<Cell> getAffectedCells(final World world, final Cell cell) {
    final Set<Cell> affected = world.getCells(cell.getCellNumber(), 0, 1, false);
    affected.removeIf(c -> !(c.peek() instanceof Actor));
    return affected;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && casterCell != null && world != null) {
      cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.ICE_BEAM));
      cell.notify(new CellEffectEvent(cell, CellEffectType.ICE_BOMB));
      for (final Cell c : getAffectedCells(world, cell)) {
        final Actor a = c.peek();
        if (a != null) {
          if (a.getState() == State.DEAD) {
            c.reinstate();
            if (caster instanceof Wizard) {
              ((Wizard) caster).addScore(5);
            }
          } else {
            final int damage = cell == c ? CENTER_DAMAGE : SIDE_DAMAGE;
            final int mr = a.get(Attribute.MAGICAL_RESISTANCE) - damage;
            if (mr > 0) {
              // survived
              a.set(Attribute.MAGICAL_RESISTANCE, mr);
            } else {
              // Magical resistance exhausted, transfer remaining damage to life
              a.set(Attribute.MAGICAL_RESISTANCE, 0);
              final int transfer = -2 * mr;
              if (transfer > a.get(Attribute.LIFE)) {
                CastUtils.handleScoreAndBonus(caster, a, casterCell);
                c.reinstate();
              } else {
                a.set(Attribute.LIFE, a.get(Attribute.LIFE) - transfer);
              }
            }
          }
        }
        c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    // Don't want to cast this close to own creatures
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      int bad = 0;
      int good = 0;
      for (final Cell c : world.getCells(it.next().getCellNumber(), 0, 1, false)) {
        final Actor a = c.peek();
        if (a != null) {
          if (a.getState() == State.ACTIVE && teams.getTeam(a) == t) {
            ++bad;
            if (a == caster) {
              ++bad; // Extra bad to cast this by the wizard
            }
          } else {
            ++good;
          }
        }
      }
      if (5 * bad > good || good == 0) {
        it.remove();
      }
    }
  }
}
