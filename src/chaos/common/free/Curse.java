package chaos.common.free;

import chaos.common.AbstractFreeDecrement;
import chaos.common.Attribute;

/**
 * Curse.
 *
 * @author Sean A. Irvine
 */
public class Curse extends AbstractFreeDecrement {

  @Override
  public int decrement() {
    return 2;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE;
  }
}
