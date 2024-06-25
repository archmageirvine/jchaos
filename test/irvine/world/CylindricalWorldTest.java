package irvine.world;

import java.util.List;
import java.util.Set;

import irvine.TestUtils;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CylindricalWorldTest extends AbstractWorldTest {


  @Override
  public World<Integer> getWorld() {
    return new CylindricalWorld<>(11, 7);
  }

  public void testIsEdge() {
    for (int k = 0; k < 11; ++k) {
      assertTrue(mWorld.isEdge(k));
      assertTrue(mWorld.isEdge(66 + k));
    }
    for (int x = 0; x < 11; ++x) {
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
    assertEquals(10, mWorld.getCellNumber(-1, 0));
    assertEquals(9, mWorld.getCellNumber(-2, 0));
    assertEquals(-1, mWorld.getCellNumber(0, -1));
    assertEquals(-1, mWorld.getCellNumber(-1, -1));
    assertEquals(-1, mWorld.getCellNumber(0, 7));
    assertEquals(0, mWorld.getCellNumber(11, 0));
    assertEquals(2, mWorld.getCellNumber(13, 0));
    assertEquals(-1, mWorld.getCellNumber(10, 7));
    assertEquals(76, mWorld.getCellNumber(10, 6));
    assertEquals(10, mWorld.getCellNumber(10, 0));
    assertEquals(66, mWorld.getCellNumber(0, 6));
    assertEquals(37, mWorld.getCellNumber(4, 3));
    assertEquals(47, mWorld.getCellNumber(3, 4));
    assertEquals(47, mWorld.getCellNumber(14, 4));
  }

  public void testGetSquaredDistance() {
    assertEquals(-1, mWorld.getSquaredDistance(-1, 0));
    assertEquals(-1, mWorld.getSquaredDistance(-1, -1));
    assertEquals(-1, mWorld.getSquaredDistance(0, -1));
    assertEquals(-1, mWorld.getSquaredDistance(77, 0));
    assertEquals(-1, mWorld.getSquaredDistance(77, 50));
    assertEquals(-1, mWorld.getSquaredDistance(10, 77));
    assertEquals(1, mWorld.getSquaredDistance(0, 1));
    assertEquals(1, mWorld.getSquaredDistance(1, 0));
    assertEquals(4, mWorld.getSquaredDistance(33, 55));
    assertEquals(4, mWorld.getSquaredDistance(34, 56));
    assertEquals(9, mWorld.getSquaredDistance(33, 41));
    assertEquals(9, mWorld.getSquaredDistance(41, 33));
    assertEquals(37, mWorld.getSquaredDistance(0, 76));
    assertEquals(61, mWorld.getSquaredDistance(2, 73));
    for (int a = 0; a < mWorld.size(); ++a) {
      assertEquals(0, mWorld.getSquaredDistance(a, a));
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
    s = mWorld.getCells(43, 1, 1, null);
    assertEquals(8, s.size());
    assertTrue(s.contains(42));
    assertTrue(s.contains(33));
    assertTrue(s.contains(31));
    assertTrue(s.contains(32));
    assertTrue(s.contains(22));
    assertTrue(s.contains(44));
    assertTrue(s.contains(53));
    assertTrue(s.contains(54));
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
    s = mWorld.getCells(43, 1, 1, AbstractWorldTest.FORBID_PRIME);
    assertEquals(6, s.size());
    assertTrue(s.contains(42));
    assertTrue(s.contains(33));
    assertTrue(s.contains(32));
    assertTrue(s.contains(22));
    assertTrue(s.contains(44));
    assertTrue(s.contains(54));
  }

  @Override
  public void testShortestPath() {
    super.testShortestPath();
    List<Integer> r = mWorld.shortestPath(66, 72, MY_FILTER);
    assertEquals(7, r.size());
    assertEquals(56, r.get(0).intValue());
    assertEquals(46, r.get(1).intValue());
    assertEquals(36, r.get(2).intValue());
    assertEquals(48, r.get(3).intValue());
    assertEquals(59, r.get(4).intValue());
    assertEquals(71, r.get(5).intValue());
    assertEquals(72, r.get(6).intValue());
    r = mWorld.shortestPath(66, 74, MY_FILTER);
    assertEquals(3, r.size());
    assertEquals(76, r.get(0).intValue());
    assertEquals(75, r.get(1).intValue());
    assertEquals(74, r.get(2).intValue());
    assertNull(mWorld.shortestPath(66, 15, MY_FILTER));
    assertNull(mWorld.shortestPath(66, 26, MY_FILTER));
    assertNull(mWorld.shortestPath(66, 73, MY_FILTER));
  }

  public void testNastyTempWorkspace() {
    assertEquals(2, ((int[]) TestUtils.getField("TEMP", FlatWorld.class)).length);
  }
}
