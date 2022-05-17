package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Assurance.
 *
 * @author Sean A. Irvine
 */
public class Assurance extends AbstractIncrement {

  @Override
  public int increment() {
    return 12;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }
}
