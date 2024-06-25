package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.CastableList;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Inanimate;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Paradigm shift.
 * @author Sean A. Irvine
 */
public class ParadigmShift extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        final Actor a = c.peek();
        if (a != null && a.getState() != State.DEAD && !(a instanceof Wizard) && c.getMount() == null && a.getRealm() == Realm.ETHERIC && !(a instanceof Inanimate)) {
          affected.add(c);
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.TWIRL));
      for (final Cell c : affected) {
        // Take it off the board, and place it back on the spell list if possible
        final Actor current = c.pop();
        final int owner = current.getOwner();
        if (owner != Actor.OWNER_INDEPENDENT) {
          final Wizard ow = world.getWizardManager().getWizard(owner);
          if (ow != null) {
            final CastableList cl = ow.getCastableList();
            if (cl != null) {
              cl.add(current);
            }
          }
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
