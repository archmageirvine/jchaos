package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Lich lord.
 *
 * @author Sean A. Irvine
 */
public class LichLord extends FreePowerUp {
  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.LICH_LORD;
  }
  @Override
  protected boolean cumulative() {
    return true;
  }
}
