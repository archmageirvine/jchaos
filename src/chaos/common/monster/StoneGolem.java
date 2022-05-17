package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Stone golem.
 *
 * @author Sean A. Irvine
 */
public class StoneGolem extends MaterialMonster implements Bonus, Humanoid {
  {
    setDefault(Attribute.LIFE, 45);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public long getLosMask() {
    return 0x0010383C3C383800L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Baboon.class;
  }
}
