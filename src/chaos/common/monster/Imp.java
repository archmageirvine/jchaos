package chaos.common.monster;

import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Imp.
 * @author Sean A. Irvine
 */
public class Imp extends MaterialMonster implements AttacksUndead {
  {
    setDefault(Attribute.LIFE, 7);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.INTELLIGENCE, 53);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0000103C382E2E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
