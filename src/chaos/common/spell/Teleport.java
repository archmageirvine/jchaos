package chaos.common.spell;

import java.util.ArrayList;
import java.util.List;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Teleport.
 * @author Sean A. Irvine
 */
public class Teleport extends Castable {
  @Override
  public int getCastFlags() {
    return CAST_ANY;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  private List<Actor> popEm(final Cell cell) {
    final ArrayList<Actor> lst = new ArrayList<>();
    Actor a;
    while ((a = cell.pop()) != null) {
      lst.add(a);
    }
    return lst;
  }

  private void pushEm(final Cell cell, final List<Actor> actors) {
    for (int k = actors.size() - 1; k >= 0; --k) {
      cell.push(actors.get(k));
    }
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && casterCell != null && casterCell.peek() != null) {
      final List<Actor> sourceActors = popEm(casterCell);
      final List<Actor> destinationActors = popEm(cell);
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.WARP_OUT, caster));
      cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_IN, caster));
      pushEm(cell, sourceActors);
      pushEm(casterCell, destinationActors);
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }
}
