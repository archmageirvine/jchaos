package irvine.util;

import java.util.Arrays;
import java.util.Random;

/**
 * Compute a maximally separated sets of points in a rectangle. A perturbation
 * algorithm is used and it is not guaranteed that the separation will be
 * optimal, but it should be within a tiny fraction of an optimal solution
 * most of the time.  The time required to find a solution increases as the
 * number of points increases.
 * @author Sean A. Irvine
 */
public final class MaximumSeparation {

  /** Prevent instantiation. */
  private MaximumSeparation() {
  }

  /** Delta arrays for considering adjacent points. */
  private static final int[] DX = {-1, 1, 0, 0, -1, 1, -1, 1};
  private static final int[] DY = {0, 0, -1, 1, -1, 1, 1, -1};

  /**
   * Compute array of deltas between all pairs of the specified points. If
   * x has length s then the resulting array has length s(s-1)/2. The values
   * in the result are sorted into ascending order and consist of the
   * squared-distance between every pair of points. <code>x[i]</code> and
   * <code>y[i]</code> are the coordinates of point <i>i</i>.
   * @param x x-coordinates
   * @param y y-coordinates
   * @return ascending deltas
   */
  private static int[] sortedSquaredDeltas(final int[] x, final int[] y) {
    assert x.length == y.length;
    final int[] d = new int[x.length * (x.length - 1) / 2];
    for (int i = 0, k = 0; i < x.length; ++i) {
      for (int j = i + 1; j < x.length; ++j) {
        final int dx = x[i] - x[j];
        final int dy = y[i] - y[j];
        d[k++] = dx * dx + dy * dy;
      }
    }
    Arrays.sort(d);
    return d;
  }

  /**
   * Compare two sorted arrays of deltas to determine which arrays corresponds
   * to better spaced arrangement of points.  The result is +1 is the first
   * arrangement is better than the second, -1 if the second arrangement is
   * better than the first, and 0 if they are equally well spaced.
   * @param da sorted deltas for first arrangement
   * @param db sorted deltas for second arrangement
   * @return comparison value
   */
  private static int compare(final int[] da, final int[] db) {
    assert da.length == db.length;
    for (int i = 0; i < da.length; ++i) {
      if (da[i] > db[i]) {
        return 1;
      } else if (da[i] < db[i]) {
        return -1;
      }
    }
    return 0;
  }

  /**
   * Find an arrangement of <code>n</code> points in a grid of size
   * <code>w</code>&times;<code>h</code> such that the points are maximally spaced
   * in the grid.  The <code>iterations</code> parameter controls how much effort
   * is made to ensure the solution is optimal.  More iterations increases the
   * likelihood of finding an optimal solution.  The method used to determine
   * <i>maximally spaced</i> consists of a cascaded minimum distance between points
   * metric.<p>
   *
   * Three integer arrays are returned. The first two arrays are of length <code>
   * p</code> and consist of the resulting coordinates for the points.  The third
   * array contains the squared-distances between every pair of points sorted into
   * ascending order.
   * @param w width of region
   * @param h height of region
   * @param n number of points
   * @param iterations effort
   * @return point positions and deltas arrays
   * @throws IllegalArgumentException if a dimension or the number of iterations
   * is less than 1 or the number of points is negative.
   */
  public static int[][] separate(final int w, final int h, final int n, final int iterations) {
    if (w < 1 || h < 1 || n < 0 || iterations < 1) {
      throw new IllegalArgumentException();
    }
    // It takes at least three points to get stuck in a local maximum, so there
    // is no point in making more than a single iteration for n < 3.
    final int it = n < 3 ? 1 : iterations;
    final Random random = new Random();
    final int[][] globalBest = new int[3][];
    for (int k = 0; k < it; ++k) {
      final int[] x = new int[n];
      final int[] y = new int[n];
      // Initial random assignment for this iteration.
      for (int i = 0; i < n; ++i) {
        x[i] = random.nextInt(w);
        y[i] = random.nextInt(h);
      }
      int[] bestDelta = sortedSquaredDeltas(x, y);
      if (globalBest[0] == null) {
        globalBest[0] = x;
        globalBest[1] = y;
        globalBest[2] = bestDelta;
      }
      boolean cont;
      do {
        cont = true;
        // Systematically perturb each point in turn.
        for (int s = 0; cont && s < x.length; ++s) {
          final int ox = x[s];
          final int oy = y[s];
          // Perturb a point in up to 8 directions.
          for (int u = 0; cont && u < DX.length; ++u) {
            final int tx = ox + DX[u];
            final int ty = oy + DY[u];
            // Check if perturbation is within rectangle.
            if (tx >= 0 && tx < w && ty >= 0 && ty < h) {
              x[s] = tx;
              y[s] = ty;
              final int[] sc = sortedSquaredDeltas(x, y);
              if (compare(sc, bestDelta) > 0) {
                // Perturbed arrangement is better than the current one.
                // Update current arrangement and restart the perturbation step.
                bestDelta = sc;
                cont = false;
              }
            }
          }
          // Restore current arrangement if no improvement was found.
          if (cont) {
            x[s] = ox;
            y[s] = oy;
          }
        }
        // Keep going until such time as no improvement occurred.
      } while (!cont);
      // Test if the result of this iteration is better than the current
      // optimal solution.  If so replace the global best solution with
      // the solution just found.
      if (compare(bestDelta, globalBest[2]) > 0) {
        globalBest[0] = x;
        globalBest[1] = y;
        globalBest[2] = bestDelta;
      }
    }
    return globalBest;
  }

