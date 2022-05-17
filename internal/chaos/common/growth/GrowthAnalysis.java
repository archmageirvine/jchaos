package chaos.common.growth;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Growth;
import chaos.util.Random;
import irvine.math.r.DoubleUtils;

/**
 * Compute statistics pertaining to growths.
 * @author Sean A. Irvine
 */
public final class GrowthAnalysis {

  private GrowthAnalysis() { }

  private static int countGrowthCells(final World w) {
    int c = 0;
    for (int i = 0; i < w.size(); ++i) {
      if (w.actor(i) != null) {
        ++c;
      }
    }
    return c;
  }

  private static void grow(final World w, final int p) {
    for (int i = 0; i < w.size(); ++i) {
      if (w.actor(i) != null) {
        w.actor(i).setMoved(true);
      }
    }
    for (int i = 0; i < w.size(); ++i) {
      final Actor a = w.actor(i);
      if (a != null && a.isMoved() && Random.nextInt(100) < p) {
        ((Growth) a).grow(i, w);
      } else if (Random.nextInt(200) < p) { // equiv p/2
        w.getCell(i).pop();
      }
    }
    for (int i = 0; i < w.size(); ++i) {
      if (w.actor(i) != null) {
        w.actor(i).setMoved(false);
      }
    }
  }

  private static void growthsOverTime(final int p) {

    final int totalTurns = 100;
    final int[] totalSize = new int[totalTurns];

    final int n = 1000;
    for (int j = 0; j < n; ++j) {
      final World w = new World(17, 14);
      final int size = w.size();
      // choose location for initial growth at random
      w.getCell(Random.nextInt(size)).push(new GooeyBlob());
      int c;
      for (int i = 0; i < totalTurns; ++i) {
        c = countGrowthCells(w);
        totalSize[i] += c;
        grow(w, p);
      }
    }
    System.out.println("# Mean size of growth at time t");
    for (int i = 0; i < totalTurns; ++i) {
      System.out.println(i + " " + DoubleUtils.NF5.format((double) totalSize[i] / n));
    }
  }

//   private static void growthsOverTimeModified(final int p) {

//     final int totalTurns = 100;
//     final int[] totalSize = new int[totalTurns];
//     final long[] totalSizeSq = new long[totalTurns];

//     final int n = 1000;
//     for (int j = 0; j < n; ++j) {
//       final World w = new World(17, 14);
//       final int size = w.size();
//       // choose location for initial growth at random
//       w.getCell(Random.nextInt(size)).push(new GooeyBlob());
//       int c;
//       for (int i = 0; i < totalTurns; ++i) {
//         c = countGrowthCells(w);
//         totalSize[i] += c;
//         totalSizeSq[i] += c * c;
//         grow(w, p * (1 + 6 / (c + 1)));
//       }
//     }
//     System.out.println("# Mean size of growth at time t");
//     for (int i = 0; i < totalTurns; ++i) {
//       System.out.println(i + " " + NF.format((double) totalSize[i] / n) + " " + NF.format(Math.sqrt((totalSizeSq[i] - ((double) totalSize[i] * (double) totalSize[i]) / n) / n)));
//     }
//   }


  private static int computeTurns(final int p, final int cover) {
    final World w = new World(17, 14);
    final int size = w.size();
    // choose location for initial growth at random
    w.getCell(Random.nextInt(size)).push(new GooeyBlob());
    int turns = 0;
    int c;
    while ((c = countGrowthCells(w)) < size * cover / 100 && c != 0 && turns < 1000) {
      grow(w, p);
      ++turns;
    }
    return c == 0 ? -turns : turns;
  }

  private static void computeMean(final int p, final int n, final int cover) {
    int total = 0;
    int died = 0;
    for (int i = 0; i < n; ++i) {
      final int v = computeTurns(p, cover);
      if (v > 0) {
        total += v;
      } else {
        ++died;
      }
    }
    System.out.println(p + " " + DoubleUtils.NF5.format((double) total / (n - died)) + " " + DoubleUtils.NF5.format(100.0 * died / n));
  }

  /**
   * Run the growth analysis. See usage information.
   *
   * @param args parameters
   */
  public static void main(final String[] args) {
    if (args.length == 0) {
      System.err.println("Usage:");
      System.err.println("-c iterations n       for n% coverage plot");
      System.err.println("-t rate               number of growths plot");
      return;
    }
    if ("-c".equals(args[0])) {
      for (int p = 100; p >= 0; --p) {
        computeMean(p, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
      }
    } else if ("-t".equals(args[0])) {
      growthsOverTime(Integer.parseInt(args[1]));
    }
  }

}
