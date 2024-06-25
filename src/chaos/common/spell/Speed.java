package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;
import chaos.common.Multiplicity;

/**
 * Speed.
 * @author Sean A. Irvine
 */
public class Speed extends AbstractShield implements Multiplicity {
  @Override
  public int increment() {
    return Attribute.MOVEMENT.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.MOVEMENT;
  }

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
}
