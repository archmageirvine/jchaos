package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.FrequencyTable;
import chaos.common.Growth;
import chaos.common.Singleton;
import chaos.common.State;

/**
 * Summons.
 * @author Sean A. Irvine
 */
public class Summons extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      for (final Cell c : world.getCells(casterCell.getCellNumber(), 1, 1, false)) {
        if (c.peek() == null || c.peek().getState() == State.DEAD) {
          // Select uniform random creature
          Actor a;
          while ((a = FrequencyTable.DEFAULT.getUniformRandomMonster()) == null || a instanceof Growth
            || (a instanceof Singleton && world.isAlive(a.getClass()) != Actor.OWNER_NONE)) {
            // do nothing
          }
          // and whack it in via normal creature cast
          a.cast(world, caster, c, casterCell);
        }
      }
    }
  }
}
