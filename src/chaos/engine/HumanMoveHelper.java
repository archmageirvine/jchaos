package chaos.engine;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.NoFlyOver;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Gollop;
import chaos.common.wizard.Wizard;
import chaos.util.CombatUtils;
import irvine.math.r.Constants;

/**
 * Provides path finding and cell identification for human based movement.
 * @author Sean A. Irvine
 */
public class HumanMoveHelper implements Serializable {

  private static final int[] DELTA_X = {1, -1, 0, 0, 1, 1, -1, -1};
  private static final int[] DELTA_Y = {0, 0, 1, -1, 1, -1, 1, -1};

  private final World mWorld;
  private final double[] mDistance; // Movement points to reach here, or -1
  private final int[] mPrior;    // Prior cell on path
  private final int[] mCellCoords = new int[2];

  HumanMoveHelper(final World world) {
    mWorld = world;
    mDistance = new double[world.size()];
    mPrior = new int[world.size()];
  }

  private void enqueue(final Queue<Integer> queue, final double distance, final int direction) {
    final int next = mWorld.getCellNumber(mCellCoords[0] + DELTA_X[direction], mCellCoords[1] + DELTA_Y[direction]);
    if (next != -1 && mDistance[next] < 0) {
      mDistance[next] = distance;
      queue.add(next);
    }
  }

  private void searchFlying(final Queue<Integer> queue, final double distance) {
    while (!queue.isEmpty()) {
      // The queue is cells we can already reach.  All such cells will already have their
      // distance set appropriate and prior
      final int cell = queue.remove();
      final double d = mDistance[cell];
      if (d + 1 <= distance && !(mWorld.actor(cell) instanceof NoFlyOver)) {
        // N, S, E, W
        mWorld.getCellCoordinates(cell, mCellCoords);
        for (int k = 0; k < 4; k++) {
          enqueue(queue, d + 1, k);
        }
        final double ds = d + Constants.SQRT2;
        if (ds <= distance) {
          for (int k = 4; k < DELTA_X.length; k++) {
            enqueue(queue, ds, k);
          }
        }
      }
    }
  }

  private void setTargetsFlying(final int source, final double distance, final Wizard wizard, final MoveMaster mm, final boolean isWizardMoving) {
    Arrays.fill(mPrior, source); // Flying movement is always single step
    final Queue<Integer> queue = new LinkedList<>();
    queue.add(source);
    searchFlying(queue, distance);
    final int owner = wizard.getOwner();
    // The above has set everything in range, but need to reject those that we cannot actually moved to
    for (int c = 0; c < mDistance.length; ++c) {
      if (mDistance[c] > 0) {
        final Actor a = mWorld.actor(c);
        if (a != null && a.getState() != State.DEAD && !mm.isAttackable(source, c)
          && (!isWizardMoving || !mm.isMountable(wizard, c))
          && (!(a instanceof Gollop) || owner != a.getOwner())) {
          mDistance[c] = -1;
        }
      }
    }
  }

  private void enqueueWalking(final Queue<Integer> queue, final double distance, final int direction, final MoveMaster mm, final Actor actor, final int previous) {
    final int next = mWorld.getCellNumber(mCellCoords[0] + DELTA_X[direction], mCellCoords[1] + DELTA_Y[direction]);
    if (next != -1 && mDistance[next] < 0) {
      final Actor a = mWorld.actor(next);
      if (a == null || a.getState() == State.DEAD) {
        mDistance[next] = distance;
        mPrior[next] = previous;
        queue.add(next);
      } else if (mm.isMountable(actor, next) || mm.isAttackable(actor, next, CombatUtils.NORMAL)) {
        mDistance[next] = distance;
        mPrior[next] = previous;
        // Do not enqueue for further exploration, since we cannot move after mounting
      }
    }
  }

  private void searchWalking(final Actor actor, final Queue<Integer> queue, final double distance, final MoveMaster mm) {
    boolean engageIsCompulsory = false; // Mover is assumed not engaged at start of search
    while (!queue.isEmpty()) {
      // The queue is cells we can already reach.  All such cells will already have their
      // distance set appropriate and prior.
      final int cell = queue.remove();
      final double d = mDistance[cell];
      final boolean engaged = mWorld.checkEngagement(actor, cell, engageIsCompulsory);
      engageIsCompulsory = true;
      if (engaged) {
        // Do not enqueue for further exploration, since we cannot move after mounting
        for (final Cell c : mWorld.getCells(cell, 1, 1, false)) {
          final int cn = c.getCellNumber();
          if (mDistance[cn] < 0 && mm.isAttackable(actor, cn, CombatUtils.NORMAL)) {
            mDistance[cn] = d; // Does not actually incur any extra distance
            mPrior[cn] = cell;
          }
        }
      } else if (d + 1 <= distance) {
        // N, S, E, W
        mWorld.getCellCoordinates(cell, mCellCoords);
        for (int k = 0; k < 4; k++) {
          enqueueWalking(queue, d + 1, k, mm, actor, cell);
        }
        final double ds = d + Constants.SQRT2;
        if (ds <= distance) {
          // diagonals
          for (int k = 4; k < DELTA_X.length; k++) {
            enqueueWalking(queue, ds, k, mm, actor, cell);
          }
        }
      }
    }
  }

  private void setTargetsWalking(final Actor actor, final int source, final double distance, final MoveMaster mm) {
    final Queue<Integer> queue = new LinkedList<>();
    queue.add(source);
    searchWalking(actor, queue, distance, mm);
  }

  void setTargets(final Actor actor, final int cell, final Wizard wizard, final MoveMaster mm) {
    // The actor need not be the top item in the cell (e.g. mounted wizard)
    // Assumes actor is already selected for movement
    assert wizard.getOwner() == actor.getOwner();
    Arrays.fill(mDistance, -1);
    mDistance[cell] = 0;
    mPrior[cell] = 0;
    if (mm.isEngaged(wizard, cell)) {
      // Actor is engaged, only valid targets are adjacent enemies
      for (final Cell c : mWorld.getCells(cell, 1, 1, false)) {
        final int cn = c.getCellNumber();
        if (mDistance[cn] < 0 && mm.isAttackable(cell, cn)) {
          mDistance[cn] = 1;
          mPrior[cn] = cell;
        }
      }
    } else {
      final int sqMovement = mm.getSquaredMovementPoints(wizard, cell);
      final boolean isWizardMoving = actor instanceof Wizard;
      if (actor.is(PowerUps.FLYING)) {
        setTargetsFlying(cell, Math.sqrt(sqMovement), wizard, mm, isWizardMoving);
      } else {
        setTargetsWalking(actor, cell, Math.sqrt(sqMovement), mm);
      }
    }
  }

  double distance(final int c) {
    return mDistance[c];
  }

  int prior(final int c) {
    return mPrior[c];
  }

  Set<Integer> getPossiblePlacesToMove() {
    final HashSet<Integer> targets = new HashSet<>(mDistance.length);
    for (int k = 0; k < mDistance.length; ++k) {
      if (mDistance[k] > 0) {
        targets.add(k);
      }
    }
    return targets;
  }
}
