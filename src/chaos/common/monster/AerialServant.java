package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * The aerial servant.
 * @author Sean A. Irvine
 */
public class AerialServant extends MaterialMonster implements Bonus {
  {
    setDefault(Attribute.LIFE, 58);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.INTELLIGENCE, 66);
    setDefault(Attribute.AGILITY, 66);
    setDefault(Attribute.MOVEMENT, 15);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getCastRange() {
    return 5;
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x003C7EFFFF7E3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return FloatingEye.class;
  }

}
