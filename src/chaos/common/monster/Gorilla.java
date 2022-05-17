package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Gorilla.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Gorilla extends MaterialMonster implements Humanoid, Promotion {

  {
    setDefault(Attribute.LIFE, 13);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 14);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 7);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x007E7E7E3C3C7E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return WoodElf.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return CarnivorousApe.class;
  }

  @Override
  public int promotionCount() {
    return 2;
  }
}
