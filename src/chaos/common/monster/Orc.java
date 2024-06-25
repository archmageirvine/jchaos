package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.Promotion;
import chaos.util.Random;

/**
 * Orc.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Orc extends MaterialMonster implements Promotion, Humanoid, Multiplicity {
  {
    setDefault(Attribute.LIFE, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 33);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00387E7E3E183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Spider.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return MightyOrc.class;
  }

  @Override
  public int promotionCount() {
    return 3;
  }

  @Override
  public int getMultiplicity() {
    return 1 + Random.nextInt(8);
  }
}
