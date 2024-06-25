package chaos.common;

import chaos.board.Cell;
import chaos.board.World;

/**
 * Encapsulates spells that do not require selection of a target.
 * @author Sean A. Irvine
 */
public abstract class FreeCastable extends Castable {

  @Override
  public int getCastRange() {
    // These spells have infinite range, which it is convenient to represent by zero.
    return 0;
  }

  @Override
  public int getCastFlags() {
    // these spells generally have no special flags
    return 0;
  }

  /**
   * Cast the spell as the indicated caster.  This type of spell does not require
   * the selection of a target, hence the implementation of the actual spell can
   * be done purely from the caster's viewpoint.  This should never be called with
   * a null caster or world, but implementations should avoid throwing an exception
   * if this situation does arise.
   * @param world the world where this cast takes place
   * @param caster caster casting this castable
   * @param casterCell the cell containing the caster
   */
  public abstract void cast(final World world, final Caster caster, final Cell casterCell);

  @Override
  public final void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    cast(world, caster, casterCell);
  }

}
