package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;

/**
 * Swiss knife.
 * @author Sean A. Irvine
 */
public class SwissKnife extends MythosMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 28);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 12);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 12);
  }

  @Override
  public long getLosMask() {
    return 0x7CFEFFFFFEFEFC7CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Pliers.class;
  }

  @Override
  public int getCastRange() {
    return 8;
  }
}
