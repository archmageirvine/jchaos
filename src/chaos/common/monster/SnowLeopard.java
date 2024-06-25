package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Snow leopard.
 * @author Sean A. Irvine
 */
public class SnowLeopard extends MaterialMonster implements Cat {
  {
    setDefault(Attribute.LIFE, 21);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 47);
    setDefault(Attribute.COMBAT, 9);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 5);
  }

  @Override
  public long getLosMask() {
    return 0x00000006FFFE6600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Leopard.class;
  }
}
