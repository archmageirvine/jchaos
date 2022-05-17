package chaos.common.free;

import chaos.common.AbstractFreeDecrement;
import chaos.common.Attribute;

/**
 * Insecurity.
 *
 * @author Sean A. Irvine
 */
public class Insecurity extends AbstractFreeDecrement {
  @Override
  public int decrement() {
    return 6;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }
}
