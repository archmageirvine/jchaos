package chaos.common.spell;

import chaos.common.PowerUps;

/**
 * Life leech.
 *
 * @author Sean A. Irvine
 */
public class LifeLeech extends AbstractPowerUp {

  @Override
  public int getCastRange() {
    return 100;
  }

  @Override
  public PowerUps getPowerUpType() {
    return PowerUps.LIFE_LEECH;
  }
}
