package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.State;
import chaos.util.CastUtils;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Disrupt.
 * @author Sean A. Irvine
 */
public class Disrupt extends FreeCastable {

  /** Damage caused by this spell. */
  private static final int DAMAGE = 14;

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // This is a harmful spell, so it is only applied to actors not on the same team
      // as the caster.
      final Team t = world.getTeamInformation();
      final int p = t.getTeam(caster);
      // Compute the set of affected cells first, so that we can start a global
      // effect to represent this spell
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a instanceof Monster && !(a instanceof Growth) && !(a instanceof Inanimate) && t.getTeam(a) != p && a.getState() != State.DEAD) {
          affected.add(world.getCell(i));
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.FADE_TO_RED));
      for (final Cell c : affected) {
        final Monster m = (Monster) c.peek();
        if (m != null) {
          final int intel = m.get(Attribute.INTELLIGENCE) - DAMAGE;
          if (intel >= 0) {
            m.set(Attribute.INTELLIGENCE, intel);
          } else {
            // Brain death
            CastUtils.handleScoreAndBonus(caster, m, casterCell);
            //c.notify(new CellEffectEvent(c, CellEffectType.CORPSE_EXPLODE));
            c.reinstate();
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
