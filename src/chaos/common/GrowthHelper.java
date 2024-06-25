package chaos.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.wizard.Wizard;

/**
 * Helper functions for growths.
 * @author Sean A. Irvine
 */
public final class GrowthHelper {

  private GrowthHelper() {
  }

  /**
   * Given a set of possible places to put a growth, select the best.
   * @param targets the possible targets
   * @param caster the caster
   * @param world the world
   */
  public static void filter(final Set<Cell> targets, final Caster caster, final World world) {
    // Keep targets closest to enemy wizards
    final Team team = world.getTeamInformation();
    final ArrayList<Cell> enemyWizards = new ArrayList<>();
    for (final Wizard wiz : world.getWizardManager().getWizards()) {
      if (wiz != null && wiz.getState() == State.ACTIVE && team.getTeam(wiz) != team.getTeam(caster)) {
        final Cell cell = world.getCell(wiz);
        if (cell != null) {
          enemyWizards.add(cell);
        }
      }
    }
    //System.err.println("Found: " + enemyWizards.size() + " enemy wizards.");
    final HashSet<Cell> best = new HashSet<>();
    int bestDistance = Integer.MAX_VALUE;
    for (final Cell t : targets) {
      for (final Cell w : enemyWizards) {
        final int d = world.getSquaredDistance(t.getCellNumber(), w.getCellNumber());
        if (d < bestDistance) {
          bestDistance = d;
          best.clear();
        }
        if (d == bestDistance) {
          best.add(t);
        }
      }
    }
    if (!best.isEmpty()) {
      targets.clear();
      targets.addAll(best);
    }
  }

}
