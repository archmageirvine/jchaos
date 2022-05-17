package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Emerald dragon.
 *
 * @author Sean A. Irvine
 */
public class EmeraldDragon extends Dragon implements Bonus {
  {
    setDefault(Attribute.LIFE, 48);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.INTELLIGENCE, 80);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGED_COMBAT, 15);
    setDefault(Attribute.RANGE, 9);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getBonus() {
    return 4;
  }
  @Override
  public long getLosMask() {
    return 0x1F3EFEFF7FFFF676L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GreenDragon.class;
  }
}
