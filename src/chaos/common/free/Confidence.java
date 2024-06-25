package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Confidence.
 * @author Sean A. Irvine
 */
public class Confidence extends FreePowerUp {
  @Override
  protected int getPowerUpCount() {
    return 3;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.CONFIDENCE;
  }
}
