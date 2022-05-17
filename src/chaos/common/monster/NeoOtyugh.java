package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Neo-otyugh.
 *
 * @author Sean A. Irvine
 */
public class NeoOtyugh extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 33);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.RANGED_COMBAT, 3);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x087FCF7FFEFEBE2AL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }
}
