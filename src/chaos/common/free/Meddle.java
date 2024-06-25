package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Meddle.
 * @author Sean A. Irvine
 */
public class Meddle extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a instanceof Monster && ((Monster) a).reincarnation() != null && a.getState() != State.DEAD) {
          affected.add(world.getCell(i));
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REINCARNATE));
      for (final Cell c : affected) {
        final Actor m = c.peek();
        if (m instanceof Monster) {
          m.set(PowerUps.REINCARNATE, ((Monster) m).reincarnation() != null ? 1 : 0);
          c.reinstate();
          final Actor x = c.peek();
          if (x instanceof Monster) {
            x.set(PowerUps.REINCARNATE, 0);
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
