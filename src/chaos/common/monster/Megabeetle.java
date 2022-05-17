package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Megabeetle.
 * @author Sean A. Irvine
 */
public class Megabeetle extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 60);
    setDefault(Attribute.MAGICAL_RESISTANCE, 18);
    setDefault(Attribute.INTELLIGENCE, 69);
    setDefault(Attribute.COMBAT, 10);
    setDefault(Attribute.COMBAT_RECOVERY, 4);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.AGILITY_RECOVERY, 5);
    setDefault(Attribute.MOVEMENT, 1);
    set(PowerUps.INVULNERABLE, 7);
  }
  @Override
  public long getLosMask() {
    return 0x007E7E7E3E7E7E3CL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }
}
