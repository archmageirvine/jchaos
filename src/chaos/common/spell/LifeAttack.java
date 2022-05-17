package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;

/**
 * Magic attack.  These words appeared in Spectrum Chaos, but no such spell was
 * actually implemented.
 *
 * @author Sean A. Irvine
 */
public class LifeAttack extends AbstractDecrement {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_LOS | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return 10;
  }

  @Override
  public int decrement() {
    return 10;
  }

  @Override
  public Attribute attribute() {
    return Attribute.LIFE;
  }

}
