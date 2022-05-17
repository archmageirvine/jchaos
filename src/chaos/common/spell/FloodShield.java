package chaos.common.spell;

import chaos.common.Multiplicity;
import chaos.common.PowerUps;

/**
 * Flood shield.
 *
 * @author Sean A. Irvine
 */
public class FloodShield extends AbstractPowerUp implements Multiplicity {

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.FLOOD_SHIELD;
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
