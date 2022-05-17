package chaos.util;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.State;

/**
 * Restore.
 *
 * @author Sean A. Irvine
 */
public final class Restore {

  private Restore() { }

  /**
   * Restore an actor to mint condition.
   *
   * @param a actor to restore
   */
  public static void restore(final Actor a) {
    if (a != null) {
      a.setState(State.ACTIVE);
      for (final Attribute attr : Attribute.values()) {
        a.set(attr, a.getDefault(attr));
      }
      a.setMoved(false);
      a.setEngaged(false);
      if (a instanceof Monster) {
        final Monster m = (Monster) a;
        m.resetShotsMade();
      }
    }
  }
}
