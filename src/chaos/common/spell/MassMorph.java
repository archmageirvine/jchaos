package chaos.common.spell;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.inanimate.MagicWood;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Mass morph.
 * @author Sean A. Irvine
 * @author William S. Irvine
 */
public class MassMorph extends Castable implements TargetFilter {

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
    if (world != null && cell != null && caster != null) {
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
          final Cell c = world.getCell(i);
          final Actor a = c.peek();
          if (a != null
            && !(a instanceof Wizard)
            && !(a instanceof AbstractGenerator)
            && a.getState() == State.ACTIVE
            && c.getMount() == null
            && c.peek().getOwner() == player) {
            affected.add(c);
          }
        }
        // Normally the inserted trees will belong to the player who originally
        // owned the target.  But sometimes that player could actually be dead,
        // if a sleeping target was chosen.  In this case, the new trees will
        // belong to the caster.  This fixes Bug#264.
        final int castOwner = caster.getOwner();
        final int newOwner;
        if (player == Actor.OWNER_NONE) {
          newOwner = castOwner;
        } else {
          final Wizard wizTarget = world.getWizardManager().getWizard(player);
          newOwner = wizTarget != null && wizTarget.getState() != State.ACTIVE ? castOwner : player;
        }
        if (!affected.isEmpty()) {
          // At least one candidate cell was found.  Perform an effect on all
          // selected cells and carry out the animation.
          world.notify(new PolycellEffectEvent(affected, CellEffectType.TWIRL));
          for (final Cell c : affected) {
            // Bug#309 case of growths with things underneath, need to pop entire content
            while (c.pop() != null) {
              // do nothing
            }
            final MagicWood mw = new MagicWood();
            mw.setOwner(newOwner);
            c.push(mw);
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
    CastUtils.keepMostFrequentOwner(targets, world, world.getTeamInformation().getTeam(caster));
  }
}
