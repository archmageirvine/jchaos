package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * King cobra.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class KingCobra extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 2);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 1);
    setCombatApply(Attribute.LIFE_RECOVERY);
    setRangedCombatApply(Attribute.LIFE_RECOVERY);
  }

  @Override
  public long getLosMask() {
    return 0x00001838182C1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }
}
