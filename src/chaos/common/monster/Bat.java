package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Bat.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Bat extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 73);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0000007C7C000000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Halfling.class;
  }
}
