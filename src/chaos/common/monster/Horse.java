package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * Horse.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Horse extends MaterialMonsterMount {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 60);
    setDefault(Attribute.MOVEMENT, 4);
  }
  @Override
  public long getLosMask() {
    return 0x0000E0FF7E7E2400L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Spider.class;
  }
}
