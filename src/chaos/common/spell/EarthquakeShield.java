package chaos.common.spell;

import chaos.common.Multiplicity;
import chaos.common.PowerUps;

/**
 * Earthquake shield.
 * @author Sean A. Irvine
 */
public class EarthquakeShield extends AbstractPowerUp implements Multiplicity {

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.EARTHQUAKE_SHIELD;
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
