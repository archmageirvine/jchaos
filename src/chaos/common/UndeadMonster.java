package chaos.common;

/**
 * Convenience class to implement a standard undead creature.
 *
 * @author Sean A. Irvine
 */
public abstract class UndeadMonster extends Monster {

  {
    setRealm(Realm.ETHERIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    // by default undead do not block LOS
    return 0L;
  }
}
