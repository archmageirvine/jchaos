package chaos.common.spell;

import chaos.common.PowerUps;

/**
 * Reflector.
 * @author Sean A. Irvine
 */
public class Reflector extends AbstractPowerUp {

  @Override
  public int getCastRange() {
    return 100;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.REFLECT;
  }
}
