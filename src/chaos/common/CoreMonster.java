package chaos.common;

/**
 * Convenience class to implement a standard core creature.
 * @author Sean A. Irvine
 */
public abstract class CoreMonster extends Monster {

  {
    setRealm(Realm.CORE);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
