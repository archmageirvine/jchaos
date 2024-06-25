package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.AbstractFreeDecrement;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.State;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldDestroyEvent;

/**
 * Drain.
 * @author Sean A. Irvine
 */
public class Drain extends AbstractFreeDecrement {

  @Override
  public int decrement() {
    return 1;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MOVEMENT;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // These spells are only applied to enemy creatures, but it does take
      // account of team responsibilities.
      final Team t = world.getTeamInformation();
      final int p = t.getTeam(caster);
      // Compute the set of affected cells first, so that we can start a global
      // effect to represent this spell
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a instanceof Monster && t.getTeam(a) != p && a.getState() != State.DEAD) {
          affected.add(world.getCell(i));
        }
      }
      world.notify(new PolyshieldDestroyEvent(affected, attribute(), caster));
      final int dec = decrement();
      for (final Cell c : affected) {
        final Actor a = c.peek();
        if (a instanceof Monster) {
          final Monster m = (Monster) a;
          final int v = m.get(Attribute.MOVEMENT);
          if (v > 1) {
            m.set(Attribute.MOVEMENT, Math.max(1, v - dec));
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
