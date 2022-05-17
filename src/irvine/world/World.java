package irvine.world;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Provides definitions for methods that operate on a 2-dimensional grid.
 * The intended use is to represent the fundamental structure of a game
 * world.  It defines methods for obtaining basic attributes of the world
 * and for retrieving cells or groups of cells from the grid.<p>
 *
 * Actual instances of classes implementing this interface can be obtained
 * using the <code>WorldFactory</code> class.
 *
 * @param <C> fundamental cell type of the world.
 *
 * @author Sean A. Irvine
 */
public interface World<C> extends Serializable {

  /**
   * Get the width of the world.  Returns the maximum number of cells
   * in any grid row.
   *
   * @return the width of the world.
   */
  int width();

  /**
   * Get the height of the world.  Returns the maximum number of cells
   * in any grid column.
   *
   * @return the height of the world.
   */
  int height();

  /**
   * Get the total number of cells in the world.
   *
   * @return world size.
   */
  int size();

  /**
   * Test if a cell exists.  Tests if the given <code>cellNumber</code>
   * corresponds to an actual cell.
   *
   * @param cellNumber cell number to test
   * @return true if the cell exists
   */
  boolean isReal(final int cellNumber);

  /**
   * Test if a cell is an edge cell.  Tests if the given <code>cellNumber</code>
   * is an edge cell of the world.
   *
   * @param cellNumber cell number to test
   * @return true if the cell is an edge
   */
  boolean isEdge(final int cellNumber);

  /**
   * Set the content of a cell.
   *
   * @param cellNumber cell number to set
   * @param cell content to set
   * @exception IllegalArgumentException if the <code>cellNumber</code> does not
   * correspond to an actual cell.
   */
  void setCell(final int cellNumber, final C cell);

  /**
   * Get the content of a cell.  If the specified cell does not exist then
   * <code>null</code> is returned.
   *
   * @param cellNumber cell number to get cell for.
   * @return the cell
   */
  C getCell(final int cellNumber);

  /**
   * Get the content of a cell.  If the specified cell does not exist then
   * <code>null</code> is returned.
   *
   * @param x cell x-coordinate
   * @param y cell y-coordinate
   * @return the cell
   */
  C getCell(final int x, final int y);

  /**
   * Get the cell number for a given cell.  Returns the lowest cell number
   * have the specified cell as it content.  If the given cell does not exist
   * in the world then -1 is returned.
   *
   * @param cell cell to get cell number for
   * @return cell number or -1.
   */
  int getCellNumber(final C cell);

  /**
   * Convert from cell coordinates to cell number.  Given the coordinate pair
   * <code>(x,y)</code>, retrieve the corresponding cell number.  If the
   * coordinates do not denote a real cell then -1 is returned.
   *
   * @param x x-ordinate
   * @param y y-ordinate
   * @return cell number or -1.
   */
  int getCellNumber(final int x, final int y);

  /**
   * Get the cell coordinates for a given cell number.  The x-coordinate of
   * <code>cellNumber</code> is stored in <code>xy[0]</code> and the
   * y-coordinate in <code>xy[1]</code>.  Other elements of <code>xy</code>
   * are not affected.  If the cell is a real cell then true is returned
   * otherwise false is returned.
   *
   * @param cellNumber cell number to get coordinates for
   * @param xy array in which to place result
   * @return true if <code>cellNumber</code> is a real cell.
   * @exception NullPointerException if <code>xy</code> is <code>null</code>.
   * @exception ArrayIndexOutOfBoundsException if <code>xy</code> does not
   * have length at least 2.
   */
  boolean getCellCoordinates(final int cellNumber, final int[] xy);

  /**
   * Compute the squared distance between the cells with the given cell numbers.
   * If either cell number is invalid then -1 is returned.
   *
   * @param a first cell number
   * @param b second cell number
   * @return shortest squared distance between <code>a</code> and <code>b</code>
   */
  int getSquaredDistance(final int a, final int b);

  /**
   * Return the set of cells within given radii.  The set contains all cells
   * within distance <code>maxRadius</code> and at least <code>minRadius</code>
   * from the specified <code>origin</code>. If no cells match the requirements
   * then the resulting set will be empty.  Any cell in the given radius is
   * then tested with <code>filter</code> for acceptance.  If <code>filter
   * </code> is <code>null</code> then no filtering is performed.
   *
   * @param origin cell number of origin
   * @param minRadius minimum radius
   * @param maxRadius maximum radius
   * @param filter filter to apply
   * @return list of cells meeting criteria in distance order
   * @exception IllegalArgumentException if either radius is negative; or
   * <code>maxRadius</code> is less than <code>minRadius</code>; or if the
   * <code>origin</code> is not a valid cell number.
   */
  Set<C> getCells(final int origin, final int minRadius, final int maxRadius, final CellFilter<C> filter);

  /**
   * Compute shortest paths.  Attempt to compute a shortest path from <code>
   * origin</code> to <code>target</code> subject to <code>filter</code>.  If
   * the result is null then it is not possible to reach <code>target</code>
   * from <code>origin</code>; otherwise the result is a sequence of cells,
   * in order, from the first cell used after the <code>origin</code> to
   * <code>target</code> (that is, the <code>origin</code> itself is excluded
   * from the resulting list).  Cells for which <code>filter.accept()</code>
   * returns true are forbidden and cannot be used in the path or as a target.
   * If <code>filter</code> is null, then no cells are treated as forbidden.
   * The <code>filter</code> is not applied to the origin.
   *
   * <p>Vertical and horizontal movement has a cost of 1 and diagonal movement
   * has a cost of <code>sqrt(2)</code>.
   *
   * <p>Implementations should attempt to be particularly efficient for the
   * situation where this function is called several times in succession with
   * the same <code>origin</code> and <code>filter</code>.
   *
   * <p>Implementations may cache results, including previous invocations of
   * the <code>filter</code>.  To force a recomputation it is possible to pass
   * -1 as the <code>origin</code>: <code>shortestPath(-1, 0, null)</code>.
   *
   * @param origin start cell
   * @param target target cell
   * @param filter filter for cells
   * @return shortest path or null
   * @exception IllegalArgumentException if the <code>origin</code> is not
   * a valid cell or -1 or <code>target</code> is not a valid cell number.
   */
  List<C> shortestPath(final int origin, final int target, final CellFilter<C> filter);

  /**
   * Return an iterator over the cells in natural reading order.
   * @return cell iterator
   */
  Iterator<C> iterator();

  /**
   * Return an iterator over the cells in column major order.
   * @return cell iterator
   */
  Iterator<C> columnMajorIterator();

  /**
   * Return an iterator over the cells in a random order.
   * @return cell iterator
   */
  Iterator<C> randomIterator();
}
