package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Anger.
 * @author Sean A. Irvine
 */
public class Anger extends AbstractIncrement {

  @Override
  public int increment() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.COMBAT;
  }
}
