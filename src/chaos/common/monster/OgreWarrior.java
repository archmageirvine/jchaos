package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Ogre warrior.
 * @author Sean A. Irvine
 */
public class OgreWarrior extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 29);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 28);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 7);
  }

  @Override
  public long getLosMask() {
    return 0x001E3E7C5C3C3E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ogre.class;
  }
}
