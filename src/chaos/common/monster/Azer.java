package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Unicaster;
import chaos.common.free.Quench;

/**
 * The azer.
 * @author Sean A. Irvine
 */
public class Azer extends Unicaster implements Humanoid {
  {
    mDelay = 280;
    mCastClass = Quench.class;
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x003C7CFE7E183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Bandit.class;
  }
}
