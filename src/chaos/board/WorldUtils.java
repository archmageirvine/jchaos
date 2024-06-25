package chaos.board;

import java.util.Random;

import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.Generator;
import chaos.common.wizard.Wizard;
import irvine.util.MaximumSeparation;

/**
 * Various utility functions operating on the whole world.
 *
 * @author Sean A. Irvine
 */
public final class WorldUtils {

  private WorldUtils() { }

  private static final Random RANDOM = new Random();

  /**
   * Insert generators into the world. Will be sensibly limit the request if
   * the world is too small to handle requested number.
   *
   * @param world the world
   * @param generators the generators
   */
  public static void insertGenerators(final World world, final int generators) {
    int lim = 0;
    for (int k = 0; k < world.size(); ++k) {
      if (world.actor(k) == null) {
        ++lim;
      }
    }
    lim = Math.min(lim, generators);
    for (int k = 0; k < lim; ++k) {
      int rc;
      while (world.actor(rc = RANDOM.nextInt(world.size())) != null) {
        // do nothing
      }
      final Generator g = new Generator();
      g.setOwner(Actor.OWNER_INDEPENDENT);
      world.getCell(rc).push(g);
    }
  }

  private static int alive(final Wizard[] wiz) {
    int a = 0;
    for (final Wizard w : wiz) {
      if (w != null && w.getState() == State.ACTIVE) {
        ++a;
      }
    }
    return a;
  }

  /**
   * Insert all the active wizards from the array into the world.
   *
   * @param world world
   * @param wiz wizards
   * @param scenario true if this is a scenario
   */
  public static void insertWizards(final World world, final Wizard[] wiz, final boolean scenario) {
    final int alive = alive(wiz);
    final int[][] pos;
    final int add;
    if (scenario) {
      pos = MaximumSeparation.separate(world.width(), 1, alive, 10);
      add = world.height() / 2;
    } else {
      pos = MaximumSeparation.separate(world.width(), world.height(), alive, 10);
      add = 0;
    }
    int k = 0;
    for (final Wizard w : wiz) {
      if (w != null && w.getState() == State.ACTIVE) {
        world.getCell(pos[0][k], pos[1][k] + add).push(w);
        ++k;
      }
    }
  }

}
