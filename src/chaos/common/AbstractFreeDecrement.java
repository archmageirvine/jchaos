package chaos.common;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.util.CastUtils;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldDestroyEvent;

/**
 * Superclass for the free decrement spells.
 * @author Sean A. Irvine
 */
public abstract class AbstractFreeDecrement extends FreeCastable {

  /**
   * The decrement to be applied.
   * @return decrement
   */
  public abstract int decrement();

  /**
   * The attribute to be decremented.
   * @return attribute
   */
  public abstract Attribute attribute();

  /**
   * Given the set of potential cells affected by this spell, return
   * only those cells that are affected.  The default implementation
   * simply returns the full set.
   * @param affected affected set
   * @return filtered affected set
   */
  protected Set<Cell> filter(final Set<Cell> affected) {
    return affected;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // These spells are only applied to enemy creatures, but it does take
      // account of team responsibilities.
      final Team t = world.getTeamInformation();
      final int p = t.getTeam(caster);
      // Compute the set of affected cells first, so that we can start a global
      // effect to represent this spell
      Set<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a != null && t.getTeam(a) != p && a.getState() != State.DEAD) {
          affected.add(world.getCell(i));
        }
      }
      affected = filter(affected);
      world.notify(new PolyshieldDestroyEvent(affected, attribute(), caster));
      final Attribute at = attribute();
      final int dec = decrement();
      for (final Cell c : affected) {
        final Actor a = c.peek();
        // This following check is needed because if a wizard is killed in an
        // earlier cell, it is possible that some of his creations have also
        // been removed; hence a previous target can become null.  Like if a
        // GoblinBomb has exploded.
        if (a != null) {
          final boolean wouldDie = a.decrement(at, dec);
          if (wouldDie && at != Attribute.MAGICAL_RESISTANCE) {
            // Death!!
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            c.reinstate();
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
