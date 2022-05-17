package chaos.graphics;

/**
 * Draw the current scores.
 *
 * @author Sean A. Irvine
 */
public interface ScoreDisplay {

  /**
   * Produce a display containing the current scores.
   *
   * @param turn turn number.
   */
  void showScores(final int turn);
}
