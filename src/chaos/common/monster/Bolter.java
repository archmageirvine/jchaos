package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * The bolter.
 * @author Sean A. Irvine
 */
public class Bolter extends MaterialMonster implements Bonus {
  {
    setDefault(Attribute.LIFE, 49);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 1);
    setDefault(Attribute.RANGE, 7);
    setDefault(Attribute.RANGED_COMBAT, 12);
    setDefault(Attribute.RANGED_COMBAT_RECOVERY, 1);
    setDefault(Attribute.AGILITY, 60);
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return ~0L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public int getCastFlags() {
    return CAST_LOS | CAST_DEAD | CAST_EMPTY;
  }
}
