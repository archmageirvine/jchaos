package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Shadow.
 * @author Sean A. Irvine
 */
public class Shadow extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 13);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.INTELLIGENCE, 4);
    setDefault(Attribute.COMBAT, 5);
    setDefault(Attribute.AGILITY, 87);
    setDefault(Attribute.MOVEMENT, 4);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghoul.class;
  }
}
