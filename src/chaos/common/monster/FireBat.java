package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Fire bat.
 *
 * @author Sean A. Irvine
 */
public class FireBat extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 7);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 6);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public long getLosMask() {
    return 0x00007C7C7C000000L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Bat.class;
  }
}
