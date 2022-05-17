package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Separation.
 *
 * @author Sean A. Irvine
 * @author Jonathan Levell
 */
public class Separation extends FreeCastable {

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster != null && world != null) {
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.TEAM_CHANGE));
      }
      world.getTeamInformation().separate(caster.getOwner());
    }
  }
}
