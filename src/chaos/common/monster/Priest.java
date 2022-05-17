package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Unicaster;
import chaos.common.spell.Subversion;

/**
 * Priest.
 * @author Sean A. Irvine
 */
public class Priest extends Unicaster implements Humanoid {

  {
    mDelay = 10;
    mCastClass = Subversion.class;
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.MAGICAL_RESISTANCE, 68);
    setDefault(Attribute.AGILITY, 26);
    setDefault(Attribute.INTELLIGENCE, 12);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.SPECIAL_COMBAT, 1);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.MAGICAL_RESISTANCE_RECOVERY);
  }

  @Override
  public long getLosMask() {
    return 0x094DFEFF3E3E7C6CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Aesculapius.class;
  }
}
