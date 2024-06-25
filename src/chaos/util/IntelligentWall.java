package chaos.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.wizard.Wizard;

/**
 * Routines to assist in the intelligent casting of walls and pits.
 * @author Sean A. Irvine
 */
public final class IntelligentWall {

  private IntelligentWall() {
  }

  private enum TargetType {
    WIZARD_AND_BLOCKER, WIZARD, BLOCKER_FOUR, BLOCKER_EIGHT, ENEMY_HI, ENEMY_LOW, DISTANT, NONE
  }

  private static TargetType computeType(final Cell c, final World world, final int t, final int cc) {
    final Team team = world.getTeamInformation();
    final int cn = c.getCellNumber();
    boolean enemyWizard = false;
    boolean blocker4 = false;
    boolean blocker8 = false;
    int enemy = 0;
    for (final Cell v : world.getCells(c.getCellNumber(), 1, 1, false)) {
      final Actor a = v.peek();
      if (a != null) {
        if (team.getTeam(a) != t) {
          ++enemy;
          enemyWizard |= a instanceof Wizard || (a instanceof Conveyance && ((Conveyance) a).getMount() != null);
        } else {
          if (a instanceof Blocker) {
            if (world.getSquaredDistance(cn, v.getCellNumber()) == 1) {
              blocker4 = true;
            } else {
              blocker8 = true;
            }
          }
        }
      }
    }
    if (enemyWizard) {
      return blocker4 || blocker8 ? TargetType.WIZARD_AND_BLOCKER : TargetType.WIZARD;
    } else if (blocker4) {
      return TargetType.BLOCKER_FOUR;
    } else if (blocker8) {
      return TargetType.BLOCKER_EIGHT;
    } else if (enemy > 0) {
      return enemy > 3 ? TargetType.ENEMY_HI : TargetType.ENEMY_LOW;
    } else if (cc == -1 || world.getSquaredDistance(cc, cn) > 15) {
      return TargetType.DISTANT;
    } else {
      return TargetType.NONE;
    }
  }

  /**
   * From a set of putative targets, retain those which are the best candidates
   * for casting a wall or pit type of object.  The order of preference runs as
   * follows: adjacent to an enemy wizard and another blocker, adjacent to an
   * enemy wizard, 4-way adjacent to a blocker, 8-way adjacent to a blocker,
   * adjacent to enemies, not close to caster's wizard, anything else.
   * @param targets targets to filter
   * @param caster the caster
   * @param world the world
   * @return selected targets
   */
  public static Set<Cell> choose(final Set<Cell> targets, final Caster caster, final World world) {
    final int t = world.getTeamInformation().getTeam(caster);

    // Try and find the wizard of the caster
    final Wizard w = world.getWizardManager().getWizard(caster.getOwner());
    final Cell cc = w == null ? null : world.getCell(w);
    final int ccn = cc == null ? -1 : cc.getCellNumber();

    // Compute the type of each potential target cell
    TargetType best = TargetType.NONE;
    final HashMap<Cell, TargetType> targetMap = new HashMap<>();
    for (final Cell c : targets) {
      final TargetType tt = computeType(c, world, t, ccn);
      if (tt.ordinal() < best.ordinal()) {
        best = tt;
      }
      targetMap.put(c, tt);
    }

    // Eliminate all things not of the best type
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext(); ) {
      if (targetMap.get(it.next()) != best) {
        it.remove();
      }
    }

    return targets;
  }

}
