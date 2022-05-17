package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.Singleton;

/**
 * Alien 8.
 * @author Sean A. Irvine
 * @author Tim Stamper
 */
public class Alien8 extends MythosMonster implements Bonus, Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 56);
    setDefault(Attribute.MAGICAL_RESISTANCE, 18);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.INTELLIGENCE, 32);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.SPECIAL_COMBAT, 2);
    setSpecialCombatApply(Attribute.INTELLIGENCE_RECOVERY);
  }

  @Override
  public long getLosMask() {
    return 0x3C7E3E7E7E7EFE1CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public int getBonus() {
    return 1;
  }
}
