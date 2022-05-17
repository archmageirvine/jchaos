package chaos.util;

/**
 * Which cell if any should have the shoot icon.
 *
 * @author Sean A. Irvine
 */
public class ShootIconEvent implements Event {

  private final int mCell;

  /**
   * Display shoot icon in given cell, previous cell will be cleared.
   *
   * @param cell the cell
   */
  public ShootIconEvent(final int cell) {
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
