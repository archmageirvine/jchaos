package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.Realm;

/**
 * Mind flayer.
 *
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class MindFlayer extends Monster {
  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 93);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.SPECIAL_COMBAT, -2);
    setDefault(Attribute.AGILITY, 87);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.RANGE, 1);
    setDefault(Attribute.RANGED_COMBAT, 12);
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.COMBAT);
    setRangedCombatApply(Attribute.INTELLIGENCE);
    setSpecialCombatApply(Attribute.INTELLIGENCE);
  }
  @Override
  public long getLosMask() {
    return 0x20F7F77B7F7C3E00L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return NeoOtyugh.class;
  }
}
