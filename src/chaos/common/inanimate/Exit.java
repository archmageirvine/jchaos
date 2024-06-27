package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.Inanimate;
import chaos.common.Mountable;
import chaos.common.Named;
import chaos.common.Realm;

/**
 * The exit.
 * @author Sean A. Irvine
 */
public class Exit extends Actor implements Inanimate, Mountable, Named {

  /** Exit types that help determine when it will open. */
  public enum ExitType {
    /** Exit is always open. */
    ALWAYS_OPEN,
    /** Exit is open provided there is no enemy with movement. */
    NO_REAL_ENEMY
  }

  private final String mNextScenario;
  private final ExitType mExitType;

  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
  }

  /**
   * Construct an exit with a specified following scenario (can be null).
   * @param nextScenarioResource follow on resource
   * @param exitType the type of the exit
   */
  public Exit(final String nextScenarioResource, final ExitType exitType) {
    mNextScenario = nextScenarioResource;
    mExitType = exitType;
  }

  /** Construct an exit. */
  public Exit() {
    this(null, ExitType.ALWAYS_OPEN);
  }

  @Override
  public int getCastFlags() {
    return 0;
  }

  @Override
  public int getCastRange() {
    return 1; // this just a dummy, can't ever get this to cast
  }

  @Override
  public long getLosMask() {
    return ~0L;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
  }

  @Override
  public int getDefaultWeight() {
    return 5;
  }

  private Actor mMount = null;

  @Override
  public Actor getMount() {
    return mMount;
  }

  @Override
  public void setMount(final Actor actor) {
    Conveyance.checkMount(this, actor);
    mMount = actor;
  }

  /**
   * Get the subsequent scenario or null if there is no follow on scenario.
   * @return resource for next scenario
   */
  public String nextScenario() {
    return mNextScenario;
  }

  private boolean mIsOpen = false;

  /**
   * Set whether this exit is currently open.
   * @param status open status
   */
  public void setOpen(final boolean status) {
    mIsOpen = status;
  }

  /**
   * Test if this exit is currently open.
   * @return true if the exit is open
   */
  public boolean isOpen() {
    return mIsOpen;
  }

  /**
   * The type of this exit.
   * @return the type of this exit
   */
  public ExitType getExitType() {
    return mExitType;
  }

  @Override
  public String getPersonalName() {
    return getName() + (isOpen() ? " (open)" : " (closed)");
  }

  @Override
  public void setPersonalName(final String name) {
  }
}
