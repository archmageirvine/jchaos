package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Mighty orc.
 * @author Sean A. Irvine
 */
public class MightyOrc extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 33);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00387E7E3E183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Orc.class;
  }
}
