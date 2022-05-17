package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Goblin bomb.
 * @author Sean A. Irvine
 */
public class GoblinBomb extends MaterialMonster implements Humanoid {
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
    return 0x00183C3C3C1C1C10L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Goblin.class;
  }
}
