package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.UndeadMonster;

/**
 * Ghost.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Ghost extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 85);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.INTELLIGENCE, 80);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 2);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Wraith.class;
  }
}
