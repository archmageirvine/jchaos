package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.inanimate.Rock;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Meteor storm.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class MeteorStorm extends FreeCastable {

  /** Number of meteorites to drop. */
  private static final int STONES = 10;
  /** Damage inflicted by each stone. */
  private static final int DAMAGE = 10;

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      for (int i = 0; i < STONES; ++i) {
        // Pick a random cell, and look at its contents.  Things that need to be
        // excluded are wizards.
        final Cell cell = world.getCell(Random.nextInt(world.size()));
        final Actor a = cell.peek();
        if (a instanceof Wizard) {
          continue;
        }
        cell.notify(new CellEffectEvent(cell,
            CellEffectType.WHITE_CIRCLE_EXPLODE));
        if (a == null || a.getState() == State.DEAD || DAMAGE >= a.get(Attribute.LIFE)) {
          // Current cell is replaceable or killed as a result of impact.
          if (cell.reinstate()) {
            final Rock r = new Rock();
            if (caster != null) {
              r.setOwner(caster.getOwner());
            }
            cell.push(r);
          }
        } else {
          a.decrement(Attribute.LIFE, DAMAGE);
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }
}
