package chaos.util;

import java.util.HashSet;

/**
 * Superclass for all classes capable of generating <code>Event</code>
 * notifications.  The event generation is done in a thread safe manner.
 * @author Sean A. Irvine
 */
public class DefaultEventGenerator implements EventGenerator {

  /** List of objects listening to events. */
  private final HashSet<EventListener> mListeners = new HashSet<>();

  @Override
  public void register(final EventListener el) {
    if (el == null) {
      throw new NullPointerException();
    }
    synchronized (mListeners) {
      mListeners.add(el);
    }
  }

  @Override
  public void deregister(final EventListener el) {
    synchronized (mListeners) {
      mListeners.remove(el);
    }
  }

  @Override
  public void notify(final Event event) {
    synchronized (mListeners) {
      for (final EventListener el : mListeners) {
        el.update(event);
      }
    }
  }
}
