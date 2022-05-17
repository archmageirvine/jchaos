package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Pyrohydra.
 *
 * @author Sean A. Irvine
 */
public class Pyrohydra extends MaterialMonster implements Bonus {
  {
    setDefault(Attribute.LIFE, 36);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.COMBAT, 5);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.RANGE, 2);
    setDefault(Attribute.RANGED_COMBAT, 15);
  }
  @Override
  public int getCastRange() {
    return 2;
  }
  @Override
  public int getBonus() {
    return 3;
  }
  @Override
  public long getLosMask() {
    return 0x787CFEFC7E7E7E3EL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Crocodile.class;
  }
}
