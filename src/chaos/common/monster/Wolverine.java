package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Wolverine.
 *
 * @author Sean A. Irvine
 */
public class Wolverine extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 14);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.COMBAT, 9);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x0000E27E7E7E0000L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return DireWolf.class;
  }
}
