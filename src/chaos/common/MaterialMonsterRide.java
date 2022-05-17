package chaos.common;


/**
 * Convenience class to implement a standard material creature which is
 * also a rideable creature.
 *
 * @author Sean A. Irvine
 */
public abstract class MaterialMonsterRide extends MaterialMonster implements Rideable {

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
