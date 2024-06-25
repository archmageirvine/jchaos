package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;

/**
 * Spectator.
 * @author Sean A. Irvine
 */
public class Spectator extends Monster {
  {
    setDefault(Attribute.LIFE, 9);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 15);
    setDefault(Attribute.COMBAT, 2);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.AGILITY);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public int getCastRange() {
    return 3;
  }

  @Override
  public long getLosMask() {
    return 0x3C7EFFFFFFFF7E3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return FloatingEye.class;
  }
}
