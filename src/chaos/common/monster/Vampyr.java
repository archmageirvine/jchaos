package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Unicaster;
import chaos.common.spell.RaiseDead;

/**
 * Vampyr.
 * @author Sean A. Irvine
 */
public class Vampyr extends Unicaster implements Bonus {
  {
    mDelay = 1;
    mCastClass = RaiseDead.class;
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.MAGICAL_RESISTANCE, 92);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 5);
    setDefault(Attribute.COMBAT, 11);
    setDefault(Attribute.RANGE, 3);
    setDefault(Attribute.RANGED_COMBAT, 15);
    setDefault(Attribute.INTELLIGENCE, 95);
    setDefault(Attribute.AGILITY, 95);
    setDefault(Attribute.MOVEMENT, 15);
    set(PowerUps.FLYING, 1);
    setRealm(Realm.SUBHYADIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.AGILITY);
    setSpecialCombatApply(Attribute.INTELLIGENCE_RECOVERY);
    set(PowerUps.WAND, 6);
  }

  @Override
  public long getLosMask() {
    return 0x00001C3C3E3E1800L;
  }

  @Override
  public int getBonus() {
    return 4;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Vampire.class;
  }
}
