package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Elemental;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.dragon.RedDragon;

/**
 * Hidden horror.
 * @author Sean A. Irvine
 */
public class HiddenHorror extends MaterialMonster implements Humanoid, Elemental {
  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 100);
    setDefault(Attribute.INTELLIGENCE, 27);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00387E7E3E183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return MightyOrc.class;
  }

  @Override
  public Actor getElementalReplacement() {
    final RedDragon t = new RedDragon();
    t.setOwner(getOwner());
    t.setState(getState());
    return t;
  }
}
