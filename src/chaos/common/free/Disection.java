package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Disection.
 *
 * @author Sean A. Irvine
 */
public class Disection extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      // This spell applies to every cell except empty and dead cells
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a != null && a.getState() != State.DEAD) {
          affected.add(world.getCell(i));
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.FADE_TO_RED));
      for (final Cell c : affected) {
        final Actor a = c.peek();
        a.set(Attribute.LIFE, (a.get(Attribute.LIFE) + 1) >>> 1);
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
