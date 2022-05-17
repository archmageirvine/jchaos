package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.TargetFilter;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Alliance.
 *
 * @author Sean A. Irvine
 * @author Jonathan Levell
 */
public class Alliance extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH | CAST_DEAD | CAST_SINGLE | CAST_NOINDEPENDENTS;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    // Find the owner of the target cell, and set the team of the caster to be
    // equal to the team of the target cell.  In theory, it is impossible to
    // cast this spell on the independents, but this implementation contains an
    // extra check to make sure this condition is not violated.  Likewise it
    // should never be cast on an empty cell, but again the code is robust
    // enough to silently do nothing if such a request is made.
    if (world != null && caster != null && cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        final int owner = a.getOwner();
        if (owner != Actor.OWNER_INDEPENDENT) {
          cell.notify(new CellEffectEvent(cell, CellEffectType.TEAM_CHANGE));
          // It is the other player that moves to the caster's team!
          world.getTeamInformation().setTeam(caster.getOwner(), owner);
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepMostFrequentOwner(targets, world, world.getTeamInformation().getTeam(caster));
  }
}
