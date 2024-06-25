package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * The baboon.
 * @author Sean A. Irvine
 */
public class Baboon extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.COMBAT, 5);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x003C3C3C3C3C7E66L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Halfling.class;
  }
}
