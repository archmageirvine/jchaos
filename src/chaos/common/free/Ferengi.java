package chaos.common.free;

import java.util.ArrayList;
import java.util.Arrays;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.RankingComparator;

/**
 * Ferengi.
 *
 * @author Sean A. Irvine
 */
public class Ferengi extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster instanceof Wizard) {
      final ArrayList<Castable> pendingAddition = new ArrayList<>();
      for (final Wizard w : world.getWizardManager().getWizards()) {
        if (w != null && w != caster && w.getState() == State.ACTIVE) {
          // Found a living wizard
          final CastableList spells = w.getCastableList();
          Castable best = null;
          for (final Castable c : spells.getVisible()) {
            if (RankingComparator.FORWARD_COMPARATOR.compare(c, best) >= 0) {
              best = c;
            }
          }
          if (best != null) {
            spells.use(best);
            pendingAddition.add(best);
          }
        }
      }
      // Resort spells so that the very best of the selections appears earlier
      // in the caster's spell list, and so that the very best are retained in
      // the case where not all of them fit.
      final CastableList goodGuy = ((Wizard) caster).getCastableList();
      final Castable[] toBeAdded = pendingAddition.toArray(new Castable[0]);
      Arrays.sort(toBeAdded, RankingComparator.REVERSE_COMPARATOR);
      for (final Castable spell : toBeAdded) {
        goodGuy.add(spell);
      }
    }
    // Always do the effect on the caster
    if (casterCell != null) {
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.BONUS, caster));
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
    }
  }
}


