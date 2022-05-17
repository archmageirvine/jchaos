package irvine.world;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import irvine.util.AbstractIterator;

/**
 * Provides implementations of common aspects of a world.
 *
 * @param <C> fundamental cell type of the world.
 *
 * @author Sean A. Irvine
 */
abstract class AbstractWorld<C> implements World<C>, Iterable<C> {

  private static final double SQRT2 = sqrt(2.0);

  /** Width of the world in cells. */
  private final int mWidth;
  /** Height of the world in cells. */
  private final int mHeight;
  /** Total number of cells in the world. */
  private final int mSize;
  /** The actual cells. */
  private final C[] mCells;
  /** Precomputed coordinate mapping. */
  protected final int[] mCellToCoords;
  /** Offset tables for all cells within a given radius. */
  private final int[] mA, mB;
  /** Limiting index for radii in above arrays. */
  private final int[] mRadialLimit;

  /**
   * Construct a world of given width and height.
   *
   * @param width width of world in cells
   * @param height height of world in cells
   * @exception IllegalArgumentException if <code>width</code> or <code>height
   * </code> is less than 1. Also if <code>width</code> or <code>height</code>
   * exceeds 65535.
   *
   */
  @SuppressWarnings("unchecked")
  AbstractWorld(final int width, final int height) {
    if (width < 1 || height < 1 || width >= 65536 || height >= 65536) {
      throw new IllegalArgumentException("Bad dimension.");
    }
    mWidth = width;
    mHeight = height;
    mSize = width * height;
    // Java generics do not allow "new C[mSize]" here!
    mCells = (C[]) new Object[mSize];

    // compute coordinate mapping
    mCellToCoords = new int[mSize];
    mShortestPathTrace = new int[mSize];
    final int limit = height << 16;
    for (int y = 0, k = 0; y < limit; y += 0x10000) {
      for (int x = 0; x < width; ++x) {
        mCellToCoords[k++] = y + x;
      }
    }

    // precompute arrays used in the selection of cells within a given radius.
    final int maxLength = max(width, height);
    final int[] columnPosition = new int[maxLength];
    mA = new int[maxLength * (maxLength + 1) / 2];
    mB = new int[mA.length];
    // next trickery ensures mRadialLimit is exactly the right length
    // fiddle with these numbers at your own risk
    mRadialLimit = new int[1 + (int) (0.5 + SQRT2 * (maxLength - 1))];
    for (int r = 0, k = 0, u = 0; k != mA.length; ++r) {
      final int t = (int) ((r + 0.5) * (r + 0.5));
      for (int a = u; a < maxLength; ++a) {
        final int s = t - a * a;
        while (columnPosition[a] <= a && columnPosition[a] * columnPosition[a] <= s) {
          mA[k] = a;
          mB[k++] = columnPosition[a]++;
        }
        // next condition is only for efficiency in large worlds
        if (columnPosition[a] > a) {
          ++u;
        }
      }
      mRadialLimit[r] = k - 1;
    }
  }

  @Override
  public int width() {
    return mWidth;
  }

  @Override
  public int height() {
    return mHeight;
  }

  @Override
  public int size() {
    return mSize;
  }

  @Override
  public boolean isReal(final int cellNumber) {
    return cellNumber >= 0 && cellNumber < size();
  }

  @Override
  public void setCell(final int cellNumber, final C cell) {
    if (!isReal(cellNumber)) {
      throw new IllegalArgumentException("Bad cell number.");
    }
    mCells[cellNumber] = cell;
  }

  @Override
  public C getCell(final int cellNumber) {
    return isReal(cellNumber) ? mCells[cellNumber] : null;
  }

  @Override
  public C getCell(final int x, final int y) {
    return getCell(getCellNumber(x, y));
  }

  @Override
  public int getCellNumber(final C cell) {
    for (int k = 0; k < size(); ++k) {
      if (getCell(k) == cell) {
        return k;
      }
    }
    return -1;
  }

  @Override
  public boolean getCellCoordinates(final int cellNumber, final int[] xy) {
    if (cellNumber < 0 || cellNumber >= size()) {
      xy[0] = -1;
      xy[1] = -1;
      return false;
    }
    final int t = mCellToCoords[cellNumber];
    xy[0] = t & 0xFFFF;
    xy[1] = t >>> 16;
    return true;
  }

  /** Workspace for <code>getCells</code> and <code>shortestPath</code>. */
  private static final int[] GC_TEMP = new int[2];

  private void addIfAccepted(final CellFilter<C> filter, final Set<C> result, final int x, final int y) {
    final C c = getCell(x, y);
    if (filter.accept(c)) {
      result.add(c);
    }
  }

