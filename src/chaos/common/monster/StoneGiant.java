package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Virtuous;

/**
 * Stone giant.  Was just called "Giant" in Spectrum Chaos.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class StoneGiant extends MaterialMonster implements Bonus, Humanoid, Virtuous {
  {
    setDefault(Attribute.LIFE, 48);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.INTELLIGENCE, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 13);
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
    return 0x48FEFEFF1F141436L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return ShapeChanger.class;
  }
}
