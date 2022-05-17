package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Unicaster;
import chaos.common.Virtuous;
import chaos.common.spell.Sleep;

/**
 * Faun.
 *
 * @author Sean A. Irvine
 */
public class Faun extends Unicaster implements Humanoid, Virtuous {
  {
    mDelay = 40;
    mCastClass = Sleep.class;
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE, 86);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.AGILITY, 47);
    setDefault(Attribute.INTELLIGENCE, 80);
  }
  @Override
  public long getLosMask() {
    return 0x00387C7C1C1C1C00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Druid.class;
  }
}
