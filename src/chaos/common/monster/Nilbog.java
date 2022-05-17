package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Realm;

/**
 * Nilbog.
 * @author Sean A. Irvine
 */
public class Nilbog extends Monster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 6);
    setRealm(Realm.SUBHYADIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x00183E3F7FBC1C10L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Goblin.class;
  }
}
