package chaos.common;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;

/**
 * Defines an interface that can be used to filter a set of cells.
 * This can be used by the AI system, to filter out or select appropriate
 * cells for spell casting.
 *
 * @author Sean A. Irvine
 */
public interface TargetFilter {

  /**
   * Filter a set of target cells.  The resulting set can contain any
   * or none of the given cells, but should not contain cells not
   * appearing in the given set.  It is assumed that the supplied set
   * is not null.
   *
   * @param targets the possible targets
   * @param caster the caster
   * @param world the world
   */
  void filter(final Set<Cell> targets, final Caster caster, final World world);

}
