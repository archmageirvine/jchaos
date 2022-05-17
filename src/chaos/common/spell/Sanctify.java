package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.Attribute;

/**
 * Sanctify.
 *
 * @author Sean A. Irvine
 */
public class Sanctify extends AbstractShield {
  @Override
  public int increment() {
    return 7;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE_RECOVERY;
  }

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 12;
  }
}
