package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.CastableList;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Joker.
 *
 * @author Sean A. Irvine
 */
public class Joker extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster instanceof Wizard) {
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.TWIRL));
        casterCell.notify(new AudioEvent(casterCell, caster, "joker"));
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
      final CastableList cl = ((Wizard) caster).getCastableList();
      if (cl != null) {
        cl.joker();
      }
    }
  }
}
