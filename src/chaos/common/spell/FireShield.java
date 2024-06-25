package chaos.common.spell;

import chaos.common.Multiplicity;
import chaos.common.PowerUps;

/**
 * Fire shield.
 * @author Sean A. Irvine
 */
public class FireShield extends AbstractPowerUp implements Multiplicity {

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.FIRE_SHIELD;
  }

  @Override
  public int getCastRange() {
    return 100;
  }

  @Override
  public int getMultiplicity() {
    return 3;
  }
}
