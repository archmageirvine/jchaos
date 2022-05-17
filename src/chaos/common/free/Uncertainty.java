package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Uncertainty.
 *
 * @author Sean A. Irvine
 */
public class Uncertainty extends FreePowerUp {
  @Override
  protected int getPowerUpCount() {
    return 6;
  }
  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.UNCERTAINTY;
  }
  @Override
  protected boolean cumulative() {
    return true;
  }
}
