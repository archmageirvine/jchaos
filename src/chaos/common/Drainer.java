package chaos.common;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.Restore;

/**
 * Abstract class for spells like Dark Power, Justice, and Consecrate.
 * @author Sean A. Irvine
 */
public abstract class Drainer extends Castable implements TargetFilter {

  /**
   * Return the set of cells to be affected by this spell.  This method should
   * not return null, although an empty set is permissible.
   * @param world world where spell is cast
   * @param cell cell that the user selected
   * @return set of affected cells
   */
  public abstract Set<Cell> getAffectedCells(final World world, final Cell cell);

  /**
   * Get the cell effect type to use on each affected cell.  Can be null.
   * @return cell effect type
   */
  public abstract CellEffectType getEffectType();

  /**
   * Get the damage inflicted on each cell by this spell.
   * @return damage value
   */
  public abstract int getDamage();

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && world != null) {
      final Set<Cell> affected = getAffectedCells(world, cell);
      final CellEffectType e = getEffectType();
      if (e != null) {
        world.notify(new PolycellEffectEvent(affected, e, caster));
      }
      for (final Cell c : affected) {
        final Actor a = c.peek();
        if (a != null) {
          final int mr = a.get(Attribute.MAGICAL_RESISTANCE) - getDamage();
          if (mr >= 0) {
            a.set(Attribute.MAGICAL_RESISTANCE, mr);
          } else if (a instanceof Wizard) {
            // Death would normally occur, but this is a wizard.  So we just throw a
            // bonus spell at the caster.
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            a.set(Attribute.MAGICAL_RESISTANCE, 0);
            c.notify(new CellEffectEvent(c, CellEffectType.WHITE_CIRCLE_EXPLODE));
          } else {
            // Death.  This is a little trickier than normal because we may want to
            // replace the current creature with a living form of the corpse below.
            // Start by retrieving the under creature (if any)
            c.notify(new CellEffectEvent(c, CellEffectType.CORPSE_EXPLODE));
            c.pop();
            final Actor under = c.peek();
            c.push(a);
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            if (c.reinstate()) {
              c.pop(); // throw away any corpse that may have appeared
              if (under != null) {
                // Cell was "vacated" by death, can reinstate a living as necessary
                Restore.restore(under);
                under.setOwner(a.getOwner());
                c.push(under);
              }
            }
          }
        }
      }
      if (e != null) {
        world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL, caster));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepEnemies(targets, t, teams);
    // First try for the strongest thing we can kill
    int best = -1;
    final int d = getDamage();
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a != null) {
        final int m = a.get(Attribute.MAGICAL_RESISTANCE);
        final int s = CastUtils.score(c);
        if (s > best && m < d) {
          best = s;
        }
      }
    }
    if (best != -1) {
      // Okay found at least one killable target
      for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
        final Cell c = it.next();
        if (CastUtils.score(c) != best) {
          it.remove();
        }
      }
    } else {
      CastUtils.keepHighestScoring(CastUtils.preferAwake(CastUtils.preferAnimates(targets)));
    }
  }
}
