package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.Random;

/**
 * Charm.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class Charm extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // Compute the set of cells to be affected, which consists of every cell
      // containing a tree.
      final int p = caster.getOwner();
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        final Actor a = c.peek();
        if (a instanceof Monster && !(a instanceof Wizard) && a.getState() != State.DEAD && c.getMount() == null && Random.nextInt(7) == 0) {
          // changes with probability 1/7
          affected.add(c);
        }
      }
      final Team teams = world.getTeamInformation();
      final int t = teams.getTeam(caster);
      CastUtils.keepEnemies(affected, t, teams);
      if (!affected.isEmpty()) {
        for (final Cell c : affected) {
          c.peek().setOwner(p);
        }
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
