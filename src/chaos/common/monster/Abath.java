package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;

/**
 * The abath.
 * @author Sean A. Irvine
 */
public class Abath extends MaterialMonsterMount {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 4);
  }

  @Override
  public long getLosMask() {
    return 0x80C0F1FF3F773600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Camel.class;
  }
}
