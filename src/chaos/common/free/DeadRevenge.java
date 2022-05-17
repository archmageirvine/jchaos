package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Dead revenge.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class DeadRevenge extends FreePowerUp {

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.DEAD_REVENGE;
  }

  @Override
  protected boolean cumulative() {
    return true;
  }
}
