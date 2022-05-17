package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Bandit.
 *
 * @author Sean A. Irvine
 */
public class Bandit extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 22);
    setDefault(Attribute.COMBAT, 5);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x00187B7F7C181C00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Orc.class;
  }
}
