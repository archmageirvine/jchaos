package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Goblin.
 * @author Sean A. Irvine
 */
public class Goblin extends MaterialMonster implements Humanoid, Promotion {
  {
    setDefault(Attribute.LIFE, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 6);
  }
  @Override
  public long getLosMask() {
    return 0x00183E3F7FBC1C10L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Orc.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Nilbog.class;
  }

  @Override
  public int promotionCount() {
    return 1;
  }
}
