package chaos.common;

/**
 * Convenience class to implement a standard demonic monster.
 *
 * @author Sean A. Irvine
 */
public abstract class DemonicMonster extends Monster {
  {
    setRealm(Realm.DEMONIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
