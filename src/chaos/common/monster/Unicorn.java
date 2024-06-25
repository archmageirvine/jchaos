package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * Unicorn.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Unicorn extends MaterialMonsterMount {
  {
    setDefault(Attribute.LIFE, 14);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 4);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 4);
  }

  @Override
  public long getLosMask() {
    return 0x00E0F1FF3E772600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Abath.class;
  }
}
