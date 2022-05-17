package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Crocodile.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Crocodile extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 38);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 69);
    setDefault(Attribute.COMBAT, 7);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x000000077F7EFF00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GrizzlyBear.class;
  }
}
