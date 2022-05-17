package irvine.util;

import java.util.HashSet;

import irvine.StandardIoTestCase;
import irvine.TestUtils;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class MaximumSeparationTest extends StandardIoTestCase {

  public void test() {
    try {
      MaximumSeparation.separate(0, 1, 1, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      MaximumSeparation.separate(1, 0, 1, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      MaximumSeparation.separate(1, 1, -1, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      MaximumSeparation.separate(1, 1, 1, 0);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void testLinear() {
    for (int w = 1; w < 10; ++w) {
      final int[][] res = MaximumSeparation.separate(1, w, w, 1);
      assertEquals(w, res[0].length);
      assertEquals(w, res[1].length);
      assertEquals(w * (w - 1) / 2, res[2].length);
      final boolean[] seen = new boolean[w];
      for (int i = 0; i < w; ++i) {
        assertEquals(0, res[0][i]);
        seen[res[1][i]] = true;
      }
      for (int i = 0; i < w; ++i) {
        assertTrue(seen[i]);
      }
    }
    for (int w = 1; w < 10; ++w) {
      final int[][] res = MaximumSeparation.separate(w, 1, w, 1);
      assertEquals(w, res[0].length);
      assertEquals(w, res[1].length);
      assertEquals(w * (w - 1) / 2, res[2].length);
      final boolean[] seen = new boolean[w];
      for (int i = 0; i < w; ++i) {
        assertEquals(0, res[1][i]);
        seen[res[0][i]] = true;
      }
      for (int i = 0; i < w; ++i) {
        assertTrue(seen[i]);
      }
    }
  }

  private static final String LS = System.lineSeparator();

  public void testViaMain1() {
    MaximumSeparation.main("5", "5", "4", "20");
    final String s = getOut();
    assertEquals("Score: 16 16 16 16 32 32" + LS
      + "+-----+" + LS
      + "|*   *|" + LS
      + "|     |" + LS
      + "|     |" + LS
      + "|     |" + LS
      + "|*   *|" + LS
      + "+-----+" + LS, s);
  }

  public void testViaMain2() {
    MaximumSeparation.main("-t", "5", "5", "4", "20");
    final String s = getOut();
    assertEquals("%Score: 128" + LS
      + "\\def\\pp{\\phantom{$\\bullet$}}" + LS
      + "\\begin{tabular}{|c|c|c|c|c|}" + LS
      + "\\hline" + LS
      + "$\\bullet$&\\pp&\\pp&\\pp&$\\bullet$\\\\\\hline" + LS
      + "\\pp&\\pp&\\pp&\\pp&\\pp\\\\\\hline" + LS
      + "\\pp&\\pp&\\pp&\\pp&\\pp\\\\\\hline" + LS
      + "\\pp&\\pp&\\pp&\\pp&\\pp\\\\\\hline" + LS
      + "$\\bullet$&\\pp&\\pp&\\pp&$\\bullet$\\\\\\hline" + LS
      + "\\end{tabular}" + LS, s);
  }

  public void testViaMain3() {
    MaximumSeparation.main("-p", "8", "8", "4", "20");
    final String s = getOut();
    assertEquals("P1" + LS
      + "#Score: 392" + LS
      + "8 8" + LS
      + " 1 0 0 0 0 0 0 1" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 0 0 0 0 0 0 0 0" + LS
      + " 1 0 0 0 0 0 0 1" + LS, s);
  }

  public void testNasty() {
    final HashSet<String> h = new HashSet<>();
    Object obj = TestUtils.getField("DX", MaximumSeparation.class);
    assertTrue(obj instanceof int[]);
    final int[] dx = (int[]) obj;
    assertEquals(8, dx.length);
    int s = 0;
    for (int i = 0; i < 8; ++i) {
      s += dx[i];
    }
    obj = TestUtils.getField("DY", MaximumSeparation.class);
    assertTrue(obj instanceof int[]);
    final int[] dy = (int[]) obj;
    assertEquals(8, dy.length);
    for (int i = 0; i < 8; ++i) {
      s += dy[i];
      h.add(dx[i] + "," + dy[i]);
    }
    assertEquals(0, s);
    assertEquals(8, h.size());
    assertTrue(h.contains("0,1"));
    assertTrue(h.contains("0,-1"));
    assertTrue(h.contains("1,1"));
    assertTrue(h.contains("1,-1"));
    assertTrue(h.contains("-1,1"));
    assertTrue(h.contains("-1,-1"));
    assertTrue(h.contains("1,0"));
    assertTrue(h.contains("-1,0"));
  }

  public void testScore() {
    final int[][] res = MaximumSeparation.separate(15, 10, 5, 1);
    assertEquals(3, res.length);
    assertTrue(res[0].length == res[1].length);
    final int[] d = res[2];
    assertTrue("d[0]=" + d[0], d[0] > 30);
    for (int i = 0; i < d.length - 1; ++i) {
      assertTrue(d[i] <= d[i + 1]);
    }
  }

  public void testScore2() {
    final int[][] res = MaximumSeparation.separate(15, 10, 5, 10);
    assertEquals(3, res.length);
    assertTrue(res[0].length == res[1].length);
    final int[] d = res[2];
    assertTrue("d[0]=" + d[0], d[0] > 50);
    for (int i = 0; i < d.length - 1; ++i) {
      assertTrue(d[i] <= d[i + 1]);
    }
  }

  public void testItCountFix() {
    final long t = System.currentTimeMillis();
    MaximumSeparation.separate(15, 10, 2, 1000000);
    assertTrue(System.currentTimeMillis() - t < 5000);
  }
}
