package chaos.common;


/**
 * Convenience class to implement a standard dragon.
 * @author Sean A. Irvine
 */
public abstract class Dragon extends Monster implements Rideable {

  {
    setRealm(Realm.DRACONIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  /** The mount if any. */
  private Actor mMount;

  @Override
  public Actor getMount() {
    return mMount;
  }

  @Override
  public void setMount(final Actor actor) {
    Conveyance.checkMount(this, actor);
    mMount = actor;
  }
}
