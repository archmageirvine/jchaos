package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Triple.
 * @author Sean A. Irvine
 */
public class Triple extends FreePowerUp {
  @Override
  protected int getPowerUpCount() {
    return 5;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.TRIPLE;
  }

  @Override
  protected boolean cumulative() {
    return true;
  }
}
