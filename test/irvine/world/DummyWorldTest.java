package irvine.world;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import irvine.math.Shuffle;

/**
 * Tests abstract world functionality.
 * @author Sean A. Irvine
 */
public class DummyWorldTest extends AbstractWorldTest {


  private static class DummyWorld<C> extends AbstractWorld<C> {
    DummyWorld(final int w, final int h) {
      super(w, h);
    }

    @Override
    public int getSquaredDistance(final int a, final int b) {
      return 0;
    }

    @Override
    public int getCellNumber(final int x, final int y) {
      if (x < 0 || y < 0 || x >= width() || y >= height()) {
        return -1;
      }
      return y * width() + x;
    }

    @Override
    public boolean isEdge(final int a) {
      return false;
    }
  }

  private static World<Integer> getWorld(final int w, final int h) {
    return new DummyWorld<>(w, h);
  }

  @Override
  public World<Integer> getWorld() {
    return getWorld(11, 7);
  }

  private void initWorld() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
  }

  private static class MyFilter implements CellFilter<Integer> {
    private final boolean[] mBombs;

    MyFilter(final boolean[] bombs) {
      mBombs = bombs;
    }

    @Override
    public boolean accept(final Integer a) {
      return a == null || mBombs[a];
    }
  }

  public void testPathsHard() {
    final Random r = new Random(42);
    for (int j = 0; j < 5; ++j) {
      //      System.err.println("#");
      final World<Integer> w = getWorld(256, 67);
      //      final World<Integer> w = getWorld(1024, 1025);
      final boolean[] bombs = new boolean[w.size()];
      final int[] shuffle = Shuffle.shuffle(w.size());
      for (int k = 0; k < bombs.length; ++k) {
        w.setCell(k, k);
        bombs[k] = r.nextBoolean();
      }
      final CellFilter<Integer> cf = new MyFilter(bombs);
      final int origin = r.nextInt(w.size());
      final int[] length = new int[w.size()];
      for (int k = 0; k < w.size(); ++k) {
        final List<Integer> l = w.shortestPath(origin, k, cf);
        length[k] = l == null ? -1 : l.size();
      }
      w.shortestPath(-1, 0, null); // reset
      for (int k = 0; k < w.size(); ++k) {
        final int kk = shuffle[k];
        final List<Integer> l = w.shortestPath(origin, kk, cf);
        if (l == null) {
          assertEquals(-1, length[kk]);
        } else {
          assertEquals(length[kk], l.size());
        }
      }
    }
  }

  private void checkRemoveFails(final Iterator<Integer> i) {
    try {
      i.remove();
      fail();
    } catch (final UnsupportedOperationException e) {
      // ok
    }
  }

  public void testIterator() {
    initWorld();
    final Iterator<Integer> i = mWorld.iterator();
    checkRemoveFails(i);
    for (int k = 0; k < mWorld.size(); ++k) {
      assertTrue(i.hasNext());
      assertEquals(k, i.next().intValue());
    }
    checkRemoveFails(i);
    assertFalse(i.hasNext());
    try {
      i.next();
      fail();
    } catch (final NoSuchElementException e) {
      // ok
    }
  }

  public void testColumnMajorIterator() {
    initWorld();
    final Iterator<Integer> i = mWorld.columnMajorIterator();
    checkRemoveFails(i);
    for (int x = 0; x < mWorld.width(); ++x) {
      for (int c = x; c < mWorld.size(); c += mWorld.width()) {
        assertTrue(i.hasNext());
        assertEquals(c, i.next().intValue());
      }
    }
    checkRemoveFails(i);
    assertFalse(i.hasNext());
    try {
      i.next();
      fail();
    } catch (final NoSuchElementException e) {
      // ok
    }
  }

  public void testRandomIterator() {
    initWorld();
    final Iterator<Integer> i = mWorld.randomIterator();
    checkRemoveFails(i);
    final boolean[] seen = new boolean[mWorld.size()];
    final int[] order = new int[mWorld.size()];
    boolean mixed = false;
    for (int k = 0; k < mWorld.size(); ++k) {
      assertTrue(i.hasNext());
      final int q = i.next();
      assertFalse(seen[q]);
      seen[q] = true;
      mixed |= k != q;
      order[q] = k;
    }
    assertTrue(mixed);
    for (int k = 0; k < mWorld.size(); ++k) {
      assertTrue(seen[k]);
    }
    checkRemoveFails(i);
    assertFalse(i.hasNext());
    try {
      i.next();
      fail();
    } catch (final NoSuchElementException e) {
      // ok
    }
    final Iterator<Integer> j = mWorld.randomIterator();
    checkRemoveFails(j);
    final int[] order2 = new int[mWorld.size()];
    for (int k = 0; k < mWorld.size(); ++k) {
      assertTrue(j.hasNext());
      final int q = j.next();
      assertTrue(seen[q]);
      seen[q] = false;
      order2[q] = k;
    }
    for (int k = 0; k < mWorld.size(); ++k) {
      assertFalse(seen[k]);
    }
    int c = 0;
    for (int k = 0; k < order.length; ++k) {
      if (order[k] == order2[k]) {
        ++c;
      }
    }
    assertTrue("c=" + c, c < 10);
  }

  public void testBadSize() {
    try {
      getWorld(-1, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad dimension.", e.getMessage());
    }
    try {
      getWorld(10, -1);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      getWorld(0, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      getWorld(10, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      getWorld(10, 65536);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      getWorld(65536, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void testShortestPath2() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    mWorld.shortestPath(17, 19, MY_FILTER);
    final List<Integer> r = mWorld.shortestPath(17, 50, MY_FILTER);
    assertEquals(3, r.size());
    assertEquals(28, r.get(0).intValue());
    assertEquals(39, r.get(1).intValue());
    assertEquals(50, r.get(2).intValue());
  }

}
