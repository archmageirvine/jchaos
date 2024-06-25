package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Spectre.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Spectre extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 18);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 80);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghast.class;
  }
}
