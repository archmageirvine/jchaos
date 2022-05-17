package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Jaguar.
 * @author Sean A. Irvine
 */
public class Jaguar extends MaterialMonster implements Cat {
  {
    setDefault(Attribute.LIFE, 26);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 53);
    setDefault(Attribute.COMBAT, 9);
    setDefault(Attribute.AGILITY, 67);
    setDefault(Attribute.MOVEMENT, 6);
  }

  @Override
  public long getLosMask() {
    return 0x00000006FEFE6600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return NeoOtyugh.class;
  }
}
