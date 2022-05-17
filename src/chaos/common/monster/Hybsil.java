package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.BowShooter;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * Hybsil.
 * @author Sean A. Irvine
 */
public class Hybsil extends MaterialMonsterMount implements BowShooter {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.RANGED_COMBAT, 6);
    setRangedCombatApply(Attribute.AGILITY);
  }

  @Override
  public long getLosMask() {
    return 0x00070FFFFFEE6C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Centaur.class;
  }
}
