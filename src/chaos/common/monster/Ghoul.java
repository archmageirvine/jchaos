package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Ghoul.
 * @author Sean A. Irvine
 */
public class Ghoul extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 60);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x24BE3E3C3C183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghast.class;
  }
}
