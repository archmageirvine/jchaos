package chaos.common;

/**
 * Convenience class to implement a standard mythos creature.
 * @author Sean A. Irvine
 */
public abstract class MythosMonster extends Monster {

  {
    setRealm(Realm.MYTHOS);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
