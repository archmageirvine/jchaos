package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Platinum dragon.
 *
 * @author Sean A. Irvine
 */
public class PlatinumDragon extends Dragon implements Bonus {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 14);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 7);
    setDefault(Attribute.COMBAT, 11);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.AGILITY_RECOVERY, 7);
    setDefault(Attribute.MOVEMENT, 5);
    setDefault(Attribute.RANGED_COMBAT, 10);
    setDefault(Attribute.RANGE, 7);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getBonus() {
    return 3;
  }

  @Override
  public long getLosMask() {
    return 0xFEFFFF7F7F3FFFFFL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return GoldenDragon.class;
  }
}
