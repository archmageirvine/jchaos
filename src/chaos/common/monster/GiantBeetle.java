package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Giant beetle.
 * @author Sean A. Irvine
 */
public class GiantBeetle extends MaterialMonster implements Promotion {
  {
    setDefault(Attribute.LIFE, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.INTELLIGENCE, 69);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.AGILITY_RECOVERY, 5);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00263E3E3C3E1C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return Megabeetle.class;
  }

  @Override
  public int promotionCount() {
    return 1;
  }
}
