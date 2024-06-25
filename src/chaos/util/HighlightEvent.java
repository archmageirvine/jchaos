package chaos.util;

import java.util.Set;

/**
 * An event indicating cells should be highlighted.
 * @author Sean A. Irvine
 */
public class HighlightEvent implements Event {

  /** The cells involved */
  private final Set<Integer> mCells;

  /**
   * Highlight event for the specified cells.
   * @param cells the cells involved, may be null.
   */
  public HighlightEvent(final Set<Integer> cells) {
    mCells = cells;
  }

  /**
   * Get the cells.
   * @return set of cells
   */
  public Set<Integer> getCells() {
    return mCells;
  }
}
