package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.Virtuous;

/**
 * Gryphon.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Gryphon extends MaterialMonsterMount implements Virtuous, Promotion {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.MAGICAL_RESISTANCE, 86);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x187B7F7F7F1D1800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Manticore.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return Opinicus.class;
  }

  @Override
  public int promotionCount() {
    return 3;
  }
}
