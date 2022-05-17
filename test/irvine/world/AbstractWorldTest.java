package irvine.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Tests basic functionality that all worlds should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractWorldTest extends TestCase {


  /** The world to test. */
  protected World<Integer> mWorld;

  /**
   * Get the world to test. It should have dimensions 11 by 7.
   *
   * @return the world
   */
  public abstract World<Integer> getWorld();

  @Override
  public void setUp() {
    mWorld = getWorld();
  }

  @Override
  public void tearDown() {
    mWorld = null;
  }

  public void testWidth() {
    assertEquals(11, mWorld.width());
  }

  public void testHeight() {
    assertEquals(7, mWorld.height());
  }

  public void testSize() {
    assertEquals(77, mWorld.size());
  }

  public void testIsReal() {
    assertFalse(mWorld.isReal(-1));
    assertFalse(mWorld.isReal(-Integer.MAX_VALUE));
    assertFalse(mWorld.isReal(mWorld.size()));
    assertFalse(mWorld.isReal(Integer.MAX_VALUE));
    assertFalse(mWorld.isReal(mWorld.size() + 1));
    assertFalse(mWorld.isReal(65536));
    for (int k = 0; k < mWorld.size(); ++k) {
      assertTrue(mWorld.isReal(k));
    }
  }

  public void testGetAndSetCell() {
    final Integer o = 2;
    try {
      mWorld.setCell(-1, o);
      fail("Set at bad cell");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad cell number.", e.getMessage());
    }
    try {
      mWorld.setCell(mWorld.size(), o);
      fail("Set at bad cell");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad cell number.", e.getMessage());
    }
    try {
      mWorld.setCell(65536, o);
      fail("Set at bad cell");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad cell number.", e.getMessage());
    }
    for (int k = 0; k < mWorld.size(); ++k) {
      assertNull(mWorld.getCell(k));
      final Integer obj = k;
      mWorld.setCell(k, obj);
      assertEquals(obj, mWorld.getCell(k));
    }
    assertNull(mWorld.getCell(-1));
    assertNull(mWorld.getCell(mWorld.size()));
    assertNull(mWorld.getCell(4234));
  }

  public void testGetCellNumber() {
    assertEquals(-1, mWorld.getCellNumber(0));
    final Integer[] o = new Integer[mWorld.size()];
    for (int k = 0; k < mWorld.size(); ++k) {
      o[k] = k;
      mWorld.setCell(k, o[k]);
    }
    for (int k = 0; k < mWorld.size(); ++k) {
      assertEquals(k, mWorld.getCellNumber(o[k]));
    }
    assertEquals(-1, mWorld.getCellNumber(-2));
  }

  public void testGetCellCoordinates() {
    try {
      mWorld.getCellCoordinates(0, null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      mWorld.getCellCoordinates(0, new int[0]);
      fail();
    } catch (final ArrayIndexOutOfBoundsException e) {
      // ok
    }
    try {
      mWorld.getCellCoordinates(0, new int[1]);
      fail();
    } catch (final ArrayIndexOutOfBoundsException e) {
      // ok
    }
    try {
      mWorld.getCellCoordinates(-1, new int[1]);
      fail();
    } catch (final ArrayIndexOutOfBoundsException e) {
      // ok
    }
    final int[] t = new int[3];
    final int width = mWorld.width();
    final int height = mWorld.height();
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        Arrays.fill(t, 42);
        assertTrue(mWorld.getCellCoordinates(y * width + x, t));
        assertEquals(x, t[0]);
        assertEquals(y, t[1]);
        assertEquals(42, t[2]);
      }
    }
    assertFalse(mWorld.getCellCoordinates(-1, t));
    assertEquals(-1, t[0]);
    assertEquals(-1, t[1]);
    assertEquals(42, t[2]);
    t[0] = 5;
    t[1] = 5;
    assertFalse(mWorld.getCellCoordinates(mWorld.size(), t));
    assertEquals(-1, t[0]);
    assertEquals(-1, t[1]);
    assertEquals(42, t[2]);
  }

  private static final CellFilter<Integer> FORBID_ALL = z -> false;

  protected static final CellFilter<Integer> FORBID_PRIME = z -> {
    //      System.err.println("Testing: " + z);
    if (z == null) {
      return false;
    }
    final int zz = z;
    if ((zz & 1) == 0) {
      return zz != 2;
    }
    if (zz % 3 == 0) {
      return zz != 3;
    }
    if (zz % 5 == 0) {
      return zz != 5;
    }
    if (zz % 7 == 0) {
      return zz != 7;
    }
    return zz == 1;
  };

  public void testGetCells1() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    try {
      mWorld.getCells(-1, 0, 1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad origin", e.getMessage());
    }
    try {
      mWorld.getCells(mWorld.size(), 0, 1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad origin", e.getMessage());
    }
    try {
      mWorld.getCells(0, -1, 1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad radius", e.getMessage());
    }
    try {
      mWorld.getCells(10, 0, -1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad radius", e.getMessage());
    }
    try {
      mWorld.getCells(3, 2, 1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad radius", e.getMessage());
    }
    try {
      mWorld.getCells(4, 0, -1, FORBID_ALL);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    assertEquals(0, mWorld.getCells(38, 0, 0, FORBID_ALL).size());
    assertEquals(0, mWorld.getCells(38, 0, 1, FORBID_ALL).size());
    assertEquals(0, mWorld.getCells(38, 1, 1, FORBID_ALL).size());
    assertEquals(0, mWorld.getCells(38, 0, 20, FORBID_ALL).size());
    Set<Integer> s = mWorld.getCells(38, 0, 0, null);
    assertEquals(1, s.size());
    assertTrue(s.contains(38));
    s = mWorld.getCells(38, 0, 1, null);
    assertEquals(9, s.size());
    assertTrue(s.contains(38));
    assertTrue(s.contains(37));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    s = mWorld.getCells(38, 1, 1, null);
    assertEquals(8, s.size());
    assertTrue(s.contains(37));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    s = mWorld.getCells(38, 0, 2, null);
    assertEquals(21, s.size());
    assertTrue(s.contains(38));
    assertTrue(s.contains(37));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    assertTrue(s.contains(15));
    assertTrue(s.contains(16));
    assertTrue(s.contains(17));
    assertTrue(s.contains(25));
    assertTrue(s.contains(29));
    assertTrue(s.contains(36));
    assertTrue(s.contains(40));
    assertTrue(s.contains(47));
    assertTrue(s.contains(51));
    assertTrue(s.contains(59));
    assertTrue(s.contains(60));
    assertTrue(s.contains(61));
    s = mWorld.getCells(38, 1, 2, null);
    assertEquals(20, s.size());
    assertTrue(s.contains(37));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    assertTrue(s.contains(15));
    assertTrue(s.contains(16));
    assertTrue(s.contains(17));
    assertTrue(s.contains(25));
    assertTrue(s.contains(29));
    assertTrue(s.contains(36));
    assertTrue(s.contains(40));
    assertTrue(s.contains(47));
    assertTrue(s.contains(51));
    assertTrue(s.contains(59));
    assertTrue(s.contains(60));
    assertTrue(s.contains(61));
    s = mWorld.getCells(38, 2, 2, null);
    assertEquals(12, s.size());
    assertTrue(s.contains(15));
    assertTrue(s.contains(16));
    assertTrue(s.contains(17));
    assertTrue(s.contains(25));
    assertTrue(s.contains(29));
    assertTrue(s.contains(36));
    assertTrue(s.contains(40));
    assertTrue(s.contains(47));
    assertTrue(s.contains(51));
    assertTrue(s.contains(59));
    assertTrue(s.contains(60));
    assertTrue(s.contains(61));
    s = mWorld.getCells(38, 2, 3, null);
    assertEquals(28, s.size());
    s = mWorld.getCells(38, 0, 3, null);
    assertEquals(37, s.size());
    s = mWorld.getCells(38, 0, 300, null);
    assertEquals(mWorld.size(), s.size());
    s = mWorld.getCells(10, 0, 30, null);
    assertEquals(mWorld.size(), s.size());
    s = mWorld.getCells(0, 0, 12, null);
    assertEquals(mWorld.size(), s.size());
    s = mWorld.getCells(76, 0, 121, null);
    assertEquals(mWorld.size(), s.size());
    s = mWorld.getCells(66, 0, 20, null);
    assertEquals(mWorld.size(), s.size());
  }

  public void testGetCells2() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    Set<Integer> s = mWorld.getCells(38, 0, 0, FORBID_PRIME);
    assertEquals(1, s.size());
    assertTrue(s.contains(38));
    s = mWorld.getCells(38, 0, 1, FORBID_PRIME);
    assertEquals(8, s.size());
    assertTrue(s.contains(38));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    s = mWorld.getCells(38, 1, 1, FORBID_PRIME);
    assertEquals(7, s.size());
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    s = mWorld.getCells(38, 0, 2, FORBID_PRIME);
    assertEquals(15, s.size());
    assertTrue(s.contains(38));
    assertTrue(s.contains(26));
    assertTrue(s.contains(27));
    assertTrue(s.contains(28));
    assertTrue(s.contains(48));
    assertTrue(s.contains(49));
    assertTrue(s.contains(50));
    assertTrue(s.contains(39));
    assertTrue(s.contains(15));
    assertTrue(s.contains(16));
    assertTrue(s.contains(25));
    assertTrue(s.contains(36));
    assertTrue(s.contains(40));
    assertTrue(s.contains(51));
    assertTrue(s.contains(60));
    s = mWorld.getCells(38, 2, 2, FORBID_PRIME);
    assertEquals(7, s.size());
    assertTrue(s.contains(15));
    assertTrue(s.contains(16));
    assertTrue(s.contains(25));
    assertTrue(s.contains(36));
    assertTrue(s.contains(40));
    assertTrue(s.contains(51));
    assertTrue(s.contains(60));
    s = mWorld.getCells(38, 0, 3, FORBID_PRIME);
    assertEquals(28, s.size());
    s = mWorld.getCells(38, 0, 300, FORBID_PRIME);
    assertEquals(56, s.size());
    s = mWorld.getCells(10, 0, 30, FORBID_PRIME);
    assertEquals(56, s.size());
    s = mWorld.getCells(0, 0, 12, FORBID_PRIME);
    assertEquals(56, s.size());
    s = mWorld.getCells(76, 0, 121, FORBID_PRIME);
    assertEquals(56, s.size());
    s = mWorld.getCells(66, 0, 20, FORBID_PRIME);
    assertEquals(56, s.size());
  }

  public void testShortestPathGeneralBad() {
    try {
      mWorld.shortestPath(10, mWorld.size(), null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Invalid target", e.getMessage());
    }
    try {
      mWorld.shortestPath(10, -1, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Invalid target", e.getMessage());
    }
    try {
      mWorld.shortestPath(-2, 10, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Invalid origin", e.getMessage());
    }
    try {
      mWorld.shortestPath(mWorld.size(), 10, null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Invalid origin", e.getMessage());
    }
    // this next one does reset, just make sure it doesn't die
    mWorld.shortestPath(-1, 0, null);
  }

  private static class NowhereFilter implements CellFilter<Integer> {
    @Override
    public boolean accept(final Integer x) {
      return true;
    }
  }

  private static class Mod7Filter implements CellFilter<Integer> {
    @Override
    public boolean accept(final Integer x) {
      return x == null || x % 7 == 0;
    }
  }

  private static class Mod3Filter implements CellFilter<Integer> {
    @Override
    public boolean accept(final Integer x) {
      return x == null || x % 3 == 0;
    }
  }

  public void testShortestPathGeneral() {
    // test all forbidden
    final CellFilter<Integer> nowhere = new NowhereFilter();
    final Random r = new Random();
    for (int k = 0; k < 2; ++k) {
      final int o = r.nextInt(mWorld.size());
      for (int j = 0; j < mWorld.size(); ++j) {
        if (j == o) {
          assertEquals(0, mWorld.shortestPath(o, j, nowhere).size());
        } else {
          assertNull(o + " " + j, mWorld.shortestPath(o, j, nowhere));
        }
      }
    }
    final CellFilter<Integer> mod7 = new Mod7Filter();
    for (int k = 0; k < 2; ++k) {
      int a, b;
      do {
        a = r.nextInt(mWorld.size());
      } while (a % 7 == 0);
      do {
        b = r.nextInt(mWorld.size());
      } while (b % 7 == 0);
      final List<Integer> s = mWorld.shortestPath(a, b, mod7);
      final List<Integer> t = mWorld.shortestPath(b, a, mod7);
      if (s == null) {
        assertNull(t);
      } else if (a == b) {
        assertEquals(0, s.size());
        assertEquals(0, t.size());
      } else {
        assertTrue(!s.isEmpty());
        assertEquals(s.size(), t.size());
        assertEquals(b, s.get(s.size() - 1).intValue());
        assertEquals(a, t.get(t.size() - 1).intValue());
      }
    }
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    for (int k = 0; k < 5; ++k) {
      final int o = r.nextInt(mWorld.size());
      for (int j = 0; j < mWorld.size(); ++j) {
        if (j == o) {
          assertEquals(0, mWorld.shortestPath(o, j, nowhere).size());
        } else {
          assertNull(o + " " + j, mWorld.shortestPath(o, j, nowhere));
        }
      }
    }
    for (int k = 0; k < 20; ++k) {
      int a, b;
      do {
        a = r.nextInt(mWorld.size());
      } while (a % 7 == 0);
      do {
        b = r.nextInt(mWorld.size());
      } while (b % 7 == 0);
      final List<Integer> s = mWorld.shortestPath(a, b, mod7);
      final List<Integer> t = mWorld.shortestPath(b, a, mod7);
      if (s == null) {
        assertNull(t);
      } else if (a == b) {
        assertEquals(0, s.size());
        assertEquals(0, t.size());
      } else {
        assertTrue(!s.isEmpty());
        assertEquals(s.size(), t.size());
        assertEquals(b, s.get(s.size() - 1).intValue());
        assertEquals(a, t.get(t.size() - 1).intValue());
      }
    }
    final CellFilter<Integer> mod3 = new Mod3Filter();
    for (int k = 0; k < 20; ++k) {
      int a, b;
      do {
        a = r.nextInt(mWorld.size());
      } while (a % 3 == 0);
      do {
        b = r.nextInt(mWorld.size());
      } while (b % 3 == 0);
      final List<Integer> s = mWorld.shortestPath(a, b, mod3);
      final List<Integer> t = mWorld.shortestPath(b, a, mod3);
      if (s == null) {
        assertNull(t);
      } else if (a == b) {
        assertEquals(0, s.size());
        assertEquals(0, t.size());
      } else {
        assertTrue(!s.isEmpty());
        if (s.size() != t.size()) {
          final StringBuilder sb = new StringBuilder("s path:");
          for (final Integer i : s) {
            sb.append(' ').append(i);
          }
          sb.append('\n');
          sb.append("t path:");
          for (final Integer i : t) {
            sb.append(' ').append(i);
          }
          sb.append('\n');
          sb.append("Start at vertex: a=").append(a);
          sb.append('\n');
          sb.append("End at vertex: b=").append(b);
          sb.append('\n');
          sb.append("World dimensions: ").append(mWorld.width()).append(' ').append(mWorld.height()).append(' ').append(mWorld.getClass().getName());
          sb.append('\n');
          for (int y = 0; y < mWorld.height(); ++y) {
            for (int x = 0; x < mWorld.width(); ++x) {
              sb.append(mWorld.getCell(x, y) % 3 == 0 ? '*' : '.');
            }
            sb.append('\n');
          }
          fail(sb.toString());
        }
        assertEquals(b, s.get(s.size() - 1).intValue());
        assertEquals(a, t.get(t.size() - 1).intValue());
      }
    }
    mWorld.shortestPath(30, 3, null);
  }

  /*
   * The following world is used for some specific path testing.
   *
   * +-----------+
   * |xxxx.xxxxxx|
   * |x..x.x....x|
   * |xxxxxx.xx.x|
   * |.....x.xx.x|
   * |...x.x....x|
   * |...x.xxxxxx|
   * |...x...x...|
   * +-----------+
   */
  private static final boolean[] MY_MAP = {
    true,  true,  true,  true,  false, true,  true,  true,  true,  true,  true,
    true,  false, false, true,  false, true,  false, false, false, false, true,
    true,  true,  true,  true,  true,  true,  false, true,  true,  false, true,
    false, false, false, false, false, true,  false, true,  true,  false, true,
    false, false, false, true,  false, true,  false, false, false, false, true,
    false, false, false, true,  false, true,  true,  true,  true,  true,  true,
    false, false, false, true,  false, false, false, true, false, false, false,
  };

  static final CellFilter<Integer> MY_FILTER = x -> x == null || MY_MAP[x];

  public void testShortestPath() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    List<Integer> r = mWorld.shortestPath(17, 50, MY_FILTER);
    assertEquals(3, r.size());
    assertEquals(28, r.get(0).intValue());
    assertEquals(39, r.get(1).intValue());
    assertEquals(50, r.get(2).intValue());
    r = mWorld.shortestPath(17, 52, MY_FILTER);
    assertEquals(4, r.size());
    assertEquals(28, r.get(0).intValue());
    assertEquals(39, r.get(1).intValue());
    assertEquals(51, r.get(2).intValue());
    assertEquals(52, r.get(3).intValue());
    assertNull(mWorld.shortestPath(17, 29, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 64, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 12, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 13, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 14, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 66, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 29, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 64, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 12, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 13, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 14, MY_FILTER));
    assertNull(mWorld.shortestPath(17, 66, MY_FILTER));
    assertNull(mWorld.shortestPath(34, 12, MY_FILTER));
    r = mWorld.shortestPath(19, 39, MY_FILTER);
    assertEquals(3, r.size());
    assertEquals(18, r.get(0).intValue());
    assertEquals(28, r.get(1).intValue());
    assertEquals(39, r.get(2).intValue());
    assertEquals(0, mWorld.shortestPath(12, 12, MY_FILTER).size());
    r = mWorld.shortestPath(57, 33, MY_FILTER);
    assertEquals(2, r.size());
    assertEquals(45, r.get(0).intValue());
    assertEquals(33, r.get(1).intValue());
    r = mWorld.shortestPath(57, 44, MY_FILTER);
    assertEquals(2, r.size());
    assertEquals(56, r.get(0).intValue());
    assertEquals(44, r.get(1).intValue());
    r = mWorld.shortestPath(33, 57, MY_FILTER);
    assertEquals(2, r.size());
    assertEquals(45, r.get(0).intValue());
    assertEquals(57, r.get(1).intValue());
    r = mWorld.shortestPath(44, 57, MY_FILTER);
    assertEquals(2, r.size());
    assertEquals(45, r.get(0).intValue());
    assertEquals(57, r.get(1).intValue());
  }

}
