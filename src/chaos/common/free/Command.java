package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Command.
 *
 * @author Sean A. Irvine
 */
public class Command extends AbstractIncrement {

  @Override
  public int increment() {
    return 12;
  }

  @Override
  public Attribute attribute() {
    return Attribute.AGILITY;
  }
}
