package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.util.CastUtils;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Mutate.
 *
 * @author Sean A. Irvine
 */
public class Mutate extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (int k = 0; k < world.size(); ++k) {
        final Cell c = world.getCell(k);
        if (CastUtils.isMutatable(c)) {
          affected.add(c);
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.TWIRL));
      for (final Cell c : affected) {
        CastUtils.mutate(c);
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
