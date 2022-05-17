package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;

/**
 * Swiss knife.
 * @author Sean A. Irvine
 */
public class DiamondSword extends MythosMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 30);
    setDefault(Attribute.LIFE_RECOVERY, 0);
    setDefault(Attribute.MAGICAL_RESISTANCE, 23);
    setDefault(Attribute.AGILITY, 45);
    setDefault(Attribute.INTELLIGENCE, 1);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 8);
  }
  @Override
  public long getLosMask() {
    return 0xC3E77E7E7E7EFFC3L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return SwissKnife.class;
  }
  @Override
  public int getCastRange() {
    return 20;
  }
}
