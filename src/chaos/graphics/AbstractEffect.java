package chaos.graphics;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Collections;

import chaos.board.Cell;

/**
 * Superclass for effects.
 * @author Sean A. Irvine
 */
public abstract class AbstractEffect {

  /**
   * Perform an effect on the specified cells. If either parameter is null
   * then no action is taken.
   * @param screen screen
   * @param graphics where to draw
   * @param cells cells to effect
   * @param width width of cell
   */
  public abstract void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width);

  /**
   * Perform an effect on the indicated cell.  If the given cell is
   * null then no action is taken.
   * @param screen screen
   * @param graphics where to draw
   * @param cell cell to effect
   * @param width width of cell
   */
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Cell cell, final int width) {
    if (cell != null) {
      performEffect(screen, graphics, Collections.singleton(cell), width);
    }
  }
}
