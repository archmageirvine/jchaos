package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.Singleton;
import chaos.common.monster.Robot;

/**
 * Quazatron.
 * @author Sean A. Irvine
 * @author Steve Turner
 */
public class Quazatron extends MythosMonster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 56);
    setDefault(Attribute.MAGICAL_RESISTANCE, 19);
    setDefault(Attribute.AGILITY, 6);
    setDefault(Attribute.INTELLIGENCE, 85);
    setDefault(Attribute.MOVEMENT, 3);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.RANGE, 5);
    setDefault(Attribute.RANGED_COMBAT, 7);
    setCombatApply(Attribute.INTELLIGENCE_RECOVERY);
    setRangedCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x003C3C3C3C3C3C18L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Robot.class;
  }
}
