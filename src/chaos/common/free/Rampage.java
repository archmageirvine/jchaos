package chaos.common.free;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.util.CastUtils;
import chaos.util.CellEffectType;
import chaos.util.PolycellAttackEvent;
import chaos.util.PolycellEffectEvent;

/**
 * Rampage.
 *
 * @author Sean A. Irvine
 */
public class Rampage extends FreeCastable {

  private static final int RAMPAGE_ATTACK = 6;

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      final Team t = world.getTeamInformation();
      final int p = t.getTeam(caster);
      final Set<Cell> affected = new HashSet<>();
      for (final Cell c : world.getCells(casterCell.getCellNumber(), 1, 1, false)) {
        final Actor a = c.peek();
        if (a != null && t.getTeam(a) != p && a.getState() != State.DEAD) {
          affected.add(c);
        }
      }
      world.notify(new PolycellAttackEvent(affected, caster, RAMPAGE_ATTACK));
      for (final Cell c : affected) {
        final Actor a = c.peek();
        // This following check is needed because if a wizard is killed in an
        // earlier cell, it is possible that some of his creations have also
        // been removed; hence a previous target can become null.  Like if a
        // GoblinBomb has exploded.
        if (a != null) {
          if (a.decrement(Attribute.LIFE, RAMPAGE_ATTACK)) {
            // Death!!
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            c.reinstate();
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
