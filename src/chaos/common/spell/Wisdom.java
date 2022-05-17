package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;
import chaos.common.Multiplicity;

/**
 * Wisdom.
 *
 * @author Sean A. Irvine
 */
public class Wisdom extends AbstractShield implements Multiplicity {
  @Override
  public int increment() {
    return Attribute.INTELLIGENCE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.INTELLIGENCE;
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
