package chaos.common.free;

import chaos.common.AbstractFreeDecrement;
import chaos.common.Attribute;

/**
 * Countermand.
 * @author Sean A. Irvine
 */
public class Countermand extends AbstractFreeDecrement {
  @Override
  public int decrement() {
    return 10;
  }

  @Override
  public Attribute attribute() {
    return Attribute.AGILITY;
  }
}
