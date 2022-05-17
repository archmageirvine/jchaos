package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;

/**
 * Disease.
 *
 * @author Sean A. Irvine
 */
public class Disease extends AbstractDecrement {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public int decrement() {
    return 8;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE_RECOVERY;
  }
}
