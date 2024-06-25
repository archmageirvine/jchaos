package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Cheetah.
 * @author Sean A. Irvine
 */
public class Cheetah extends MaterialMonster implements Cat {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 47);
    setDefault(Attribute.COMBAT, 9);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 7);
  }

  @Override
  public long getLosMask() {
    return 0x00000006FEFE6600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Jaguar.class;
  }
}
