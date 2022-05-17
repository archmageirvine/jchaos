package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterRide;
import chaos.common.Monster;

/**
 * Diplodocus.
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class Diplodocus extends MaterialMonsterRide {
  {
    setDefault(Attribute.LIFE, 60);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 20);
    setDefault(Attribute.INTELLIGENCE, 10);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.AGILITY, 3);
    setDefault(Attribute.MOVEMENT, 2);
  }
  @Override
  public long getLosMask() {
    return 0x00000007BEFC7E3CL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Crocodile.class;
  }
}
