package chaos.board;

import java.util.Random;

import chaos.Chaos;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.FrequencyTable;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.inanimate.MagicGlass;
import chaos.common.monster.Bolter;
import chaos.common.monster.Leopard;
import chaos.common.monster.Tiger;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class LineOfSightTest extends TestCase {

  /** Bits in the cell width number. */
  private static final int CELL_WIDTH_BITS = 3;
  /** Number of pixels across a cell. Must be a power of 2. */
  private static final int CELL_WIDTH = 1 << CELL_WIDTH_BITS;
  /** Mask to the cell width. */
  private static final int CELL_WIDTH_MASK = CELL_WIDTH - 1;
  /** Number of bits in half the cell width. */
  private static final int HALF_CELL_WIDTH_BITS = CELL_WIDTH_BITS - 1;
  /** Half the cell width. */
  private static final int HALF_CELL_WIDTH = 1 << HALF_CELL_WIDTH_BITS;
  /** Size of fixed-point arithmetic. */
  private static final int FIXED_POINTS_BITS = 16;
  /** Half in above fixed-point arithmetic. */
  private static final int HALF = 1 << (FIXED_POINTS_BITS - 1);
  /** Special axis shift. */
  private static final int SPECIAL_SHIFT = FIXED_POINTS_BITS + CELL_WIDTH_BITS;
  /** Special mask shift. */
  private static final int SPECIAL_MASK_SHIFT = FIXED_POINTS_BITS - CELL_WIDTH_BITS;

  /**
   * Get the line of sight mask for a cell.
   * @param cell cell to get mask for
   * @return the 8&times;8 packed mask
   */
  private long getLOSMask(final World w, final int cell) {
    final Actor a = w.actor(cell);
    return a == null ? 0L : a.getState() == State.DEAD ? 0L : a.getLosMask();
  }

  /**
   * A slower but definitely correct implementation, used to check
   * correctness against the optimized version.
   * @param source source cell
   * @param target target cell
   * @return true if line of sight is possible, false otherwise
   */
  private boolean isLOSLocal(final World world, final int source, final int target) {
    // quick exit for case of line-of-sight to self
    if (source == target) {
      return true;
    }

    // get pixel coordinates of lower-left of each cell
    final int[] sxy = new int[2];
    world.getCellCoordinates(source, sxy);
    final int osx = sxy[0] << CELL_WIDTH_BITS;
    final int osy = sxy[1] << CELL_WIDTH_BITS;
    final int[] txy = new int[2];
    world.getCellCoordinates(target, txy);
    int tx = txy[0] << CELL_WIDTH_BITS;
    int ty = txy[1] << CELL_WIDTH_BITS;

    // adjust (sx,sy) to centre pixel of source closest to (tx,ty)
    // this is critical for correct function of what follows!
    int sx = osx + (osx >= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    int sy = osy + (osy >= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    // adjust (tx,ty) to centre pixel of target closest to (sx,sy)
    // less critical, but needed for technical correctness of slope
    tx += osx <= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
    ty += osy <= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;

    // dy/dx is the slope of the line
    final int dx = tx - sx;
    final int dy = ty - sy;


    int cell;
    // choose loop based on axis with greatest delta, tests abs(dx) >= abs(dy)
    if ((dx >= 0 ? dx : -dx) >= (dy >= 0 ? dy : -dy)) {
      // x-axis is the longer
      final int slope = (dy << FIXED_POINTS_BITS) / dx;
      if (dx >= 0) {
        // rightwards
        sy = (sy << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sx += HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * world.width() + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(world, cell);
          final int csx = sx, csy = sy;
          int xbit = 64 - (sx & CELL_WIDTH_MASK);
          do {
            if ((mask & (1L << (xbit-- - ((sy >>> SPECIAL_MASK_SHIFT) & 0x38)))) != 0L) {
              return false;
            }
          } while (((csx ^ ++sx) | ((csy ^ (sy += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
        }
      } else {
        // leftwards
        sy = (sy << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sx -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * world.width() + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(world, cell);
          final int csx = sx, csy = sy;
          int xbit = 64 - (sx & CELL_WIDTH_MASK);
          do {
            if ((mask & (1L << (xbit++ - ((sy >>> SPECIAL_MASK_SHIFT) & 0x38)))) != 0L) {
              return false;
            }
          } while (((csx ^ --sx) | ((csy ^ (sy -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
        }
      }
    } else {
      // y-axis is the longer
      final int slope = (dx << FIXED_POINTS_BITS) / dy;
      if (dy >= 0) {
        // downwards
        sx = (sx << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sy += HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * world.width() + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(world, cell);
          final int csx = sx, csy = sy;
          int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
          do {
            if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
              return false;
            }
            ybit -= CELL_WIDTH;
          } while (((csy ^ ++sy) | ((csx ^ (sx += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
        }
      } else {
        // upwards
        sx = (sx << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sy -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * world.width() + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(world, cell);
          final int csx = sx, csy = sy;
          int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
          do {
            if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
              return false;
            }
            ybit += CELL_WIDTH;
          } while (((csy ^ --sy) | ((csx ^ (sx -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
        }
      }
    }
    return true;
  }

  public void testNull() {
    try {
      new LineOfSight(null);
      fail("Took null");
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testEmpty() {
    final World w = new World(20, 20);
    final LineOfSight los = new LineOfSight(w);
    for (int i = 0; i < w.size(); ++i) {
      for (int j = 0; j < w.size(); ++j) {
        assertTrue(los.isLOS(i, j));
      }
    }
  }

  public void testBlack() {
    final World w = new World(20, 20);
    for (int i = 0; i < w.size(); ++i) {
      w.getCell(i).push(new Bolter());
    }
    final LineOfSight los = new LineOfSight(w);
    for (int i = 0; i < w.size(); ++i) {
      for (int j = 0; j < w.size(); ++j) {
        final int[] ixy = new int[2];
        w.getCellCoordinates(i, ixy);
        final int[] jxy = new int[2];
        w.getCellCoordinates(j, jxy);
        if (i == j || (Math.abs(ixy[0] - jxy[0]) <= 1 && Math.abs(ixy[1] - jxy[1]) <= 1)) {
          assertTrue(los.isLOS(i, j));
        } else {
          assertTrue(!los.isLOS(i, j));
        }
      }
    }
  }

  public void testOneBolter() {
    final World w = new World(5, 5);
    final LineOfSight los = new LineOfSight(w);
    w.getCell(12).push(new Bolter());
    assertTrue(los.isLOS(12, 0));
    assertTrue(los.isLOS(12, 23));
    assertTrue(los.isLOS(0, 12));
    assertTrue(los.isLOS(5, 9));
    assertTrue(los.isLOS(15, 19));
    assertTrue(los.isLOS(1, 21));
    assertTrue(los.isLOS(3, 23));
    assertTrue(los.isLOS(20, 14));
    assertTrue(los.isLOS(24, 10));
    assertTrue(los.isLOS(16, 12));
    assertTrue(los.isLOS(22, 11));
    assertTrue(!los.isLOS(2, 17));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(10, 14));
    assertTrue(!los.isLOS(6, 18));
    assertTrue(!los.isLOS(0, 24));
    assertTrue(!los.isLOS(11, 8));
    assertTrue(!los.isLOS(7, 18));
  }

  public void testOneLeopard() {
    final World w = new World(5, 5);
    final LineOfSight los = new LineOfSight(w);
    w.getCell(12).push(new Leopard());
    assertTrue(los.isLOS(12, 0));
    assertTrue(los.isLOS(12, 23));
    assertTrue(los.isLOS(0, 12));
    assertTrue(los.isLOS(5, 9));
    assertTrue(los.isLOS(15, 19));
    assertTrue(los.isLOS(1, 21));
    assertTrue(los.isLOS(3, 23));
    assertTrue(los.isLOS(20, 14));
    assertTrue(los.isLOS(24, 10));
    assertTrue(los.isLOS(16, 12));
    assertTrue(los.isLOS(22, 11));
    assertTrue(!los.isLOS(2, 17));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(10, 14));
    assertTrue(!los.isLOS(6, 18));
    assertTrue(!los.isLOS(0, 24));
    assertTrue(los.isLOS(11, 8));
    assertTrue(los.isLOS(7, 18));
  }

  public void testOneWoodElf() {
    final World w = new World(5, 5);
    final LineOfSight los = new LineOfSight(w);
    w.getCell(12).push(new WoodElf());
    assertTrue(los.isLOS(12, 0));
    assertTrue(los.isLOS(12, 23));
    assertTrue(los.isLOS(0, 12));
    assertTrue(los.isLOS(5, 9));
    assertTrue(los.isLOS(15, 19));
    assertTrue(los.isLOS(1, 21));
    assertTrue(los.isLOS(3, 23));
    assertTrue(los.isLOS(20, 14));
    assertTrue(los.isLOS(24, 10));
    assertTrue(los.isLOS(16, 12));
    assertTrue(los.isLOS(22, 11));
    assertTrue(!los.isLOS(2, 17));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(2, 22));
    assertTrue(!los.isLOS(10, 14));
    assertTrue(!los.isLOS(6, 18));
    assertTrue(!los.isLOS(0, 24));
    assertTrue(los.isLOS(11, 8));
    assertTrue(los.isLOS(7, 18));
  }

  private String reportCell(final World w, final int c) {
    final StringBuilder b = new StringBuilder();
    final int[] xy = new int[2];
    w.getCellCoordinates(c, xy);
    b.append('(').append(xy[0]).append(',').append(xy[1]).append(')');
    return b.toString();
  }

  /**
   * Compares implementations.
   * @throws Exception if an error occurs
   */
  public void testRandomCompare() throws Exception {
    final Random r = new Random();
    for (int density = 2; density < 12; ++density) {
      final World w = new World(25, 25);
      final int size = w.size();
      for (int i = 0; i < size; ++i) {
        if (r.nextInt(density) == 0) {
          Castable c;
          while (!((c = FrequencyTable.DEFAULT.getUniformRandom().getDeclaredConstructor().newInstance()) instanceof Actor) || c instanceof MagicGlass) {
            // do nothing
          }
          w.getCell(i).push((Actor) c);
        }
      }
      final LineOfSight los = new LineOfSight(w);
      for (int i = 0; i < 1000; ++i) {
        final int s = r.nextInt(size);
        final int t = r.nextInt(size);
        if (isLOSLocal(w, s, t) != los.isLOS(s, t)) {
          System.out.println("local reports: " + isLOSLocal(w, s, t));
          System.out.println("game reports: " + los.isLOS(s, t));
          for (int y = 0; y < 25; ++y) {
            System.out.print(y + "|");
            for (int x = 0; x < 25; ++x) {
              final Actor a = w.getCell(x, y).peek();
              final String n = a == null ? "....." : a.getName() + "         ";
              System.out.print(n.substring(0, 5) + "|");
            }
            System.out.println();
          }
          fail(reportCell(w, s) + "->" + reportCell(w, t));
        }
      }
    }
  }

  /**
   * Tests idempotence, a can see b iff b can see a.
   * This has found a few bugs in the past.
   * @throws Exception if an error occurs
   */
  public void testRandomIdempotence() throws Exception {
    final Random r = new Random();
    for (int density = 2; density < 12; ++density) {
      final World w = new World(25, 25);
      final int size = w.size();
      for (int i = 0; i < size; ++i) {
        if (r.nextInt(density) == 0) {
          Castable c;
          while (!((c = FrequencyTable.DEFAULT.getUniformRandom().getDeclaredConstructor().newInstance()) instanceof Actor)) {
            // do nothing
          }
          w.getCell(i).push((Actor) c);
        }
      }
      final LineOfSight los = new LineOfSight(w);
      for (int i = 0; i < 1000; ++i) {
        final int s = r.nextInt(size);
        final int t = r.nextInt(size);
        assertEquals(reportCell(w, s) + "->" + reportCell(w, t), los.isLOS(s, t), los.isLOS(t, s));
      }
    }
  }

  public void testChequered() {
    final World w = new World(5, 5);
    for (int i = 0; i < w.size(); i += 2) {
      w.getCell(i).push(new Bolter());
    }
    final LineOfSight los = new LineOfSight(w);
    assertTrue(los.isLOS(0, 2));
    assertTrue(!los.isLOS(0, 4));
    assertTrue(los.isLOS(2, 4));
    assertTrue(los.isLOS(0, 10));
    assertTrue(!los.isLOS(1, 3));
    assertTrue(!los.isLOS(1, 4));
    assertTrue(los.isLOS(3, 4));
    assertTrue(!los.isLOS(1, 10));
    assertTrue(los.isLOS(5, 1));
  }

  public void testGlassFunctions() {
    final World w = Chaos.getChaos().getWorld();
    try {
      final Wizard1 wiz1 = new Wizard1();
      w.getWizardManager().setWizard(1, wiz1);
      final Wizard1 wiz2 = new Wizard1();
      w.getWizardManager().setWizard(2, wiz2);
      final RedDragon rd = new RedDragon();
      rd.setOwner(1);
      w.getCell(0).push(rd);
      final MagicGlass mg = new MagicGlass();
      mg.setOwner(1);
      w.getCell(1).push(mg);
      final Tiger t = new Tiger();
      t.setOwner(3);
      w.getCell(2).push(t);
      final MoveMaster mm = new MoveMaster(w);
      assertTrue(mm.isShootable(w.getWizardManager().getWizard(rd), 0, 2));
      rd.setOwner(2);
      assertFalse(mm.isShootable(w.getWizardManager().getWizard(rd), 0, 2));
    } finally {
      w.getCell(2).pop();
      w.getCell(1).pop();
      w.getCell(0).pop();
    }
  }
}
