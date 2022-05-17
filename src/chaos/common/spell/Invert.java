package chaos.common.spell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Invert.
 *
 * @author Sean A. Irvine
 */
public class Invert extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_GROWTH | CAST_INANIMATE;
  }
  @Override
  public int getCastRange() {
    return 4;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (world != null && cell != null) {
      // Need to compute the player number of the target.  If this cannot be done
      // then we silently exit, since it should not be possible to cast this
      // spell on a non-actor.
      final Actor target = cell.peek();
      if (target != null) {
        final int player = target.getOwner();
        // Compute set of cells to be affected.  This might or might not include
        // the cell the user clicked when casting the spell.
        final HashSet<Cell> affected = new HashSet<>();
        for (int i = 0; i < world.size(); ++i) {
          final Actor a = world.actor(i);
          if (a != null && !(a instanceof AbstractGenerator) && a.getState() == State.ACTIVE && a.getOwner() == player) {
            affected.add(world.getCell(i));
          }
        }
        if (!affected.isEmpty()) {
          // At least one candidate cell was found.  Perform an effect on all
          // selected cells and carry out the animation.
          world.notify(new PolycellEffectEvent(affected, CellEffectType.TWIRL));
          for (final Cell c : affected) {
            final Actor a = c.peek();
            // Use 64 here rather than the theoretical maximum of 100, because
            // most actors are subject to this constraint.
            a.set(Attribute.LIFE, Math.max(1, 64 - a.get(Attribute.LIFE)));
          }
          // And redraw all the cells with the new forms.
          world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
        } else if (casterCell != null) {
          // Try to indicate spell failure in the case of no affected.
          casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    // This is a very tricky spell to cast sensibly.  We cast it only if the
    // caster (i.e. wizard) is visible and has low life and doesn't have too
    // many other creatures.
    final int co = caster.getOwner();
    int count = 0;
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a == null || a.getOwner() != co) {
        it.remove();
      } else {
        ++count;
        if (a != caster) {
          it.remove();
        }
      }
    }
    // Finally abort spell if life too high for number of creatures
    count <<= 1;
    if (caster.get(Attribute.LIFE) > 12 - count) {
      targets.clear();
    }
  }
}
