package chaos.common.free;

import chaos.common.AbstractFreeDecrement;
import chaos.common.Attribute;

/**
 * Biohazard.
 *
 * @author Sean A. Irvine
 */
public class Biohazard extends AbstractFreeDecrement {

  @Override
  public int decrement() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE_RECOVERY;
  }
}
