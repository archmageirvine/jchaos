package chaos.common.free;

import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;

/**
 * Magic shield.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicShield extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return Attribute.MAGICAL_RESISTANCE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }
}
