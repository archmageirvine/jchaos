package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Vulture.
 * @author Sean A. Irvine
 */
public class Vulture extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 14);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.INTELLIGENCE, 33);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 6);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x0000007C7C000000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Bat.class;
  }
}
