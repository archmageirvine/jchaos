package chaos.common;


/**
 * Convenience class to implement a standard material creature which is
 * also a mountable creature.
 * @author Sean A. Irvine
 */
public abstract class MaterialMonsterMount extends MaterialMonster implements Mountable {

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
