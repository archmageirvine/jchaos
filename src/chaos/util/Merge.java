package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;

/**
 * Merge another creature with a given creature.
 *
 * @author Sean A. Irvine
 */
public final class Merge {

  private Merge() { }

  /**
   * Merge the contents of the given cell with the specified monster. In general
   * quantities are additive, but things such as the owner and state of the base
   * creature are not modified.
   *
   * @param base recipient
   * @param mergeCell cell to be merged
   */
  public static void merge(final Monster base, final Cell mergeCell) {
    final Actor a = mergeCell.peek();
    if (a == null || a instanceof Wizard) {
      return;
    }
    for (final Attribute attr : Attribute.values()) {
      base.increment(attr, a.get(attr));
    }
    if (a instanceof Monster) {
      final Monster m = (Monster) a;
      base.set(PowerUps.HORROR, base.get(PowerUps.HORROR) + m.get(PowerUps.HORROR));
      if (m.is(PowerUps.REINCARNATE)) {
        base.set(PowerUps.REINCARNATE, base.reincarnation() != null ? 1 : 0);
      }
      if (m.is(PowerUps.CLOAKED)) {
        base.set(PowerUps.CLOAKED, 1);
      }
      if (m.is(PowerUps.FLYING)) {
        base.set(PowerUps.FLYING, 1);
      }
      if (m.is(PowerUps.ARCHERY)) {
        base.set(PowerUps.ARCHERY, 1);
      }
    }
    mergeCell.reinstate();
  }
}
