package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.BowShooter;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * The centaur.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Centaur extends MaterialMonsterMount implements BowShooter, Promotion {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGE, 4);
    setDefault(Attribute.RANGED_COMBAT, 3);
  }

  @Override
  public long getLosMask() {
    return 0x00E0F0FFFF773600L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Horse.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return NamedCentaur.class;
  }

  @Override
  public int promotionCount() {
    return 1;
  }
}
