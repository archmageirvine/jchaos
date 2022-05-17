package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Lion.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Lion extends MaterialMonster implements Cat {
  {
    setDefault(Attribute.LIFE, 30);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 38);
    setDefault(Attribute.INTELLIGENCE, 69);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.AGILITY, 50);
    setDefault(Attribute.MOVEMENT, 4);
  }
  @Override
  public long getLosMask() {
    return 0x0060F0FE7E7E3200L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return ShapeChanger.class;
  }
}
