package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Free all.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class FreeAll extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        final Actor a = c.peek();
        if ((a instanceof Monster || a instanceof Growth) && !(a instanceof Wizard) && !(a instanceof Inanimate) && a.getState() != State.DEAD && a.getOwner() != Actor.OWNER_INDEPENDENT && c.getMount() == null) {
          affected.add(c);
          a.setOwner(Actor.OWNER_INDEPENDENT);
        }
      }
      if (!affected.isEmpty()) {
        // At least one creature was charmed.
        world.notify(new PolycellEffectEvent(affected, CellEffectType.OWNER_CHANGE));
        world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
      } else if (casterCell != null) {
        // Try to indicate spell failure in the case of no affected.
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
