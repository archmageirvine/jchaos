package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Grizzly bear.
 * @author Sean A. Irvine
 */
public class GrizzlyBear extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 30);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 10);
  }

  @Override
  public long getLosMask() {
    return 0x00383C3C3C1C1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return OgreMage.class;
  }
}
