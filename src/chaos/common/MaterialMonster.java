package chaos.common;


/**
 * Convenience class to implement a standard material creature.
 * @author Sean A. Irvine
 */
public abstract class MaterialMonster extends Monster {

  {
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
