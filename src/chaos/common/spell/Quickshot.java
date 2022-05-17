package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;

/**
 * Quickshot.
 *
 * @author Sean A. Irvine
 */
public class Quickshot extends AbstractShield {

  @Override
  public int increment() {
    return Attribute.SHOTS.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.SHOTS;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }
}
