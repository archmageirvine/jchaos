package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Robot.
 *
 * @author Sean A. Irvine
 */
public class Robot extends MaterialMonster implements Bonus {
  {
    setDefault(Attribute.LIFE, 49);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 2);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public long getLosMask() {
    return 0x2418183C3C3C3C3CL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return KingCobra.class;
  }
}
