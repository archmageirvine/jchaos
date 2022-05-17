package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Ogre mage.
 *
 * @author Sean A. Irvine
 */
public class OgreMage extends MaterialMonster implements Promotion, Humanoid {
  {
    setDefault(Attribute.LIFE, 13);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 1);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.SPECIAL_COMBAT, -1);
    setDefault(Attribute.RANGE, 11);
    setDefault(Attribute.RANGED_COMBAT, 7);
  }
  @Override
  public long getLosMask() {
    return 0x001E3E7C5C3C3E00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Ogre.class;
  }
  @Override
  public Class<? extends Monster> promotion() {
    return OgreAssassin.class;
  }
  @Override
  public int promotionCount() {
    return 7;
  }
}
