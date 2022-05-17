package chaos.util;

import java.util.Collection;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;

/**
 * A multiple cell effect event for a shield increment spell.
 *
 * @author Sean A. Irvine
 */
public class PolyshieldDestroyEvent extends PolycellEffectEvent {

  /** The attribute affected. */
  private final Attribute mAttr;

  /**
   * Construct a new event.
   *
   * @param cells the cells involved
   * @param attr the attribute affected
   * @param cause the castable causing this event (may be null)
   * @exception NullPointerException if cells is null.
   * @exception IllegalArgumentException if event type is not known.
   */
  public PolyshieldDestroyEvent(final Collection<Cell> cells, final Attribute attr, final Actor cause) {
    super(cells, CellEffectType.SHIELD_DESTROYED, cause);
    mAttr = attr;
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
