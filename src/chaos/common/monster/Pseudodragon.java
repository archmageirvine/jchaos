package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterRide;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Pseudodragon.
 *
 * @author Sean A. Irvine
 */
public class Pseudodragon extends MaterialMonsterRide {
  {
    setDefault(Attribute.LIFE, 29);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 86);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGED_COMBAT, 2);
    setDefault(Attribute.RANGE, 7);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public long getLosMask() {
    return 0xF07C7EFFFEFF6F6EL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return TRex.class;
  }
}