  private static int vectorLength(final int[] v) {
    int r = 0;
    for (final int x : v) {
      r += x;
    }
    return r;
  }

  /** Print a LaTeX style rendition of the result. */
  private static void texPrint(final int w, final int h, final int[][] res) {
    System.out.println("%Score: " + vectorLength(res[2]));
    final int[][] z = new int[w][h];
    for (int i = 0; i < res[0].length; ++i) {
      z[res[0][i]][res[1][i]]++;
    }
    System.out.println("\\def\\pp{\\phantom{$\\bullet$}}");
    System.out.print("\\begin{tabular}{|");
    for (int i = 0; i < w; ++i) {
      System.out.print("c|");
    }
    System.out.println('}');
    System.out.println("\\hline");
    for (int j = 0; j < h; ++j) {
      for (int i = 0; i < w; ++i) {
        if (i != 0) {
          System.out.print('&');
        }
        switch (z[i][j]) {
          case 0:
            System.out.print("\\pp");
            break;
          case 1:
            System.out.print("$\\bullet$");
            break;
          default:
            System.out.print(z[i][j]);
            break;
        }
      }
      System.out.println("\\\\\\hline");
    }
    System.out.println("\\end{tabular}");
  }

  /** Print a PBM style rendition of the arrangement. */
  private static void pbmPrint(final int w, final int h, final int[][] res) {
    final int[][] z = new int[w][h];
    for (int i = 0; i < res[0].length; ++i) {
      z[res[0][i]][res[1][i]]++;
    }
    System.out.println("P1");
    System.out.println("#Score: " + vectorLength(res[2]));
    System.out.println(w + " " + h);
    for (int j = 0; j < h; ++j) {
      for (int i = 0; i < w; ++i) {
        System.out.print(' ');
        System.out.print(z[i][j] > 0 ? "1" : "0");
      }
      System.out.println();
    }
  }

  /** Print an ASCII rendition of the arrangement. */
  private static void print(final int w, final int h, final int[][] res) {
    final int[] x = res[0];
    final int[] y = res[1];
    System.out.print("Score:");
    for (int t = 0; t < res[2].length; ++t) {
      System.out.print(" " + res[2][t]);
    }
    System.out.println();
    final int[][] z = new int[w][h];
    for (int i = 0; i < x.length; ++i) {
      z[x[i]][y[i]]++;
    }
    System.out.print('+');
    for (int i = 0; i < w; ++i) {
      System.out.print('-');
    }
    System.out.println('+');
    for (int j = 0; j < h; ++j) {
      System.out.print('|');
      for (int i = 0; i < w; ++i) {
        System.out.print(String.valueOf(z[i][j]).replace('0', ' ').replace('1', '*'));
      }
      System.out.println('|');
    }
    System.out.print('+');
    for (int i = 0; i < w; ++i) {
      System.out.print('-');
    }
    System.out.println('+');
  }


  /**
   * A main that provides a facility to dump arrangements in various forms.
   * Should be called with the width, height, number of points, and optional
   * number of iterations. If the first argument is "-t" the result is dumped
   * as a LaTeX table. If the first argument is "-p" a PBM image is dumped.
   * Otherwise the dump is ASCII.
   * @param args arguments
   */
  public static void main(final String... args) {
    int q = 0;
    boolean texPrint = false;
    boolean pbmPrint = false;
    if ("-t".equals(args[q])) {
      ++q;
      texPrint = true;
    } else if ("-p".equals(args[q])) {
      ++q;
      pbmPrint = true;
    }
    final int w = Integer.parseInt(args[q++]);
    final int h = Integer.parseInt(args[q++]);
    final int p = Integer.parseInt(args[q++]);
    final int i = args.length > q ? Integer.parseInt(args[q]) : 10;
    final int[][] res = separate(w, h, p, i);
    if (texPrint) {
      texPrint(w, h, res);
    } else if (pbmPrint) {
      pbmPrint(w, h, res);
    } else {
      print(w, h, res);
    }
  }
}
