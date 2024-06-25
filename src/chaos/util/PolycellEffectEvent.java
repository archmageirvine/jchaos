package chaos.util;

import java.util.Collection;

import chaos.board.Cell;
import chaos.common.Actor;

/**
 * A multiple cell effect event.
 * @author Sean A. Irvine
 */
public class PolycellEffectEvent implements Event {

  /** The type of cell event */
  private final CellEffectType mEvent;
  /** The cells involved */
  private final Collection<Cell> mCells;
  /** Cause of the event. */
  private final Actor mCause;

  /**
   * Construct a new cell effect event.
   * @param cells the cells involved
   * @param eventType the type of cell event
   * @param cause the castable causing this event (may be null)
   * @throws NullPointerException if cells is null.
   * @throws IllegalArgumentException if event type is not known.
   */
  public PolycellEffectEvent(final Collection<Cell> cells, final CellEffectType eventType, final Actor cause) {
    if (cells == null) {
      throw new NullPointerException();
    }
    mCells = cells;
    mEvent = eventType == null ? CellEffectType.NON_EVENT : eventType;
    mCause = cause;
  }

  /**
   * Construct a new cell effect event.
   * @param cells the cells involved
   * @param eventType the type of cell event
   * @throws NullPointerException if cells is null.
   * @throws IllegalArgumentException if event type is not known.
   */
  public PolycellEffectEvent(final Collection<Cell> cells, final CellEffectType eventType) {
    this(cells, eventType, null);
  }

  /**
   * Return the type of this cell event.
   * @return cell event type
   */
  public CellEffectType getEventType() {
    return mEvent;
  }

  /**
   * Get the cells.
   * @return set of cells
   */
  public Collection<Cell> getCells() {
    return mCells;
  }

  /**
   * Return the cause of this event if any.
   * @return cause or null
   */
  public Actor getCause() {
    return mCause;
  }
}
