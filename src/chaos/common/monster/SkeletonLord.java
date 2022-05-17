package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Realm;
import chaos.common.Unicaster;
import chaos.common.Virtuous;

/**
 * Skeleton lord.
 *
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class SkeletonLord extends Unicaster implements Humanoid, Virtuous {
  {
    mDelay = 1;
    mCastClass = Skeleton.class;
    setDefault(Attribute.LIFE, 55);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 50);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.COMBAT, 11);
    setDefault(Attribute.RANGE, 1);
    setDefault(Attribute.RANGED_COMBAT, 2);
    setDefault(Attribute.SPECIAL_COMBAT, -3);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 2);
    setRealm(Realm.ETHERIC);
    setSpecialCombatApply(Attribute.LIFE);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.MAGICAL_RESISTANCE_RECOVERY);
  }

  @Override
  public long getLosMask() {
    return 0x589EFEFC7C3C1C1CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return SkeletonWarrior.class;
  }
}
