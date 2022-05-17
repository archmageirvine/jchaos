package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Ghast.
 *
 * @author Sean A. Irvine
 */
public class Ghast extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x0000183C3C1C1C00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Skeleton.class;
  }
}
