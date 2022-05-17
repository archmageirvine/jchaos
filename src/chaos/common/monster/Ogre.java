package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Ogre.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Ogre extends MaterialMonster implements Promotion, Humanoid {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 6);
  }
  @Override
  public long getLosMask() {
    return 0x001E3E7C5C3C3E00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Goblin.class;
  }
  @Override
  public Class<? extends Monster> promotion() {
    return OgreWarrior.class;
  }
  @Override
  public int promotionCount() {
    return 5;
  }
}
