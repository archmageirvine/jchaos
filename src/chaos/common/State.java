package chaos.common;

/**
 * Possible states for an actor.
 * @author Sean A. Irvine
 */
public enum State {
  /** State for normal activity. */
  ACTIVE,
  /** State for dead instance (not applicable to all Actors). */
  DEAD,
  /** State for sleeping instance (not applicable to all Actors). */
  ASLEEP
}
