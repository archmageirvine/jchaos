package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Fortitude.
 * @author Sean A. Irvine
 */
public class Fortitude extends AbstractIncrement {

  @Override
  public int increment() {
    return 7;
  }

  @Override
  public Attribute attribute() {
    return Attribute.INTELLIGENCE;
  }
}
