package chaos.common.growth;

import chaos.common.Actor;
import chaos.common.Attribute;

/**
 * Magma.
 * @author Sean A. Irvine
 */
public class Magma extends Earthquake {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 63);
  }

  @Override
  public Class<? extends Actor> sproutClass() {
    return Earthquake.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Earthquake.class;
  }

  @Override
  public int promotionCount() {
    return 1000;
  }
}
