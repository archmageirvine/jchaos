package chaos.board;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Growth;
import chaos.common.MaterialGrowth;
import chaos.common.PowerUps;
import chaos.common.growth.Earthquake;
import chaos.common.growth.Fire;
import chaos.common.growth.Flood;
import chaos.common.growth.GooeyBlob;
import chaos.common.growth.GreenOoze;
import chaos.common.growth.OrangeJelly;
import chaos.common.growth.VioletFungi;
import junit.framework.TestCase;

/**
 * Tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class GrowerTest extends TestCase {

  private static int countGrowthCells(final World w) {
    int c = 0;
    for (int i = 0; i < w.size(); ++i) {
      if (w.actor(i) != null) {
        ++c;
      }
    }
    return c;
  }

  public void testFillsSpace(final Actor c) {
    final World w = new World(3, 3);
    w.getCell(4).push(c);
    final Grower g = new Grower(w);
    int i;
    for (i = 0; i < 2000; ++i) {
      final int n = countGrowthCells(w);
      if (n == w.size()) {
        break;
      } else if (n == 0) {
        // i.e. growth died out
        assertTrue(!(c instanceof VioletFungi));
        assertTrue(!(c instanceof Earthquake));
        // retry
        i = 0;
        w.getCell(4).push(c);
      }
      g.grow();
    }
    assertTrue("count expired: " + countGrowthCells(w), i < 2000);
    final Class<?> cl = c.getClass();
    for (i = 0; i < w.size(); ++i) {
      assertTrue(w.actor(i).getClass().toString(), cl.isInstance(w.actor(i)));
    }
  }

  public void testJelly() {
    testFillsSpace(new OrangeJelly());
  }

  public void testBlob() {
    testFillsSpace(new GooeyBlob());
  }

  public void testOoze() {
    testFillsSpace(new GreenOoze());
  }

  public void testFlood() {
    testFillsSpace(new Flood());
  }

  public void testFire() {
    testFillsSpace(new Fire());
  }

  public void testFungi() {
    testFillsSpace(new VioletFungi());
  }

  public void testEarthquake() {
    testFillsSpace(new Earthquake());
  }

  public void testEQDoesNotDie() {
    final World w = new World(3, 3);
    final Earthquake eq = new Earthquake();
    w.getCell(4).push(eq);
    final Grower g = new Grower(w);
    for (int i = 0; i < 2000; ++i) {
      g.grow();
      assertEquals(eq, w.actor(4));
    }
  }

  public void testFungiDoesNotDie() {
    final World w = new World(3, 3);
    final VioletFungi eq = new VioletFungi();
    w.getCell(4).push(eq);
    final Grower g = new Grower(w);
    for (int i = 0; i < 2000; ++i) {
      g.grow();
      assertEquals(eq, w.actor(4));
    }
  }

  static class MyGrowth extends MaterialGrowth {
    private boolean mSeen = false;

    @Override
    public int growthRate() {
      return 100;
    }

    @Override
    public int getGrowthType() {
      return Growth.GROW_OVER;
    }

    @Override
    public void grow(final int a, final World world) {
      mSeen = true;
    }

    @Override
    public int getCastRange() {
      return 0;
    }

    @Override
    public boolean canGrowOver(final Cell c) {
      return true;
    }

    public boolean isSeen() {
      return mSeen;
    }

    public void setSeen(final boolean seen) {
      mSeen = seen;
    }
  }

  public void testAlways() {
    final MyGrowth g = new MyGrowth();
    for (int i = 0; i < 10000; ++i) {
      final World w = new World(1, 1);
      w.getCell(0).push(g);
      new Grower(w).grow();
      assertTrue(g.isSeen());
      g.setSeen(false);
    }
  }

  public void testCons() {
    try {
      new Grower(null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  private static class MyFungi extends VioletFungi {
    {
      setDefault(Attribute.LIFE, 100); // Fire unlikely to burn
    }

    @Override
    public int growthRate() {
      return 0;
    }
  }

  // The test needs to allow for the possibility that the growth disappeared
  private boolean poisoned(final boolean state, final Cell c) {
    final Actor a = c.peek();
    if (a instanceof Growth) {
      return a.is(PowerUps.NO_GROW);
    } else {
      return state;
    }
  }

  public void testSpreadOfPoison() {
    final World w = new World(1, 7);
    w.getCell(0).push(new MyFungi());
    w.getCell(1).push(new Fire());
    w.getCell(2).push(new MyFungi());
    final MyFungi root = new MyFungi();
    root.set(PowerUps.NO_GROW, 1);
    w.getCell(3).push(root);
    w.getCell(4).push(new MyFungi());
    w.getCell(5).push(new MyFungi());
    w.getCell(6).push(new MyFungi());
    final Grower g = new Grower(w);
    g.grow();
    assertFalse(poisoned(false, w.getCell(0)));
    assertFalse(poisoned(false, w.getCell(1)));
    assertTrue(poisoned(true, w.getCell(2)));
    assertTrue(poisoned(true, w.getCell(3)));
    assertTrue(poisoned(true, w.getCell(4)));
    assertFalse(poisoned(false, w.getCell(5)));
    assertFalse(poisoned(false, w.getCell(6)));
    g.grow();
    assertFalse(poisoned(false, w.getCell(0)));
    assertFalse(poisoned(false, w.getCell(1)));
    assertTrue(poisoned(true, w.getCell(2)));
    assertTrue(poisoned(true, w.getCell(3)));
    assertTrue(poisoned(true, w.getCell(4)));
    assertTrue(poisoned(true, w.getCell(5)));
    assertFalse(poisoned(false, w.getCell(6)));
    g.grow();
    assertFalse(poisoned(false, w.getCell(0)));
    assertFalse(poisoned(false, w.getCell(1)));
    assertTrue(poisoned(true, w.getCell(2)));
    assertTrue(poisoned(true, w.getCell(3)));
    assertTrue(poisoned(true, w.getCell(4)));
    assertTrue(poisoned(true, w.getCell(5)));
    assertTrue(poisoned(true, w.getCell(6)));
  }
}
