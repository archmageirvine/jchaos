package chaos.util;

/**
 * Which cell if any should have the wings icon.
 *
 * @author Sean A. Irvine
 */
public class WingsIconEvent implements Event {

  private final int mCell;

  /**
   * Display wings icon in given cell, previous cell will be cleared.
   *
   * @param cell the cell
   */
  public WingsIconEvent(final int cell) {
    mCell = cell;
  }

  /**
   * Get the cell.
   *
   * @return the cell
   */
  public int getCell() {
    return mCell;
  }
}
