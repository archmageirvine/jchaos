package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Tiger.
 * @author Sean A. Irvine
 */
public class Tiger extends MaterialMonster implements Cat {
  {
    setDefault(Attribute.LIFE, 45);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 73);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 4);
  }

  @Override
  public long getLosMask() {
    return 0x00000006FEFE6600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Lion.class;
  }
}
