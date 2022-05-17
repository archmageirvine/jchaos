package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Clay golem.
 *
 * @author Sean A. Irvine
 */
public class ClayGolem extends MaterialMonster implements Bonus, Humanoid {
  {
    setDefault(Attribute.LIFE, 41);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.COMBAT, 10);
    setDefault(Attribute.MOVEMENT, 2);
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
    return GrizzlyBear.class;
  }
}
