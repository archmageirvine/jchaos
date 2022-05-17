package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Wraith.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Wraith extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 22);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 93);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 2);
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Wight.class;
  }
}
