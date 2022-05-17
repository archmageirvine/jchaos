package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * Camel.
 *
 * @author Sean A. Irvine
 */
public class Camel extends MaterialMonsterMount {
  {
    setDefault(Attribute.LIFE, 24);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 3);
    setDefault(Attribute.RANGE, 2);
    setDefault(Attribute.RANGED_COMBAT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x00607C3E3E3E2600L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Horse.class;
  }
}
