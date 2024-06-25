package chaos.util;

import java.util.Collection;

import chaos.board.Cell;
import chaos.common.Actor;

/**
 * A multiple cell effect event for a shield increment spell.
 * @author Sean A. Irvine
 */
public class PolycellAttackEvent extends PolycellEffectEvent {

  private final int mDamage;

  /**
   * Construct a new event.
   * @param cells the cells involved
   * @param cause the castable causing this event (may be null)
   * @param damage the strength of the attack
   * @throws NullPointerException if cells is null.
   * @throws IllegalArgumentException if event type is not known.
   */
  public PolycellAttackEvent(final Collection<Cell> cells, final Actor cause, final int damage) {
    super(cells, CellEffectType.ATTACK_EVENT, cause);
    mDamage = damage;
  }

  /**
   * The damage.
   * @return the damage.
   */
  public int getDamage() {
    return mDamage;
  }
}
