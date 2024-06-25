package chaos.common.free;

import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;

/**
 * Elixir.
 * @author Sean A. Irvine
 * @author Jonathan Levell
 */
public class Elixir extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return 7;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE_RECOVERY;
  }
}
