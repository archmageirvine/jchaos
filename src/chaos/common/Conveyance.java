package chaos.common;

/**
 * This interface indicates the Actor is mountable or rideable.
 * @author Sean A. Irvine
 */
public interface Conveyance {

  /**
   * Get the mounted actor, or null if nothing is mounted.
   * @return mounted actor
   */
  Actor getMount();

  /**
   * Set the mounted actors (can be null to clear the mount).
   * @param actor Actor to mount
   */
  void setMount(Actor actor);

  /**
   * Perform some basic checking of an attempt to mount.
   * @param conveyance conveyance being mounted
   * @param intendedMount actor trying to mount
   */
  static void checkMount(final Conveyance conveyance, final Actor intendedMount) {
    //assert conveyance != null; // Causes UCF in findbugs ???
    if (intendedMount != null && conveyance.getMount() != null) {
      throw new RuntimeException("Attempt to mount something already mounted");
    } else if (intendedMount == conveyance) {
      throw new RuntimeException("Cannot mount self");
    }
  }
}
