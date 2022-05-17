package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;

/**
 * Nullify.
 *
 * @author Sean A. Irvine
 */
public class Nullify extends AbstractDecrement {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }

  @Override
  public int getCastRange() {
    return 9;
  }

  @Override
  public int decrement() {
    return Attribute.SPECIAL_COMBAT.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.SPECIAL_COMBAT;
  }
}
