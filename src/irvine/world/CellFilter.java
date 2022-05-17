package irvine.world;

/**
 * Defines a single method used to filter cells according to an criterion.
 *
 * @param <C> fundamental cell type of the world.
 *
 * @author Sean A. Irvine
 */
public interface CellFilter<C> {

  /**
   * Acceptance test for a cell.  Apply some criterion to <code>cell</code>,
   * if the criterion passes then returns true, otherwise false.  The filter
   * should also handle <code>null</code> (generally by returning false).
   *
   * @param cell cell to test
   * @return true if the cell is acceptable
   */
  boolean accept(final C cell);
}
