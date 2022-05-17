package chaos.util;

import chaos.common.Actor;

/**
 * Attack on a cell event.
 *
 * @author Sean A. Irvine
 */
public class AttackCellEffectEvent extends CellEffectEvent {

  /** The damage done by this attack. */
  private final int mDamage;
  /** The actor performing the attack. */
  private final Actor mOffence;
  /** The type of attack. */
  private final int mType;

  /**
   * Construct a new attack cell effect event.
   *
   * @param cell the cell this event is for
   * @param damage the damage incurred
   * @param offence the cell making the attack
   * @param type combat type
   */
  public AttackCellEffectEvent(final int cell, final int damage, final Actor offence, final int type) {
    super(cell, CellEffectType.ATTACK_EVENT);
    mDamage = damage;
    mOffence = offence;
    mType = type;
  }

  /**
   * Return the damage recorded by this event.
   *
   * @return damage value
   */
  public int getDamage() {
    return mDamage;
  }

  /**
   * Return the actor making the attack.
   *
   * @return offence cell
   */
  public Actor getOffence() {
    return mOffence;
  }

  /**
   * Get the type of the attack.
   *
   * @return attack type
   */
  public int getType() {
    return mType;
  }

}
