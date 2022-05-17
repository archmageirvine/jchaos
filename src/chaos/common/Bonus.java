package chaos.common;

/**
 * Indicates bonus spells should be awarded for a kill of this
 * Actor.
 *
 * @author Sean A. Irvine
 */
public interface Bonus {

  /**
   * Return the number of bonus spells for killing this Actor.
   *
   * @return number of bonus spells
   */
  int getBonus();
}
