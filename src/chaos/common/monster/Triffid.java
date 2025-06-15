package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.growth.DancingDaisy;

/**
 * The triffid.
 * @author Sean A. Irvine
 */
public class Triffid extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 46);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.RANGE, 1);
    setDefault(Attribute.RANGED_COMBAT, 15);
    setDefault(Attribute.RANGED_COMBAT_RECOVERY, 1);
    setDefault(Attribute.AGILITY, 60);
  }

  @Override
  public long getLosMask() {
    return 0x00183C3E7E7E3C3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return DancingDaisy.class;
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
