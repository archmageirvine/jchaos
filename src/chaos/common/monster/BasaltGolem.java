package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Realm;

/**
 * The basalt golem.
 * @author Sean A. Irvine
 */
public class BasaltGolem extends Monster implements Bonus, Humanoid {
  {
    setDefault(Attribute.LIFE, 42);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.RANGED_COMBAT, 2);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.MOVEMENT, 5);
    setDefault(Attribute.AGILITY, 7);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.MOVEMENT);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getBonus() {
    return 3;
  }

  @Override
  public long getLosMask() {
    return 0x00081C3E3E1C1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return StoneGolem.class;
  }
}
