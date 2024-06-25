package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Virtuous;

/**
 * Derro.
 * @author Sean A. Irvine
 */
public class Derro extends MaterialMonster implements Humanoid, Virtuous {
  {
    setDefault(Attribute.LIFE, 28);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 73);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x001E3F7C7C1C1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Troll.class;
  }
}
