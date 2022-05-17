package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.DemonicMonster;
import chaos.common.Elemental;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.growth.Balefire;

/**
 * Balrog.
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class Balrog extends DemonicMonster implements Elemental, Humanoid, Bonus, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 100);
    setDefault(Attribute.LIFE_RECOVERY, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE, 90);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.RANGED_COMBAT, 15);
    setDefault(Attribute.RANGE, 2);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.AGILITY, 60);
    setDefault(Attribute.MOVEMENT, 5);
    set(PowerUps.FLYING, 1);
    set(PowerUps.ATTACK_ANY_REALM, 1);
  }

  @Override
  public long getLosMask() {
    return 0x3CFEFEFFFF7F1B19L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return FireDemon.class;
  }

  @Override
  public int getBonus() {
    return 4;
  }

  @Override
  public Actor getElementalReplacement() {
    final Balefire t = new Balefire();
    t.setOwner(getOwner());
    t.setState(getState());
    return t;
  }
}
