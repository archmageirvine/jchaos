package chaos.util;

/**
 * Defines the methods implemented by any event generators.
 * @author Sean A. Irvine
 */
public interface EventGenerator {

  /**
   * Add the specified <code>EventListener</code> to the queue of those
   * who receive events from this object.
   * @param el an <code>EventListener</code>
   * @throws NullPointerException if <code>el</code> is null.
   */
  void register(EventListener el);

  /**
   * Remove the specified <code>EventListener</code> from the queue of
   * those who receive events from this object.
   * @param el <code>EventListener</code> to remove.
   */
  void deregister(EventListener el);

  /**
   * Send an <code>Event</code> to all registered <code>EventListener</code>.
   * @param event event to send
   */
  void notify(Event event);
}
