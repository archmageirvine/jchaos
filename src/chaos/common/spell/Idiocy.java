package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;

/**
 * Idiocy.
 * @author Sean A. Irvine
 */
public class Idiocy extends AbstractDecrement {

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
    return 67;
  }

  @Override
  public Attribute attribute() {
    return Attribute.INTELLIGENCE;
  }
}
