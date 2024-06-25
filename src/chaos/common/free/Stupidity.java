package chaos.common.free;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.common.AbstractFreeDecrement;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Growth;
import chaos.common.Inanimate;

/**
 * Stupidity.
 * @author Sean A. Irvine
 */
public class Stupidity extends AbstractFreeDecrement {
  @Override
  public int decrement() {
    return 2;
  }

  @Override
  public Attribute attribute() {
    return Attribute.INTELLIGENCE;
  }

  @Override
  protected Set<Cell> filter(final Set<Cell> affected) {
    // Eliminate inanimates and growths
    for (final Iterator<Cell> it = affected.iterator(); it.hasNext(); ) {
      final Actor a = it.next().peek();
      if (a instanceof Inanimate || a instanceof Growth) {
        it.remove();
      }
    }
    return affected;
  }
}
