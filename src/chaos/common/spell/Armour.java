package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;

/**
 * Armour.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Armour extends AbstractShield {
  @Override
  public int increment() {
    return 50;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE;
  }

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
}
