package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.UndeadMonster;

/**
 * Vampire.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Vampire extends UndeadMonster implements Bonus, Promotion {
  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.MAGICAL_RESISTANCE, 72);
    setDefault(Attribute.COMBAT, 11);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.AGILITY, 55);
    setDefault(Attribute.MOVEMENT, 4);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0000183C3E3E1800L;
  }

  @Override
  public int getBonus() {
    return 3;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghost.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Vampyr.class;
  }

  @Override
  public int promotionCount() {
    return 5;
  }
}
