package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.monster.Lion;

/**
 * Golden dragon.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class GoldenDragon extends Dragon implements Bonus {
  {
    setDefault(Attribute.LIFE, 52);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.COMBAT, 10);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGED_COMBAT, 8);
    setDefault(Attribute.RANGE, 4);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public long getLosMask() {
    return 0x78FEFFFFFFFF3F0FL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Lion.class;
  }
}
