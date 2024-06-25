package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Death bringer.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class DeathBringer extends FreeCastable {

  /** Maximum number of attempts to find a suitable target. */
  private static final int ATTEMPTS_TO_FIND_TARGET = 1000;

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      for (int i = 0; i < ATTEMPTS_TO_FIND_TARGET; ++i) {
        // Pick a random cell, and look at its contents.  Things that need to be
        // excluded are wizards, dead things, and empty cells.
        final Cell cell = world.getCell(Random.nextInt(world.size()));
        final Actor a = cell.peek();
        if (a != null && a.getState() != State.DEAD && !(a instanceof Wizard)) {
          // Found a valid target
          cell.notify(new CellEffectEvent(cell, CellEffectType.DEATH, caster));
          CastUtils.handleScoreAndBonus(caster, a, casterCell);
          cell.reinstate();
          cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
          return;
        }
      }
      // Failed to find target.  In this case, there is no target cell with which
      // we can identify the attempt; therefore we resort to a SPELL_FAILED event
      // with the notification going to the caster's cell.
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
