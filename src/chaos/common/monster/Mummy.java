package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;
import chaos.common.Virtuous;

/**
 * Mummy.
 * @author Sean A. Irvine
 */
public class Mummy extends UndeadMonster implements NoDeadImage, Virtuous {
  {
    setDefault(Attribute.LIFE, 14);
    setDefault(Attribute.INTELLIGENCE, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 5);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x1C3C7E7E3C38383CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Skeleton.class;
  }
}
