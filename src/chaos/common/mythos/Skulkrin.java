package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.monster.Derro;

/**
 * Skulkrin.
 * @author Sean A. Irvine
 * @author Mike Singleton
 */
public class Skulkrin extends MythosMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE, 32);
    setDefault(Attribute.AGILITY, 70);
    setDefault(Attribute.INTELLIGENCE, 11);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.SPECIAL_COMBAT, 15);
    setDefault(Attribute.SPECIAL_COMBAT_RECOVERY, 5);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x003C3C3E7E7E7E34L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Derro.class;
  }
}
