package chaos.selector;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.RankingComparator;

/**
 * Selector that chooses highest ranking spell that can be cast.
 * @author Sean A. Irvine
 */
public class Ranker implements Selector {

  private final Wizard mWizard;
  private final World mWorld;
  private final CastMaster mCastMaster;

  /**
   * Construct a selector.
   * @param wizard the wizard
   * @param world the world
   * @param castMaster casting rules
   */
  public Ranker(final Wizard wizard, final World world, final CastMaster castMaster) {
    mWizard = wizard;
    mWorld = world;
    mCastMaster = castMaster;
  }

  protected Comparator<Castable> comparator() {
    // Default is to use standard ranking with best spell first
    return RankingComparator.REVERSE_COMPARATOR;
  }

  private boolean hasLegalTargets(final Wizard caster, final Castable c, final int casterCell) {
    if (c instanceof FreeCastable) {
      return true;
    }
    final int range = c.getCastRange() + caster.get(PowerUps.WAND);
    final boolean los = (c.getCastFlags() & Castable.CAST_LOS) != 0;
    final Set<Cell> potentialSet = mWorld.getCells(casterCell, 0, range, los);
    // Eliminate those which are illegal
    potentialSet.removeIf(cell -> !mCastMaster.isLegalCast(caster, c, casterCell, cell.getCellNumber()));
    // Eliminate those which while legal are not a good idea
    if (c instanceof TargetFilter) {
      ((TargetFilter) c).filter(potentialSet, caster, mWorld);
    }
    return !potentialSet.isEmpty();
  }

  @Override
  public Castable[] select(final Castable[] castables, final boolean texas) {
    if (SelectorUtils.noSelectionPossible(castables, mWizard)) {
      return SelectorUtils.NO_SELECTION;
    }
    final Cell casterCell = mWorld.getCell(mWizard);
    if (casterCell == null) {
      return SelectorUtils.NO_SELECTION;
    }
    Arrays.sort(castables, comparator());
    Castable best = null;
    for (final Castable c : castables) {
      if (hasLegalTargets(mWizard, c, casterCell.getCellNumber())) {
        best = c;
        break;
      }
    }

    final Castable worst = texas ? SelectorUtils.worst(castables, best) : null;
    return new Castable[] {best, worst};
  }

  @Override
  public Castable[] selectBonus(final Castable[] castables, final int count) {
    return SelectorUtils.best(castables, count);
  }
}
