package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * The amphisbaena.
 * @author Sean A. Irvine
 */
public class Amphisbaena extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 4);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.AGILITY, 93);
    setDefault(Attribute.AGILITY_RECOVERY, 12);
    setDefault(Attribute.MOVEMENT, 2);
  }

  @Override
  public long getLosMask() {
    return 0x00003E3E7E7F7E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return KingCobra.class;
  }
}
