package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;
import chaos.common.Multiplicity;

/**
 * Protection.
 * @author Sean A. Irvine
 */
public class Protection extends AbstractShield implements Multiplicity {
  @Override
  public int increment() {
    return Attribute.MAGICAL_RESISTANCE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }

  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public int getMultiplicity() {
    return 3;
  }
}
