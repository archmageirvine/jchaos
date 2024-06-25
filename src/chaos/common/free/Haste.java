package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Haste.
 * @author Sean A. Irvine
 */
public class Haste extends AbstractIncrement {

  @Override
  public int increment() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MOVEMENT;
  }
}
