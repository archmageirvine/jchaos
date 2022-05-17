package chaos.common.free;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.common.AbstractFreeDecrement;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Monster;

/**
 * Impair.
 *
 * @author Sean A. Irvine
 */
public class Impair extends AbstractFreeDecrement {
  @Override
  public int decrement() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.SPECIAL_COMBAT;
  }

  @Override
  protected Set<Cell> filter(final Set<Cell> affected) {
    // Eliminate non-monsters
    for (final Iterator<Cell> it = affected.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (!(a instanceof Monster)) {
        it.remove();
      }
    }
    return affected;
  }
}
