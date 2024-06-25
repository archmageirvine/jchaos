package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;

/**
 * Magic bolt.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicBolt extends AbstractDecrement {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public int decrement() {
    return 30;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }
}
