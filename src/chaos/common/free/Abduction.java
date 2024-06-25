package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Abduction.
 * @author Sean A. Irvine
 */
public class Abduction extends FreeCastable {

  /** Maximum number of attempts to find a suitable target. */
  private static final int ATTEMPTS_TO_FIND_TARGET = 1000;
  /** Maximum number of attempts to actually subvert. */
  private static final int ATTEMPTS_TO_SUBVERT = 3;
  /**
   * The larger this number the more likely the abduction on a given creature
   * will succeed.  It should be larger than MAX_VALUE to ensure that even the
   * most intelligent creatures have some chance of succumbing.
   */
  private static final int SUBVERSION_FACTOR = Attribute.INTELLIGENCE.max() + 1;

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      for (int i = 0, j = 0; i < ATTEMPTS_TO_FIND_TARGET; ++i) {
        // Pick a random cell, and look at its contents.  If it is suitable then
        // apply a randomness test to see if the creature succumbs to the spell,
        // if so then make it change sides.  Things that need to be excluded are
        // wizards, cells containing mounted wizards, dead things, and actors
        // that are not monsters (i.e. they have no intelligence attribute).
        final Cell cell = world.getCell(Random.nextInt(world.size()));
        final Actor a = cell.peek();
        if (a instanceof Monster && a.getState() != State.DEAD && !(a instanceof Wizard) && cell.getMount() == null) {
          // Found a valid target, so attempt the actual abduction
          if (Random.nextInt(SUBVERSION_FACTOR) > a.get(Attribute.INTELLIGENCE)) {
            // Abduction succeeds
            cell.notify(new AudioEvent(cell, a, "abduction"));
            cell.notify(new CellEffectEvent(cell, CellEffectType.OWNER_CHANGE));
            a.setOwner(caster.getOwner());
            cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
            return;
          } else if (++j == ATTEMPTS_TO_SUBVERT) {
            // Abduction fails.  Although behind the scenes we try ATTEMPTS_TO_SUBVERT
            // times to perform an abduction, we only show a failing graphic effect
            // for the last attempt.  This is to reduce the cognitive load for the
            // player and to simply the description of the spell.
            cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
            cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
            return;
          }
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
