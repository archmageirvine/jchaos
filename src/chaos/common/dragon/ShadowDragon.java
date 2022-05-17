package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Shadow dragon.
 *
 * @author Sean A. Irvine
 */
public class ShadowDragon extends Dragon implements Bonus {
  {
    setDefault(Attribute.LIFE, 49);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 47);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGED_COMBAT, 4);
    setDefault(Attribute.RANGE, 5);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public long getLosMask() {
    return 0x062E77FE9FBEF6FCL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return chaos.common.monster.GrayElf.class;
  }
}
