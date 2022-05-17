package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Virtuous;

/**
 * Opinicus.
 * @author Sean A. Irvine
 */
public class Opinicus extends MaterialMonsterMount implements Virtuous {
  {
    setDefault(Attribute.LIFE, 35);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.MAGICAL_RESISTANCE, 86);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.AGILITY, 90);
    setDefault(Attribute.MOVEMENT, 7);
    set(PowerUps.FLYING, 1);
    set(PowerUps.INVULNERABLE, 2);
  }

  @Override
  public long getLosMask() {
    return 0x187B7F7F7F1D1800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Gryphon.class;
  }
}
