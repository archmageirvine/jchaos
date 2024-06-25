package chaos.common.spell;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Swap.
 * @author Sean A. Irvine
 */
public class Swap extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (world != null && cell != null && caster != null) {
      // Need to compute the player number of the target.  If this cannot be done
      // then we silently exit, since it should not be possible to cast this
      // spell on a non-actor.
      final Actor target = cell.peek();
      if (target != null) {
        final int cp = caster.getOwner();
        final int sp = target.getOwner();
        // There is no point to this spell is the caster and target are the same
        if (cp != sp) {
          // Compute the sets of cells to be affected.
          final HashSet<Cell> swapee = new HashSet<>();
          final HashSet<Cell> swaper = new HashSet<>();
          for (int i = 0; i < world.size(); ++i) {
            final Cell c = world.getCell(i);
            final Actor a = c.peek();
            if (a != null && !(a instanceof Wizard) && !(a instanceof Inanimate) && c.getMount() == null) {
              final int o = c.peek().getOwner();
              if (o == sp) {
                swapee.add(c);
              } else if (o == cp) {
                swaper.add(c);
              }
            }
          }
          // Change owner of swapee
          if (!swapee.isEmpty()) {
            // At least one candidate cell was found.  Perform an effect on all
            // selected cells and carry out the animation.
            world.notify(new PolycellEffectEvent(swapee, CellEffectType.OWNER_CHANGE));
            for (final Cell c : swapee) {
              c.peek().setOwner(cp);
            }
          }
          // And redraw all the cells with the new forms.
          world.notify(new PolycellEffectEvent(swapee, CellEffectType.REDRAW_CELL));
          // Change owner of swapper
          if (!swaper.isEmpty()) {
            // At least one candidate cell was found.  Perform an effect on all
            // selected cells and carry out the animation.
            world.notify(new PolycellEffectEvent(swaper, CellEffectType.OWNER_CHANGE));
            for (final Cell c : swaper) {
              c.peek().setOwner(sp);
            }
          }
          // And redraw all the cells with the new forms.
          world.notify(new PolycellEffectEvent(swaper, CellEffectType.REDRAW_CELL));
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    // Try and swap with player with most creature.  If this player is the caster
    // then will attempt to swap with self (i.e. abort).
    CastUtils.keepMostFrequentOwner(targets, world, -1);
  }

}
