package chaos.common;

/**
 * Indicates castable confers some power-up on the wizard.  This should
 * only be implemented by certain FreeCastable spells.
 * @author Sean A. Irvine
 */
public interface PowerUp {

  /**
   * Return the type of power-up in accordance with constants in Wizard.
   * @return number of bonus spells
   */
  PowerUps getPowerUpType();
}
