package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Dire wolf.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class DireWolf extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 9);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x0000E27E7E7E0000L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantRat.class;
  }
}
