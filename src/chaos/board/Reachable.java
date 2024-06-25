package chaos.board;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import chaos.common.NoFlyOver;
import irvine.math.r.Constants;

/**
 * Compute set of reachable cells.
 * @author Sean A. Irvine
 */
public class Reachable implements Serializable {

  private static final int[] DELTA_X = {1, -1, 0, 0, 1, 1, -1, -1};
  private static final int[] DELTA_Y = {0, 0, 1, -1, 1, -1, 1, -1};

  private final World mWorld;
  private final int[] mCellCoords = new int[2];

  /**
   * Calculator for reachable cells.
   * @param world world to apply to
   */
  public Reachable(final World world) {
    mWorld = world;
  }

  private void enqueue(final Set<Cell> reachable, final Queue<Cell> queue, final Map<Cell, Double> dist, final double d, final int k) {
    final Cell next = mWorld.getCell(mCellCoords[0] + DELTA_X[k], mCellCoords[1] + DELTA_Y[k]);
    if (next != null && !reachable.contains(next) && !dist.containsKey(next)) {
      dist.put(next, d);
      queue.add(next);
    }
  }

  private void search(final Set<Cell> reachable, final Queue<Cell> queue, final Map<Cell, Double> dist) {
    while (!queue.isEmpty()) {
      final Cell cell = queue.remove();
      final double d = dist.remove(cell);
      reachable.add(cell);
      if (d >= 1 && !(cell.peek() instanceof NoFlyOver)) {
        // N, S, E, W
        mWorld.getCellCoordinates(cell.getCellNumber(), mCellCoords);
        for (int k = 0; k < 4; k++) {
          enqueue(reachable, queue, dist, d - 1, k);
        }
        final double ds = d - Constants.SQRT2;
        if (ds >= 0) {
          for (int k = 4; k < DELTA_X.length; k++) {
            enqueue(reachable, queue, dist, ds, k);
          }
        }
      }
    }
  }

  /**
   * Get all cells reachable for flying movement from given cell.
   * @param source source cell
   * @param distance maximum distance
   * @return cells which are reachable
   */
  public Set<Cell> getReachableFlying(final int source, final double distance) {
    final Set<Cell> reachable = new HashSet<>();
    final Map<Cell, Double> dist = new HashMap<>();
    final Queue<Cell> queue = new LinkedList<>();
    final Cell sourceCell = mWorld.getCell(source);
    dist.put(sourceCell, distance);
    queue.add(sourceCell);
    reachable.add(sourceCell); // So we don't return to the source
    search(reachable, queue, dist);
    reachable.remove(sourceCell); // but we don't actually want the source in the result
    return reachable;
  }

}
