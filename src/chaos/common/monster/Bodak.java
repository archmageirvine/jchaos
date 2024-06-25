package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Bodak.
 * @author Sean A. Irvine
 */
public class Bodak extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 27);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.MAGICAL_RESISTANCE, 13);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 13);
  }

  @Override
  public long getLosMask() {
    return 0x127F7FFFF828286CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Troll.class;
  }
}
