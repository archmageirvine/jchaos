package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Dalek.
 *
 * @author Sean A. Irvine
 */
public class Dalek extends MaterialMonster {
  {
    setDefault(Attribute.LIFE, 38);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 73);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.RANGED_COMBAT, 12);
  }
  @Override
  public long getLosMask() {
    return 0x183CBDFF7E3C7E7EL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Robot.class;
  }

}
