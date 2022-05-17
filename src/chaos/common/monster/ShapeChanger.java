package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Shape changer.
 *
 * @author Sean A. Irvine
 */
public class ShapeChanger extends MaterialMonster implements Bonus {
  {
    setDefault(Attribute.LIFE, 41);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 3);
    setDefault(Attribute.COMBAT, 8);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public int getCastRange() {
    return 3;
  }
  @Override
  public long getLosMask() {
    return 0x00787C7E7F7E7E00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return BrownBear.class;
  }
}
