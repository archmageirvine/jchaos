package chaos.common.spell;

import chaos.common.AbstractDecrement;
import chaos.common.Attribute;
import chaos.common.Multiplicity;

/**
 * Demonic touch.
 *
 * @author Sean A. Irvine
 */
public class DemonicTouch extends AbstractDecrement implements Multiplicity {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 11;
  }
  @Override
  public int getMultiplicity() {
    return 2;
  }
  @Override
  public int decrement() {
    return Attribute.COMBAT.max();
  }
  @Override
  public Attribute attribute() {
    return Attribute.COMBAT;
  }
}
