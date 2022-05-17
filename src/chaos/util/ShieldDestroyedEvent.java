package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;

/**
 * A cell effect event.
 *
 * @author Sean A. Irvine
 */
public class ShieldDestroyedEvent extends CellEffectEvent {

  /** Attribute for this event. */
  private final Attribute mAttr;

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param cause the cause of this event
   * @param attr attribute this event is for
   */
  public ShieldDestroyedEvent(final int cell, final Actor cause, final Attribute attr) {
    super(cell, CellEffectType.SHIELD_DESTROYED, cause);
    mAttr = attr;
  }

  /**
   * Construct a new cell effect event.
   *
   * @param cell the cell effected
   * @param cause the cause of this event
   * @param attr attribute this event is for
   */
  public ShieldDestroyedEvent(final Cell cell, final Actor cause, final Attribute attr) {
    this(cell.getCellNumber(), cause, attr);
  }

  /**
   * The attribute this event is for. May be null.
   *
   * @return the attribute
   */
  public Attribute getAttribute() {
    return mAttr;
  }
}
