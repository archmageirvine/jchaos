package chaos.common.free;

import chaos.common.PowerUps;

/**
 * Battle cry.
 * @author Sean A. Irvine
 */
public class BattleCry extends FreePowerUp {
  @Override
  protected int getPowerUpCount() {
    // this is two because it decrements in the updater, i.e.
    // immediately after casting
    return 2;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.BATTLE_CRY;
  }
}
