package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Venom.
 * @author Sean A. Irvine
 */
public class Venom extends AbstractIncrement {

  @Override
  public int increment() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.SPECIAL_COMBAT;
  }
}
