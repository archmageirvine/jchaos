package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.util.PolyshieldEvent;

/**
 * Sharpshooting.
 *
 * @author Sean A. Irvine
 */
public class Sharpshooting extends FreeCastable {

  /** Increase in range given by the sharpshooting. */
  private static final int SHARPSHOOTING_FACTOR = 1;
  /** Increase in ranged combat given by the sharpshooting. */
  private static final int SHARP_FACTOR = 2;

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // Based on AbstractIncrement
      final HashSet<Cell> affected = new HashSet<>();
      if (caster.get(Attribute.RANGE) > 0) {
        caster.increment(Attribute.RANGE, SHARPSHOOTING_FACTOR);
        caster.increment(Attribute.RANGED_COMBAT, SHARP_FACTOR);
        if (casterCell != null) {
          affected.add(casterCell);
        }
      }
      final int p = caster.getOwner();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a instanceof Monster && a != caster && a.getOwner() == p && a.getState() == State.ACTIVE && a.get(Attribute.RANGE) > 0) {
          affected.add(world.getCell(i));
          a.increment(Attribute.RANGE, SHARPSHOOTING_FACTOR);
          a.increment(Attribute.RANGED_COMBAT, SHARP_FACTOR);
        }
      }
      world.notify(new PolyshieldEvent(affected, Attribute.RANGE, caster));
    }
  }
}
