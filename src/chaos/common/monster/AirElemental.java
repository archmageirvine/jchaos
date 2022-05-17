package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Elemental;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Virtuous;
import chaos.common.inanimate.Tempest;

/**
 * Air elemental.
 *
 * @author Sean A. Irvine
 */
public class AirElemental extends MaterialMonster implements Elemental, Bonus, Virtuous {
  {
    setDefault(Attribute.LIFE, 46);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 85);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0x7C7E7E7F7F7F7F7CL;
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
    final Tempest t = new Tempest();
    t.setOwner(getOwner());
    t.setState(getState());
    return t;
  }
}
