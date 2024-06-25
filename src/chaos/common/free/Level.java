package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PowerUpEvent;

/**
 * Level.
 * @author Sean A. Irvine
 */
public class Level extends FreeCastable {
  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster instanceof Wizard) {
      final Wizard w = (Wizard) caster;
      w.increment(PowerUps.LEVEL);
      if (casterCell != null) {
        casterCell.notify(new PowerUpEvent(casterCell, caster, PowerUps.LEVEL));
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
    }
  }
}
