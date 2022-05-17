package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Unicaster;
import chaos.common.spell.Vanish;

/**
 * Chronomancer.
 *
 * @author Sean A. Irvine
 */
public class Chronomancer extends Unicaster implements Humanoid {
  {
    mDelay = 6;
    mCastClass = Vanish.class;
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.INTELLIGENCE, 100);
  }
  @Override
  public long getLosMask() {
    return 0x7E7E7E3C383C3C3CL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Dao.class;
  }
}
