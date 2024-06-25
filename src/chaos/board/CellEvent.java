package chaos.board;

import chaos.util.Event;

/**
 * An event indicating a cell content or state change. The
 * recipient should take appropriate action to repaint the
 * cell.
 * @author Sean A. Irvine
 */
public class CellEvent implements Event {

  /** The number of the cell affected. */
  private final int mRedraw;

  /**
   * Construct an event for a cell redraw.
   * @param cell the cell to be redrawn
   */
  public CellEvent(final int cell) {
    mRedraw = cell;
  }

  /**
   * Get the number of the cell to be redrawn.
   * @return a cell number
   */
  public int getCellNumber() {
    return mRedraw;
  }

}
