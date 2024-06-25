package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.monster.GiantBeetle;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Mass resurrect.
 * @author Sean A. Irvine
 */
public class MassResurrect extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // Compute the set of cells to be affected, which consists of every cell
      // containing a visible corpse.
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        final Actor a = c.peek();
        // Technically the last condition below is unnecessary, but it helps
        // make the game safer if someone ever creates a dead wizard
        if (a != null && a.getState() == State.DEAD && !(a instanceof Wizard)) {
          affected.add(c);
        }
      }
      if (!affected.isEmpty()) {
        // At least one tree was found.
        world.notify(new PolycellEffectEvent(affected, CellEffectType.GREEN_CIRCLE_EXPLODE));
        final int p = caster.getOwner();
        for (final Cell c : affected) {
          c.pop();
          final GiantBeetle b = new GiantBeetle();
          b.setOwner(p);
          c.push(b);
        }
        world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
      } else if (casterCell != null) {
        // Try to indicate spell failure in the case of no affected.
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
