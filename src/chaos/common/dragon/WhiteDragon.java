package chaos.common.dragon;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Dragon;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Virtuous;
import chaos.common.monster.Pseudodragon;

/**
 * White dragon.
 * @author Sean A. Irvine
 */
public class WhiteDragon extends Dragon implements Bonus, Virtuous {
  {
    setDefault(Attribute.LIFE, 44);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.RANGED_COMBAT, 12);
    setDefault(Attribute.RANGE, 7);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0xF87C7FFFFEFF7F6AL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Pseudodragon.class;
  }
}
