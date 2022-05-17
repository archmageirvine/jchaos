package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Thundermare.
 *
 * @author Sean A. Irvine
 */
public class Thundermare extends MaterialMonsterMount implements Bonus, Promotion {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 100);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.COMBAT_RECOVERY, 1);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 4);
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public int getCastRange() {
    return 3;
  }
  @Override
  public long getLosMask() {
    return 0x0000E0FF7E7E2400L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Horse.class;
  }
  @Override
  public Class<? extends Monster> promotion() {
    return Iridium.class;
  }
  @Override
  public int promotionCount() {
    return 8;
  }
}
