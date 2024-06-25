package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Manticore.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Manticore extends MaterialMonsterMount {
  {
    setDefault(Attribute.LIFE, 36);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 5);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.RANGED_COMBAT, 1);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x383EFF7F7E7E7E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Centaur.class;
  }
}
