package chaos.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.Tree;
import chaos.common.wizard.Wizard;
import irvine.util.array.Sort;

/**
 * Provides for intelligent casting of meditations. This takes into account multiplicity,
 * friendly wizards, Arborist spell, and so on, to do the best job possible of casting
 * these spells.
 * @author Sean A. Irvine
 */
final class IntelligentMeditationCaster {

  private IntelligentMeditationCaster() { }

  private static long[] getTargetRankings(final World world, final List<Integer> wizardCellNumber, final List<Integer> wizardWeighting, final Cell[] targets) {
    // Rank the potential targets, actually numerical value of the ranking is not particularly relevant, its just an ordering
    final long w = world.width();
    final long h = world.height();
    final long maxDistance = (w + 1) * w + (h + 1) * h;
    final long[] targetRanking = new long[targets.length];
    for (int j = 0; j < targets.length; ++j) {
      final int targetCell = targets[j].getCellNumber();
      long sum = 0;
      for (int k = 0; k < wizardCellNumber.size(); ++k) {
        final int distance = world.getSquaredDistance(targetCell, wizardCellNumber.get(k));
        // Closer is "better" hence world.size() - distance achieve this
        final long delta = maxDistance - distance;
        final long score = delta * delta * delta * wizardWeighting.get(k);
        sum += score;
      }
      targetRanking[j] = -sum;
    }
    return targetRanking;
  }

  private static void treeAdjacencyAdjustment(final World world, final Caster caster, final Castable castable, final Cell[] targets, final int multiplicity) {
    if (!caster.is(PowerUps.ARBORIST) && castable instanceof Tree) {
      // Simulated casting to handle no two adjacent trees rule.  Note that previous target
      // selection will have already have accounted for trees outside the existing set.
      for (int k = 0, j = -1; k < multiplicity; ++k) {
        // Find next available desirable place
        while (++j < targets.length && targets[j] == null) {
          // do nothing
        }
        if (j == targets.length) {
          break; // Ran out of places
        }
        // Eliminate places further down the list that violate the adjacency requirements
        for (int i = j + 1; i < targets.length; ++i) {
          final int tj = targets[j].getCellNumber();
          if (targets[i] != null && world.getSquaredDistance(tj, targets[i].getCellNumber()) < 3) {
            targets[i] = null;
          }
        }
      }
    }
  }

  private static Cell[] getRetainedTargets(final Cell[] targets, final int multiplicity) {
    final ArrayList<Cell> retained = new ArrayList<>();
    for (int k = 0, j = 0; k < multiplicity; ++k) {
      while (j < targets.length && targets[j] == null) {
        ++j;
      }
      if (j == targets.length) {
        break;
      }
      retained.add(targets[j++]);
    }
    return retained.toArray(new Cell[0]);
  }

  private static Cell[] sortByDistance(final World world, final int casterCell, final Cell[] retainedTargets) {
    final long[] retainedDistance = new long[retainedTargets.length];
    for (int k = 0; k < retainedTargets.length; ++k) {
      // Want to sort most distant to start of the list
      retainedDistance[k] = -world.getSquaredDistance(casterCell, retainedTargets[k].getCellNumber());
    }
    Sort.sort(retainedDistance, retainedTargets);
    return retainedTargets;
  }

  static boolean cast(final World world, final CastMaster castMaster, final Caster caster, final Cell casterCell, final Castable castable, final int replications) {
    // Compute the set of valid potential targets for this casting
    final Set<Cell> legalTargets = AiEngine.getLegalTargets(world, castMaster, caster, casterCell.getCellNumber(), castable);
    if (legalTargets.isEmpty()) {
      return false;
    }
    // Compute a cell location and weighting for each wizard
    final int casterOwner = caster.getOwner();
    final ArrayList<Integer> wizardCellNumber = new ArrayList<>();
    final ArrayList<Integer> wizardWeighting = new ArrayList<>();
    final Team team = world.getTeamInformation();
    final int casterTeam = team.getTeam(casterOwner);
    for (final Wizard wiz : world.getWizardManager().getWizards()) {
      if (wiz != null && wiz.getState() == State.ACTIVE) {
        final Cell wizCell = world.getCell(wiz);
        if (wizCell != null) {
          final int owner = wiz.getOwner();
          final int wizardScore;
          final boolean sameTeam = team.getTeam(owner) == casterTeam;
          if (sameTeam) {
            final boolean isExposedWizard = wiz.equals(wizCell.peek());
            // +60 for being the caster, +1 for being exposed
            wizardScore = 1 + (owner == casterOwner ? 60 : 0) + (isExposedWizard ? 1 : 0);
          } else {
            wizardScore = -1; // Discourage casting by enemy wizards
          }
          wizardCellNumber.add(wizCell.getCellNumber());
          wizardWeighting.add(wizardScore);
        }
      }
    }
    // Compute a score for each of the potential targets and sort accordingly
    final Cell[] targets = legalTargets.toArray(new Cell[0]);
    final long[] targetRanking = getTargetRankings(world, wizardCellNumber, wizardWeighting, targets);
    Sort.sort(targetRanking, targets);
    // Work out how many we are supposed to be casting
    final int multiplicity = replications * (castable instanceof Multiplicity ? ((Multiplicity) castable).getMultiplicity() : 1);
    if (multiplicity == 1) {
      // Easy case, just pick the single best target and we are done
      castable.cast(world, caster, targets[0], casterCell);
    } else {
      // If we are dealing with a tree (i.e. MagicWood) extra care is needed to handle no two adjacent trees
      treeAdjacencyAdjustment(world, caster, castable, targets, multiplicity);
      // Make a list of those we are keeping sorted by distance
      final Cell[] orderedTargets = sortByDistance(world, casterCell.getCellNumber(), getRetainedTargets(targets, multiplicity));
      for (final Cell cell : orderedTargets) {
        // Each one we insert needs to be a distinct instance
        final Castable cst = FrequencyTable.instantiate(castable.getClass());
        cst.cast(world, caster, cell, casterCell);
      }
    }
    return true;
  }


}
