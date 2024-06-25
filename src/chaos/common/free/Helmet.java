package chaos.common.free;

import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;

/**
 * Helmet.
 * @author Sean A. Irvine
 */
public class Helmet extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return Attribute.LIFE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE;
  }
}
