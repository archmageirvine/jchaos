package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Elemental;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.growth.Flood;

/**
 * Water elemental.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class WaterElemental extends MaterialMonster implements Elemental, Bonus {
  {
    setDefault(Attribute.LIFE, 34);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 85);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 1);
    set(PowerUps.FLOOD_SHIELD, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0L;
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
    final Flood t = new Flood();
    t.setOwner(getOwner());
    t.setState(getState());
    return t;
  }
}

