package chaos.common.monster;

import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.Realm;
import chaos.common.Virtuous;

/**
 * Arctic wolf.
 * @author Sean A. Irvine
 */
public class ArcticWolf extends Monster implements AttacksUndead, Virtuous {
  {
    setDefault(Attribute.LIFE, 28);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 47);
    setDefault(Attribute.MOVEMENT, 3);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.MOVEMENT);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x0000477E7E7E0000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return DireWolf.class;
  }
}
