package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;

/**
 * Green dragon.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class GreenDragon extends Dragon implements Bonus, Promotion {
  {
    setDefault(Attribute.LIFE, 39);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.COMBAT, 9);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 3);
    setDefault(Attribute.RANGED_COMBAT, 8);
    setDefault(Attribute.RANGE, 8);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0xF87C3EFFFE7F676AL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return ShadowDragon.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return EmeraldDragon.class;
  }

  @Override
  public int promotionCount() {
    return 7;
  }
}
