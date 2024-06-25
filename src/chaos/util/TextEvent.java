package chaos.util;

/**
 * Provides a way of sending simple text events.
 * @author Sean A. Irvine
 */
public class TextEvent implements Event {

  /** The message */
  private final String mMessage;

  /**
   * Construct a new text message with the specified string.
   * @param message the message
   */
  public TextEvent(final String message) {
    if (message == null) {
      throw new NullPointerException();
    }
    mMessage = message;
  }

  /**
   * Return the message.
   * @return the message
   */
  public String toString() {
    return mMessage;
  }
}
