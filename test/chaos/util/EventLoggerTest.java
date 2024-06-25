package chaos.util;

import chaos.board.CellEvent;
import chaos.board.World;
import irvine.StandardIoTestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class EventLoggerTest extends StandardIoTestCase {

  private static final String LS = System.lineSeparator();

  private static class MyEvent implements Event {
  }

  public void test() {
    final World w = new World(5, 5);
    final EventLogger g = new EventLogger(w);
    g.update(new TextEvent("hello"));
    g.update(new CellEffectEvent(1, CellEffectType.NON_EVENT));
    g.update(new MyEvent());
    g.update(new CellEvent(6));
    assertEquals("hello" + LS + "Unknown event type: chaos.util.EventLoggerTest$MyEvent" + LS
      + "cell(6) now NOTHING" + LS, getOut());
  }
}
