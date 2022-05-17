package chaos.common.free;

import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;

/**
 * Amulet.
 *
 * @author Sean A. Irvine
 */
public class Amulet extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return Attribute.INTELLIGENCE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.INTELLIGENCE;
  }
}
