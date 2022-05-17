package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Virtuous;

/**
 * Pegasus.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Pegasus extends MaterialMonsterMount implements Virtuous {
  {
    setDefault(Attribute.LIFE, 28);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.INTELLIGENCE, 53);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public long getLosMask() {
    return 0x0E1EFEFF7EFE2400L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Manticore.class;
  }
}
