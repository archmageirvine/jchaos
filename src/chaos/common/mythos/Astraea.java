package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.Unicaster;
import chaos.common.monster.Solar;

/**
 * Astraea.
 * @author Sean A. Irvine
 */
public class Astraea extends Unicaster implements Singleton {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.COMBAT, 0);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.INTELLIGENCE, 42);
    setDefault(Attribute.MOVEMENT, 1);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    mDelay = 3;
    mCastClass = Cylinder.class;
  }

  @Override
  public long getLosMask() {
    return 0L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Solar.class;
  }
}
