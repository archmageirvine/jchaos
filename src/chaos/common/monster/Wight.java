package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Wight.
 * @author Sean A. Irvine
 */
public class Wight extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.INTELLIGENCE, 53);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x3EFFFFFFFF7F7F7FL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Skeleton.class;
  }
}
