package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Bless.
 *
 * @author Sean A. Irvine
 */
public class Bless extends AbstractIncrement {

  @Override
  public int increment() {
    return 2;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE;
  }
}
