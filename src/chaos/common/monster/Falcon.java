package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Virtuous;

/**
 * Falcon.
 * @author Sean A. Irvine
 */
public class Falcon extends MaterialMonster implements Virtuous {
  {
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 73);
    setDefault(Attribute.MOVEMENT, 10);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0000007C7C000000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Vulture.class;
  }
}
