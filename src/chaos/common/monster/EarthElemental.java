package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Elemental;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.growth.Earthquake;

/**
 * Earth elemental.
 *
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class EarthElemental extends MaterialMonster implements Elemental, Bonus {
  {
    setDefault(Attribute.LIFE, 55);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 85);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 30);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x003CFFFFFFFF7E7EL;
  }
  @Override
  public int getBonus() {
    return 4;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
  @Override
  public Actor getElementalReplacement() {
    final Earthquake t = new Earthquake();
    t.setOwner(getOwner());
    t.setState(getState());
    return t;
  }
}
