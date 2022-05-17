package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Merge.
 *
 * @author Sean A. Irvine
 */
public class Merge extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_NOEXPOSEDWIZARD | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 9;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (caster != null && cell != null) {
      cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_OUT, caster));
      chaos.util.Merge.merge(caster, cell);
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.WARP_IN, caster));
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepHighestScoring(targets);
    // Eliminate self-mount
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a instanceof Conveyance && ((Conveyance) a).getMount() == caster) {
        it.remove();
      }
    }
  }
}
