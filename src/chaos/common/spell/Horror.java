package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Horror.
 *
 * @author Sean A. Irvine
 */
public class Horror extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 2;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor target = cell.peek();
      if (target instanceof Monster) {
        target.set(PowerUps.HORROR, 4);
      }
      cell.notify(new CellEffectEvent(cell, CellEffectType.POWERUP));
      cell.notify(new AudioEvent(cell, target, "horror", caster));
      cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepFriends(targets, t, teams);
    // Remove existing horrors and wizards
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a instanceof Monster && (a instanceof Wizard || a.is(PowerUps.HORROR))) {
        i.remove();
      }
    }
    CastUtils.keepHighestScoring(CastUtils.dropWizards(CastUtils.preferAnimates(targets)));
  }
}
