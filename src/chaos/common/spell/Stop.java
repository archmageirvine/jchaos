package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractDecrement;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.Multiplicity;

/**
 * Stop.
 *
 * @author Sean A. Irvine
 */
public class Stop extends AbstractDecrement implements Multiplicity {
  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public int getMultiplicity() {
    return 3;
  }
  @Override
  public int decrement() {
    return Attribute.MOVEMENT.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.MOVEMENT;
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    super.filter(targets, caster, world);
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Cell c = it.next();
      final Actor a = c.peek();
      if (a instanceof Monster && a.get(Attribute.MOVEMENT) == 0) {
        it.remove();
      }
    }
  }
}
