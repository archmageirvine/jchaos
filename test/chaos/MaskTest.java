package chaos;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.HashSet;
import java.util.Locale;

import chaos.common.AbstractGenerator;
import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.AppleWood;
import chaos.common.inanimate.Rock;
import chaos.common.monster.NamedDeity;
import chaos.common.monster.NamedSnake;
import chaos.common.wizard.Wizard;
import chaos.graphics.TileManager;
import irvine.util.io.IOUtils;
import junit.framework.TestCase;

/**
 * Tests masks make sense.
 *
 * @author Sean A. Irvine
 */
public class MaskTest extends TestCase {

  private static long computeMask(final Actor a) {
    final TileManager tm = Chaos.getChaos().getTileManager();
    final int w = tm.getWidth();
    if (w != 16) {
      throw new UnsupportedOperationException();
    }
    final HashSet<BufferedImage> tiles = new HashSet<>();
    // make sure we get a good lot of tiles
    for (int k = 0; k < 50; ++k) {
      tiles.add(tm.getTile(a, 0, 0, 0));
    }
    final int[][] av = new int[w][w];
    for (final BufferedImage i : tiles) {
      if (i != null) {
        for (int y = 0; y < i.getHeight(); ++y) {
          for (int x = 0; x < i.getWidth(); ++x) {
            final int p = i.getRGB(x, y);
            if ((p & 0xFFFFFF) != 0) {
              ++av[y][x];
            }
          }
        }
      } else {
        System.err.println("Null image for " + a.getName());
      }
    }
    // down scale
    final double scale = 1.0 / (4 * tiles.size());
    long mask = 0;
    for (int y = 0, yy = 0; y < 8; ++y, yy += 2) {
      for (int x = 0, xx = 0; x < 8; ++x, xx += 2) {
        mask <<= 1;
        final int v = av[yy][xx] + av[yy][xx + 1] + av[yy + 1][xx] + av[yy + 1][xx + 1];
        if (v * scale > 0.1) {
          mask |= 1;
        }
      }
    }
    return mask;
  }

  private static String format(final long m) {
    final String t = "0000000000000000" + Long.toHexString(m).toUpperCase(Locale.getDefault());
    return "0x" + t.substring(t.length() - 16) + "L";
  }

  private Castable getCastable(final String clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    // For some named castables a specific instance to match the mask since not all of them do
    if (clazz.contains("NamedDeity")) {
      return new NamedDeity("Zeus");
    } else if (clazz.contains("NamedSnake")) {
      return new NamedSnake("Apep");
    } else {
      return (Castable) Class.forName(clazz).newInstance();
    }
  }

  /**
   * Compute best mask for each actor and check it matches the one in the
   * Castable.
   *
   * @exception Exception if an error occurs
   */
  public void testMasks() throws Exception {
    // Find all actors
    final StringBuilder sb = new StringBuilder();
    final HashSet<Actor> set = new HashSet<>();
    try (final BufferedReader f = IOUtils.reader("chaos/resources/frequency.txt")) {
      String line;
      while ((line = f.readLine()) != null) {
        if (!line.isEmpty() && line.charAt(0) != '#') {
          final int t = line.lastIndexOf('.');
          final int sp = line.indexOf(' ');
          if (t != -1 && sp != -1 && sp > t) {
            // first check we can instantiate the class from frequency.txt
            final String clazz = line.substring(0, sp);
            try {
              final Castable c = getCastable(clazz);
              if (c instanceof Actor) {
                final Actor a = (Actor) c;
                a.setState(State.ACTIVE); // for wizards
                set.add(a);
              }
            } catch (final ClassNotFoundException e) {
              sb.append(clazz).append(" is not found\n");
            }
          }
        }
      }
    }
    if (sb.length() > 0) {
      fail(sb.toString());
    }

    // Check masks
    for (final Actor a : set) {
      final long mask = a.getLosMask();
      if (mask != 0) {
        if (a instanceof Wizard) {
          assertEquals(0x7E7E7E3C3C7E00L, mask);
        } else if (a instanceof AbstractWall || a instanceof AbstractGenerator) {
          assertEquals(a.getName(), ~0L, mask);
        } else if (a instanceof AppleWood || a instanceof Rock) {
          // skip it
        } else {
          final long computedMask = computeMask(a);
          if (computedMask != mask) {
            sb.append(a.getName()).append(' ').append(format(mask)).append(' ').append(format(computedMask)).append('\n');
          }
        }
      }
    }
    if (sb.length() > 0) {
      fail(sb.toString());
    }
  }

}
