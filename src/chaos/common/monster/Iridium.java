package chaos.common.monster;

import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * Iridium.
 *
 * @author Sean A. Irvine
 */
public class Iridium extends MaterialMonsterMount implements Bonus, AttacksUndead {
  {
    setDefault(Attribute.LIFE, 48);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.INTELLIGENCE, 87);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 100);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.COMBAT_RECOVERY, 2);
    setDefault(Attribute.AGILITY, 60);
    setDefault(Attribute.AGILITY_RECOVERY, 7);
    setDefault(Attribute.MOVEMENT, 6);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.RANGED_COMBAT, 13);
    setDefault(Attribute.SPECIAL_COMBAT, 2);
    setRangedCombatApply(Attribute.INTELLIGENCE);
  }
  @Override
  public int getBonus() {
    return 6;
  }
  @Override
  public int getCastRange() {
    return 5;
  }
  @Override
  public long getLosMask() {
    return 0x0000E0FF7E7E2400L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Thundermare.class;
  }
}
