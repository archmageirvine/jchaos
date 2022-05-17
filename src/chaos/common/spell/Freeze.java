package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Freeze.
 * @author Sean A. Irvine
 */
public class Freeze extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_GROWTH;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        final Wizard w = world.getWizardManager().getWizard(a);
        if (w != null) {
          w.set(PowerUps.FROZEN, 1);
          // place of this effect is questionable
          cell.notify(new AudioEvent(cell, a, "frozen", caster));
          cell.notify(new CellEffectEvent(cell, CellEffectType.POWERUP));
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepEnemies(targets, t, teams);
    final WizardManager wm = world.getWizardManager();
    int best = -1;
    for (final Cell c : targets) {
      final Wizard w = wm.getWizard(c.peek());
      if (w != null) {
        best = Math.max(best, w.getMass());
      }
    }
    if (best != -1) {
      // Found something better than random
      for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
        final Wizard w = wm.getWizard(it.next().peek());
        if (w == null || w.getMass() < best) {
          it.remove();
        }
      }
    }
  }

}
