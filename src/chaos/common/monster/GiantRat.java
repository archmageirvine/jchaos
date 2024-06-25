package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.Realm;
import chaos.common.Virtuous;

/**
 * Giant rat.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class GiantRat extends Monster implements Virtuous {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 73);
    setDefault(Attribute.MOVEMENT, 3);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE_RECOVERY);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x0000070F7DFF7800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Python.class;
  }
}