  @Override
  public Set<C> getCells(final int origin, final int minRadius, final int maxRadius, final CellFilter<C> filter) {
    if (minRadius < 0 || maxRadius < minRadius) {
      throw new IllegalArgumentException("Bad radius");
    }
    final Set<C> result = new HashSet<>();
    if (!getCellCoordinates(origin, GC_TEMP)) {
      throw new IllegalArgumentException("Bad origin");
    }
    final int x = GC_TEMP[0];
    final int y = GC_TEMP[1];
    final int limit = mRadialLimit[min(mRadialLimit.length - 1, maxRadius)];
    if (filter == null) {
      // faster version when no filter needs to be applied
      for (int k = minRadius == 0 ? 0 : mRadialLimit[minRadius - 1] + 1; k <= limit; ++k) {
        final int a = mA[k];
        final int b = mB[k];
        // a can only be zero for at most the very first cell; that is, when
        // c == origin. Thus if a = 0 then b = 0 as well. b can be zero more
        // often so we make a special check for this case.
        if (b == 0) {
          result.add(getCell(x + a, y));
          if (a != 0) {
            result.add(getCell(x - a, y));
            result.add(getCell(x, y + a));
            result.add(getCell(x, y - a));
          }
        } else {
          // a != 0 && b != 0
          result.add(getCell(x + a, y + b));
          result.add(getCell(x + a, y - b));
          result.add(getCell(x - a, y + b));
          result.add(getCell(x - a, y - b));
          if (a != b) {
            result.add(getCell(x + b, y + a));
            result.add(getCell(x + b, y - a));
            result.add(getCell(x - b, y + a));
            result.add(getCell(x - b, y - a));
          }
        }
      }
      result.remove(null);
    } else {
      for (int k = minRadius == 0 ? 0 : mRadialLimit[minRadius - 1] + 1; k <= limit; ++k) {
        final int a = mA[k];
        final int b = mB[k];
        // a can only be zero for at most the very first cell; that is, when
        // c == origin. Thus if a = 0 then b = 0 as well. b can be zero more
        // often so we make a special check for this case.
        if (b == 0) {
          addIfAccepted(filter, result, x + a, y);
          if (a != 0) {
            addIfAccepted(filter, result, x - a, y);
            addIfAccepted(filter, result, x, y + a);
            addIfAccepted(filter, result, x, y - a);
          }
        } else {
          // a != 0 && b != 0
          addIfAccepted(filter, result, x + a, y + b);
          addIfAccepted(filter, result, x + a, y - b);
          addIfAccepted(filter, result, x - a, y + b);
          addIfAccepted(filter, result, x - a, y - b);
          if (a != b) {
            addIfAccepted(filter, result, x + b, y + a);
            addIfAccepted(filter, result, x + b, y - a);
            addIfAccepted(filter, result, x - b, y + a);
            addIfAccepted(filter, result, x - b, y - a);
          }
        }
      }
    }
    return result;
  }

  /**
   * Holds an entry in the queue of cells to be processed. This is necessary
   * because the TreeSet class needs a stable distance for the purposes of
   * its comparator.
   */
  private static class Tuple implements Serializable {
    final int mCell;
    final int mDistance;

    Tuple(final int cell, final int distance) {
      mCell = cell;
      mDistance = distance;
    }
  }

  private static class TupleComparator implements Comparator<Tuple>, Serializable {
    /** Distance based comparator. */
    @Override
    public int compare(final Tuple a, final Tuple b) {
      return a.equals(b) ? 0 : a.mDistance <= b.mDistance ? -1 : 1;
    }
  }

  /** Filter used during path finding. */
  private CellFilter<C> mShortestPathFilter = null;
  /** Origin for path finding. */
  private int mShortestPathOrigin = -1;
  /** Cached distance table. */
  private int[] mShortestPathDistance = null;
  /** Traceback cache. */
  private final int[] mShortestPathTrace;
  /** Queue of cells yet to be examined. */
  private final TreeSet<Tuple> mPriority = new TreeSet<>(new TupleComparator());

  /** Offsets to adjacent cells for extending the cache. */
  private static final int[] DELTAX = {1, -1,  0,  0,  1,  1, -1, -1};
  private static final int[] DELTAY = {0,  0,  1, -1,  1, -1,  1, -1};

  /** Unit in 14-bit fixed point arithmetic. */
  private static final int FP_ONE = 1 << 14;
  /** Diagonal distance. */
  private static final int FP_SQRT2 = (int) (SQRT2 * FP_ONE);

