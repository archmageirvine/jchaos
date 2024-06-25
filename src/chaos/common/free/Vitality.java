package chaos.common.free;

import chaos.common.AbstractIncrement;
import chaos.common.Attribute;

/**
 * Vitality.
 * @author Sean A. Irvine
 */
public class Vitality extends AbstractIncrement {

  @Override
  public int increment() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE_RECOVERY;
  }
}
