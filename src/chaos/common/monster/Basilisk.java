package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.Realm;

/**
 * The basilisk.
 * @author Sean A. Irvine
 */
public class Basilisk extends Monster {
  {
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.LIFE, 49);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.RANGED_COMBAT, 1);
    setDefault(Attribute.RANGE, 2);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.AGILITY, 7);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.MOVEMENT);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x000000C0FF7F4E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Crocodile.class;
  }
}
