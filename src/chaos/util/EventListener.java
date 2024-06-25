package chaos.util;

/**
 * Interface implemented by all classes wanting to receive
 * <code>Event</code>s.
 * @author Sean A. Irvine
 */
public interface EventListener {

  /**
   * Called when an event is generated on an <code>EventGenerator</code>
   * for which this object is a registered listener.
   * @param event the event
   */
  void update(Event event);

}
