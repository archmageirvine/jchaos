package irvine.world;

import irvine.TestUtils;

import java.util.List;
import java.util.Set;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class FlatWorldTest extends AbstractWorldTest {


  @Override
  public World<Integer> getWorld() {
    return new FlatWorld<>(11, 7);
  }

  public void testIsEdge() {
    for (int k = 0; k < 11; ++k) {
      assertTrue(mWorld.isEdge(k));
      assertTrue(mWorld.isEdge(66 + k));
    }
    assertTrue(mWorld.isEdge(11));
    assertTrue(mWorld.isEdge(21));
    assertTrue(mWorld.isEdge(22));
    assertTrue(mWorld.isEdge(32));
    assertTrue(mWorld.isEdge(33));
    assertTrue(mWorld.isEdge(43));
    assertTrue(mWorld.isEdge(44));
    assertTrue(mWorld.isEdge(54));
    assertTrue(mWorld.isEdge(55));
    assertTrue(mWorld.isEdge(65));
    for (int x = 1; x < 10; ++x) {
      for (int y = 1; y < 6; ++y) {
        assertFalse(mWorld.isEdge(y * 11 + x));
      }
    }
    assertFalse(mWorld.isEdge(-1));
    assertFalse(mWorld.isEdge(77));
    assertFalse(mWorld.isEdge(78));
  }

  @Override
  public void testGetCellNumber() {
    assertEquals(0, mWorld.getCellNumber(0, 0));
    assertEquals(-1, mWorld.getCellNumber(-1, 0));
    assertEquals(-1, mWorld.getCellNumber(0, -1));
    assertEquals(-1, mWorld.getCellNumber(-1, -1));
    assertEquals(-1, mWorld.getCellNumber(0, 7));
    assertEquals(-1, mWorld.getCellNumber(11, 0));
    assertEquals(-1, mWorld.getCellNumber(10, 7));
    assertEquals(76, mWorld.getCellNumber(10, 6));
    assertEquals(10, mWorld.getCellNumber(10, 0));
    assertEquals(66, mWorld.getCellNumber(0, 6));
    assertEquals(37, mWorld.getCellNumber(4, 3));
    assertEquals(47, mWorld.getCellNumber(3, 4));
  }

  public void testGetSquaredDistance() {
    assertEquals(-1, mWorld.getSquaredDistance(-1, 0));
    assertEquals(-1, mWorld.getSquaredDistance(-1, -1));
    assertEquals(-1, mWorld.getSquaredDistance(0, -1));
    assertEquals(-1, mWorld.getSquaredDistance(77, 0));
    assertEquals(-1, mWorld.getSquaredDistance(77, 50));
    assertEquals(-1, mWorld.getSquaredDistance(10, 77));
    final int[] t = new int[2];
    for (int a = 0; a < mWorld.size(); ++a) {
      for (int b = 0; b < mWorld.size(); ++b) {
        assertTrue(mWorld.getCellCoordinates(a, t));
        int x = t[0];
        int y = t[1];
        assertTrue(mWorld.getCellCoordinates(b, t));
        x -= t[0];
        y -= t[1];
        assertEquals(x * x + y * y, mWorld.getSquaredDistance(a, b));
      }
    }
  }

  @Override
  public void testGetCells1() {
    super.testGetCells1();
    Set<Integer> s = mWorld.getCells(71, 0, 1, null);
    assertEquals(6, s.size());
    assertTrue(s.contains(59));
    assertTrue(s.contains(60));
    assertTrue(s.contains(61));
    assertTrue(s.contains(70));
    assertTrue(s.contains(71));
    assertTrue(s.contains(72));
    s = mWorld.getCells(71, 1, 1, null);
    assertEquals(5, s.size());
    assertTrue(s.contains(59));
    assertTrue(s.contains(60));
    assertTrue(s.contains(61));
    assertTrue(s.contains(70));
    assertTrue(s.contains(72));
  }

  @Override
  public void testGetCells2() {
    super.testGetCells2();
    Set<Integer> s = mWorld.getCells(71, 0, 1, AbstractWorldTest.FORBID_PRIME);
    assertEquals(3, s.size());
    assertTrue(s.contains(60));
    assertTrue(s.contains(70));
    assertTrue(s.contains(72));
    s = mWorld.getCells(71, 1, 1, AbstractWorldTest.FORBID_PRIME);
    assertEquals(3, s.size());
    assertTrue(s.contains(60));
    assertTrue(s.contains(70));
    assertTrue(s.contains(72));
  }

  @Override
  public void testShortestPath() {
    super.testShortestPath();
    final List<Integer> r = mWorld.shortestPath(66, 72, MY_FILTER);
    assertEquals(7, r.size());
    assertEquals(56, r.get(0).intValue());
    assertEquals(46, r.get(1).intValue());
    assertEquals(36, r.get(2).intValue());
    assertEquals(48, r.get(3).intValue());
    assertEquals(59, r.get(4).intValue());
    assertEquals(71, r.get(5).intValue());
    assertEquals(72, r.get(6).intValue());
    assertNull(mWorld.shortestPath(66, 74, MY_FILTER));
    assertNull(mWorld.shortestPath(66, 15, MY_FILTER));
    assertNull(mWorld.shortestPath(66, 26, MY_FILTER));
    assertNull(mWorld.shortestPath(66, 73, MY_FILTER));
  }

  public void testNastyTempWorkspace() {
    assertEquals(2, ((int[]) TestUtils.getField("TEMP", FlatWorld.class)).length);
  }

  private static final boolean[] MY2_MAP = {
    false, false, true,  false, false, true,  false, true,  false, false, true,
    true,  false, false, true,  false, false, true,  false, true,  false, false,
    false, true,  false, false, true,  false, false, false, false, true,  false,
    false, false, true,  false, false, true,  false, true,  false, false, true,
    false, false, true,  false, false, true,  false, true,  false, false, true,
    true,  false, false, true,  false, false, true,  false, true,  false, false,
    false, true,  false, false, true,  false, false, false, false, true,  false,
  };

  static final CellFilter<Integer> MY2_FILTER = x -> x == null || MY2_MAP[x];

  public void testBuggyPath() {
    for (int k = 0; k < mWorld.size(); ++k) {
      mWorld.setCell(k, k);
    }
    final List<Integer> r = mWorld.shortestPath(48, 42, MY2_FILTER);
    final List<Integer> s = mWorld.shortestPath(42, 48, MY2_FILTER);
    assertEquals(r.size(), s.size());
  }
}
