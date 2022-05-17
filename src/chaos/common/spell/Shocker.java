package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;

/**
 * Shocker.
 *
 * @author Sean A. Irvine
 */
public class Shocker extends AbstractShield {
  @Override
  public int increment() {
    return 10;
  }

  @Override
  public Attribute attribute() {
    return Attribute.SPECIAL_COMBAT;
  }

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_INANIMATE;
  }

  @Override
  public int getCastRange() {
    return 12;
  }

}
