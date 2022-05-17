package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Brown bear.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class BrownBear extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 25);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.AGILITY_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 8);
  }
  @Override
  public long getLosMask() {
    return 0x00383C3C3C1C1C00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Goblin.class;
  }
}
