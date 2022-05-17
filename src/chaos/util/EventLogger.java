package chaos.util;

import chaos.board.CellEvent;
import chaos.board.World;
import chaos.common.Actor;

/**
 * An <code>EventListener</code> which simply produces a
 * textual version of events received.
 *
 * @author Sean A. Irvine
 */
public class EventLogger implements EventListener {

  private final World mWorld;

  /**
   * Creates a new logger for the specified world.
   * @param world world to log events for
   */
  public EventLogger(final World world) {
    mWorld = world;
  }

  @Override
  public void update(final Event event) {
    if (event instanceof CellEvent) {
      final int cell = ((CellEvent) event).getCellNumber();
      final Actor a = mWorld.actor(cell);
      System.out.println("cell(" + cell + ") now " + (a == null ? "NOTHING" : a.getName()));
    } else if (event instanceof TextEvent) {
      System.out.println(event.toString());
    } else if (event instanceof CellEffectEvent || event instanceof WeaponEffectEvent) {
      // do nothing
    } else {
      System.out.println("Unknown event type: " + event.getClass().getName());
    }
  }

}
