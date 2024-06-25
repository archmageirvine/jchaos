package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Harpy.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Harpy extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 14);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 87);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x001E7E3C3C3C3800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return FireBat.class;
  }
}