  /**
   * Attempts to compute enough path information to find the shortest path to
   * the given target.  Because caching is used, it is possible the result is
   * already known.  Otherwise, it is necessary to extend (a possibly empty)
   * previously computed result.
   *
   * @param target target cell
   */
  private void extendCache(final int target) {
    if (mShortestPathDistance[target] != 0) {
      return;
    }
    // quick check for allowable target
    if (mShortestPathFilter != null && mShortestPathFilter.accept(getCell(target))) {
      mShortestPathDistance[target] = -1;
      return;
    }
    // general extension of current cache
    while (!mPriority.isEmpty()) {
      final Tuple tuple = mPriority.first();
      final int current = tuple.mCell;
      final int d = tuple.mDistance;
      if (mShortestPathDistance[target] != 0 && d >= mShortestPathDistance[target]) {
        // As soon as the shortest distance in the queue exceeds a known distance
        // to the target, the search can be halted because no shorter path will
        // be possible.
        break;
      }
      mPriority.remove(tuple);
      if (mShortestPathDistance[current] >= d) {
        getCellCoordinates(current, GC_TEMP);
        final int x = GC_TEMP[0];
        final int y = GC_TEMP[1];
        for (int k = 0; k < DELTAX.length; ++k) {
          // process all the neighbours of a cell
          final int cn = getCellNumber(x + DELTAX[k], y + DELTAY[k]);
          if (cn >= 0 && cn != mShortestPathOrigin) {
            if (mShortestPathFilter == null || !mShortestPathFilter.accept(getCell(cn))) {
              final int dist = mShortestPathDistance[current] + (k < 4 ? FP_ONE : FP_SQRT2);
              if (mShortestPathDistance[cn] == 0 || mShortestPathDistance[cn] > dist) {
                // found a new reachable cell or a shorter way to get to a cell
                mShortestPathDistance[cn] = dist;
                mShortestPathTrace[cn] = current;
                mPriority.add(new Tuple(cn, dist));
              }
            } else {
              mShortestPathDistance[cn] = -1;
            }
          }
        }
      }
    }

    // If the queue is empty then all cells other than the origin containing
    // 0 are unreachable, and we can make later calls faster by marking them
    // with -1
    if (mPriority.isEmpty()) {
      for (int k = 0; k < mShortestPathDistance.length; ++k) {
        if (k != mShortestPathOrigin && mShortestPathDistance[k] == 0) {
          mShortestPathDistance[k] = -1;
        }
      }
    }
  }


  /**
   * Retrieve the actual path by tracing backwards from the cache.
   *
   * @param target target cell
   * @return path or null if there is no path
   */
  private List<C> retrievePath(int target) {
    assert target >= 0 && target < size();
    assert mShortestPathDistance != null;
    final int d = mShortestPathDistance[target];
    assert d != 0;
    if (d == -1) {
      return null; // forbidden cell
    }
    final LinkedList<C> result = new LinkedList<>();
    do {
      result.addFirst(getCell(target));
      target = mShortestPathTrace[target];
    } while (target != mShortestPathOrigin);
    return result;
  }

  @Override
  public List<C> shortestPath(final int origin, final int target, final CellFilter<C> filter) {
    if (!isReal(target)) {
      throw new IllegalArgumentException("Invalid target");
    }
    if (origin != -1 && !isReal(origin)) {
      throw new IllegalArgumentException("Invalid origin");
    }
    if (mShortestPathOrigin != origin || mShortestPathFilter != filter) {
      // invalidate caches
      mPriority.clear();
      mShortestPathOrigin = origin;
      mShortestPathFilter = filter;
      mShortestPathDistance = null;
      mPriority.add(new Tuple(origin, 0));
    }
    if (origin == -1) {
      // quick exit, for special invalidating origin
      return null;
    }
    if (origin == target) {
      // the only distance 0 result
      return new LinkedList<>();
    }
    if (mShortestPathDistance == null) {
      mShortestPathDistance = new int[size()];
    }
    // make sure we have the path
    extendCache(target);
    // and then retrieve it
    return retrievePath(target);
  }

  @Override
  public Iterator<C> iterator() {
    return new AbstractIterator<C>() {
      private int mCell = 0;

      @Override
      public boolean hasNext() {
        return mCell < size();
      }

      @Override
      public C next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return getCell(mCell++);
      }
    };
  }

  @Override
  public Iterator<C> columnMajorIterator() {
    return new AbstractIterator<C>() {
      private int mX = 0;
      private int mCell = 0;

      @Override
      public boolean hasNext() {
        return mX < width();
      }

      @Override
      public C next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        final C result = getCell(mCell);
        mCell += width();
        if (mCell >= size()) {
          mCell = ++mX;
        }
        return result;
      }
    };
  }

  @Override
  public Iterator<C> randomIterator() {
    return new AbstractIterator<C>() {

      private int mCell = 0;

      private final int[] mPermutation = new int[size()];
      {
        // identity
        for (int k = 0; k < mPermutation.length; ++k) {
          mPermutation[k] = k;
        }
        // shuffle
        final Random random = new Random();
        for (int k = 0; k < mPermutation.length; ++k) {
          final int t = mPermutation[k];
          final int r = k + random.nextInt(size() - k);
          mPermutation[k] = mPermutation[r];
          mPermutation[r] = t;
        }
      }

      @Override
      public boolean hasNext() {
        return mCell < size();
      }

      @Override
      public C next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return getCell(mPermutation[mCell++]);
      }
    };
  }

}
