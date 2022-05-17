package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Magic wand.
 *
 * @author Sean A. Irvine
 */
public class MagicWand extends FreePowerUp {
  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.WAND;
  }
  @Override
  protected boolean cumulative() {
    return true;
  }
  @Override
  protected int getPowerUpCount() {
    return 2;
  }
}
