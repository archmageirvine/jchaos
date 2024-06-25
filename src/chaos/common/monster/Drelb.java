package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.UndeadMonster;

/**
 * Drelb.
 * @author Sean A. Irvine
 */
public class Drelb extends UndeadMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 50);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.INTELLIGENCE, 80);
    setDefault(Attribute.AGILITY, 75);
    setDefault(Attribute.MOVEMENT, 2);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghost.class;
  }
}
