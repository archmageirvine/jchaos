package chaos.graphics;

/**
 * Interface specifying the plotting of a weapon.
 * @author Sean A. Irvine
 */
interface Plotter {

  /**
   * Draw something at suggested coordinates.  May draw complex
   * shapes, but it is the plotter's responsibility to keep
   * track of them for later deletion.
   * @param x x-coordinate
   * @param y y-coordinate
   */
  void plot(final int x, final int y);
}
