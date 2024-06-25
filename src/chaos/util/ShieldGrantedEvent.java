package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;

/**
 * A cell effect event.
 * @author Sean A. Irvine
 */
public class ShieldGrantedEvent extends CellEffectEvent {

  /** Attribute for this event. */
  private final Attribute mAttr;

  /**
   * Construct a new cell effect event.
   * @param cell the cell effected
   * @param actor the actor this event is for
   * @param attr attribute this event is for
   */
  public ShieldGrantedEvent(final int cell, final Actor actor, final Attribute attr) {
    super(cell, CellEffectType.SHIELD_GRANTED, actor);
    mAttr = attr;
  }

  /**
   * Construct a new cell effect event.
   * @param cell the cell effected
   * @param actor the actor this event is for
   * @param attr attribute this event is for
   */
  public ShieldGrantedEvent(final Cell cell, final Actor actor, final Attribute attr) {
    this(cell.getCellNumber(), actor, attr);
  }

  /**
   * The attribute this event is for. May be null.
   * @return the attribute
   */
  public Attribute getAttribute() {
    return mAttr;
  }
}
