package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterRide;
import chaos.common.Monster;

/**
 * Elephant.
 * @author Sean A. Irvine
 */
public class Elephant extends MaterialMonsterRide {
  {
    setDefault(Attribute.LIFE, 39);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE, 42);
    setDefault(Attribute.COMBAT, 7);
    setDefault(Attribute.AGILITY, 26);
    setDefault(Attribute.MOVEMENT, 2);
  }

  @Override
  public long getLosMask() {
    return 0x0000FF7F7F6F6C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Leopard.class;
  }
}
