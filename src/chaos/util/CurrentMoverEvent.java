package chaos.util;

/**
 * An event indicating cells should be highlighted.
 * @author Sean A. Irvine
 */
public class CurrentMoverEvent implements Event {

  private final int mPlayer;

  /**
   * New player movement event.
   * @param player the currently moving player
   */
  public CurrentMoverEvent(final int player) {
    mPlayer = player;
  }

  /**
   * Get the currently moving player.
   * @return currently moving player
   */
  public int getCurrentPlayer() {
    return mPlayer;
  }
}
