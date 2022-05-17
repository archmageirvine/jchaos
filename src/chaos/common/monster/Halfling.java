package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Halfling.
 *
 * @author Sean A. Irvine
 */
public class Halfling extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setDefault(Attribute.INTELLIGENCE, 53);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x0000303E3C383800L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }
}
