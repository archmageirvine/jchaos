package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Double.
 * @author Sean A. Irvine
 */
public class Double extends FreePowerUp {
  @Override
  protected int getPowerUpCount() {
    return 5;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.DOUBLE;
  }

  @Override
  protected boolean cumulative() {
    return true;
  }
}
