package chaos.selector;

import java.util.Set;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.FreeCastable;
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;

/**
 * A rule-based selector with a tweak to test a spell is usefully castable.
 * @author Sean A. Irvine
 */
public class Strategiser extends OrdinarySelector {

  /**
   * Construct a selector for the specified wizard and world.
   * @param wizard the wizard
   * @param world the world
   * @param castMaster casting rules
   */
  public Strategiser(final Wizard wizard, final World world, final CastMaster castMaster) {
    super(wizard, world, castMaster);
  }

  private Set<Cell> getLegalTargets(final Wizard caster, final Castable c, final int casterCell) {
    final int range = c.getCastRange() + caster.get(PowerUps.WAND);
    final boolean los = (c.getCastFlags() & Castable.CAST_LOS) != 0;
    final Set<Cell> potentialSet = mWorld.getCells(casterCell, 0, range, los);
    // Eliminate those which are illegal
    potentialSet.removeIf(cell -> !mCastMaster.isLegalCast(mWizard, c, casterCell, cell.getCellNumber()));
    return potentialSet;
  }

  @Override
  protected int getScore(final Castable c, final int[] s, final Cell casterCell) {
    final double multiplicityAdjustment;
    if (!(c instanceof FreeCastable)) {
      final Set<Cell> potentialSet = getLegalTargets(mWizard, c, casterCell.getCellNumber());
      //System.out.println(c + " " + potentialSet.size());
      if (c instanceof TargetFilter) {
        ((TargetFilter) c).filter(potentialSet, mWizard, mWorld);
      }
      if (potentialSet.isEmpty()) {
        return 0;
      }
      final int multiplicity = c instanceof Multiplicity ? ((Multiplicity) c).getMultiplicity() : 1;
      multiplicityAdjustment = Math.min(1, potentialSet.size() / (double) multiplicity);
    } else {
      multiplicityAdjustment = 1;
    }
    return (int) (super.getScore(c, s, casterCell) / multiplicityAdjustment);
  }
}
