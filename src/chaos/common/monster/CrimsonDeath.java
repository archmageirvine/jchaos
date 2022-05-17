package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.UndeadMonster;

/**
 * Crimson death.
 *
 * @author Sean A. Irvine
 */
public class CrimsonDeath extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 25);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.INTELLIGENCE, 50);
    setDefault(Attribute.AGILITY, 60);
    setDefault(Attribute.MOVEMENT, 3);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Imp.class;
  }
}
