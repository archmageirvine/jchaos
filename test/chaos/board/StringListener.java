package chaos.board;

import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.TextEvent;

/**
 * Testing class to look for a particular message.
 * @author Sean A. Irvine
 */
class StringListener implements EventListener {
  private final String mExpected;
  private boolean mSeen = false;

  StringListener(final String expected) {
    mExpected = expected;
  }

  @Override
  public void update(final Event event) {
    //      System.err.println(event.toString());
    if (event instanceof TextEvent && mExpected.equals(event.toString())) {
      mSeen = true;
    }
  }

  boolean seen() {
    return mSeen;
  }
}

