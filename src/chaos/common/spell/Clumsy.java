package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;
import chaos.common.Multiplicity;

/**
 * Clumsy.
 *
 * @author Sean A. Irvine
 */
public class Clumsy extends AbstractDecrement implements Multiplicity {
  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public int getMultiplicity() {
    return 3;
  }
  @Override
  public int decrement() {
    return Attribute.AGILITY.max();
  }
  @Override
  public Attribute attribute() {
    return Attribute.AGILITY;
  }
}
