package chaos.util;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.FrequencyTable;
import chaos.common.PowerUps;

/**
 * Clone.
 * @author Sean A. Irvine
 */
public final class Clone {

  private Clone() {
  }

  /**
   * Make a clone of a given actor.  All attributes, power ups, and values are copied
   * into a new instance.
   * @param a actor to clone
   * @return clone
   */
  public static Actor clone(final Actor a) {
    if (a == null) {
      return null;
    }
    final Actor clone = (Actor) FrequencyTable.instantiate(a.getClass());
    clone.setOwner(a.getOwner());
    clone.setState(a.getState());
    clone.setRealm(a.getRealm());
    for (final Attribute attr : Attribute.values()) {
      clone.set(attr, a.get(attr));
    }
    for (final PowerUps p : PowerUps.values()) {
      clone.set(p, a.get(p));
    }
    return clone;
  }
}
