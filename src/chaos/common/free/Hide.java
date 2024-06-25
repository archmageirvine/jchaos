package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;

/**
 * Hide.
 * @author Sean A. Irvine
 */
public class Hide extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      world.getWarpSpace().warpOut(casterCell, caster);
    }
  }
}
