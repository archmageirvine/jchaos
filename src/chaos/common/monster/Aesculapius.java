package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;

/**
 * Aesculapius.
 * @author Sean A. Irvine
 */
public class Aesculapius extends Monster implements Bonus, Humanoid {
  {
    setDefault(Attribute.LIFE, 34);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 29);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 100);
    setDefault(Attribute.COMBAT_RECOVERY, 4);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.AGILITY_RECOVERY, 1);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.COMBAT, -4);
    setDefault(Attribute.RANGED_COMBAT, -1);
    setDefault(Attribute.SPECIAL_COMBAT, -4);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    set(PowerUps.ARCHERY, 1);
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public int getBonus() {
    return 4;
  }

  @Override
  public long getLosMask() {
    return 0xFCFC743F3F3F3E3EL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Hippocrates.class;
  }
}
