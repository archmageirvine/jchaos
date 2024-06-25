package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;

/**
 * Points.
 * @author Sean A. Irvine
 */
public class Points extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster instanceof Wizard) {
      ((Wizard) caster).addScore(100);
      if (casterCell != null) {
        casterCell.notify(new AudioEvent(casterCell, caster, "points"));
      }
    }
  }
}
