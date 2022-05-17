package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;

/**
 * A cell effect event.
 *
 * @author Sean A. Irvine
 */
public class CellEffectEvent implements Event {

  /** The type of cell event. */
  private final CellEffectType mEvent;
  /** The cell involved. */
  private final int mCell;
  /** An actor this event corresponds to, can be null. */
  private final Actor mActor;

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param eventType the type of cell event
   * @param actor the actor this event is for
   */
  public CellEffectEvent(final int cell, final CellEffectType eventType, final Actor actor) {
    mCell = cell;
    mEvent = eventType == null ? CellEffectType.NON_EVENT : eventType;
    mActor = actor;
    if (mEvent == CellEffectType.ATTACK_EVENT && !(this instanceof AttackCellEffectEvent)) {
      throw new IllegalArgumentException("ATTACK_EVENT only for AttackCellEffectEvent");
    }
  }

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param eventType the type of cell event
   * @param actor the actor this event is for
   */
  public CellEffectEvent(final Cell cell, final CellEffectType eventType, final Actor actor) {
    this(cell.getCellNumber(), eventType, actor);
  }

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param eventType the type of cell event
   */
  public CellEffectEvent(final int cell, final CellEffectType eventType) {
    this(cell, eventType, null);
  }

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param eventType the type of cell event
   */
  public CellEffectEvent(final Cell cell, final CellEffectType eventType) {
    this(cell.getCellNumber(), eventType);
  }

  /**
   * Return the type of this cell event.
   *
   * @return cell event type
   */
  public CellEffectType getEventType() {
    return mEvent;
  }

  /**
   * Get the number of the cell.
   *
   * @return a cell number
   */
  public int getCellNumber() {
    return mCell;
  }

  /**
   * Get the actor being cast.
   *
   * @return a actor
   */
  public Actor getActor() {
    return mActor;
  }

}
