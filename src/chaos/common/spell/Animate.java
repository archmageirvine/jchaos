package chaos.common.spell;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Animate.
 * @author Sean A. Irvine
 */
public class Animate extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
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
        // the cell the user clicked when casting the spell.  Cells affected
        // must implement animateable but have no mount present.
        final HashSet<Cell> affected = new HashSet<>();
        for (int i = 0; i < world.size(); ++i) {
          final Cell c = world.getCell(i);
          if (c.peek() instanceof Animateable && c.getMount() == null && c.peek().getOwner() == player) {
            affected.add(c);
          }
        }
        if (!affected.isEmpty()) {
          // At least one candidate cell was found.  Perform an effect on all
          // selected cells and carry out the animation.
          world.notify(new PolycellEffectEvent(affected, CellEffectType.TWIRL));
          for (final Cell c : affected) {
            final Actor a = c.pop();
            c.push(((Animateable) a).getAnimatedForm());
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
    // AI should only cast this on thyself
    final int owner = caster.getOwner();
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext(); ) {
      final Actor a = it.next().peek();
      if (a == null || a.getOwner() != owner) {
        it.remove();
      }
    }
  }

}
