package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Ogre assassin.
 * @author Sean A. Irvine
 */
public class OgreAssassin extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 31);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.MOVEMENT, 3);
    setDefault(Attribute.COMBAT, 12);
  }

  @Override
  public long getLosMask() {
    return 0x001E3E7C5C3C3E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return OgreMage.class;
  }
}
