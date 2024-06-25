package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Red dragon.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class RedDragon extends Dragon {
  {
    setDefault(Attribute.LIFE, 40);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 87);
    setDefault(Attribute.COMBAT, 11);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 5);
    setDefault(Attribute.RANGED_COMBAT, 5);
    setDefault(Attribute.RANGE, 6);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x607EFF7FFD7F7F3FL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return ShadowDragon.class;
  }
}
