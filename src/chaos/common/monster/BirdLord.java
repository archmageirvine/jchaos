package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterRide;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * The Bird Lord.
 * @author Sean A. Irvine
 */
public class BirdLord extends MaterialMonsterRide {
  {
    setDefault(Attribute.LIFE, 23);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.INTELLIGENCE, 93);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 7);
    setDefault(Attribute.RANGE, 10);
    setDefault(Attribute.RANGED_COMBAT, 2);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0040FE7E7F660000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Eagle.class;
  }
}
