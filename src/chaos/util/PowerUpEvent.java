package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.PowerUps;

/**
 * A cell effect event.
 * @author Sean A. Irvine
 */
public class PowerUpEvent extends CellEffectEvent {

  /** PowerUps for this event. */
  private final PowerUps mAttr;

  /**
   * Construct a new cell effect event.
   * @param cell the cell effected
   * @param actor the actor this event is for
   * @param attr attribute this event is for
   */
  public PowerUpEvent(final int cell, final Actor actor, final PowerUps attr) {
    super(cell, CellEffectType.POWERUP, actor);
    mAttr = attr;
  }

  /**
   * Construct a new cell effect event.
   * @param cell the cell effected
   * @param actor the actor this event is for
   * @param attr attribute this event is for
   */
  public PowerUpEvent(final Cell cell, final Actor actor, final PowerUps attr) {
    this(cell.getCellNumber(), actor, attr);
  }

  /**
   * The attribute this event is for. May be null.
   * @return the attribute
   */
  public PowerUps getPowerUp() {
    return mAttr;
  }
}
