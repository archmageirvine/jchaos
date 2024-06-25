package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Python.
 * @author Sean A. Irvine
 */
public class Python extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 2);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 13);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 87);
    setDefault(Attribute.AGILITY_RECOVERY, 19);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x00000070303C3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return KingCobra.class;
  }
}
