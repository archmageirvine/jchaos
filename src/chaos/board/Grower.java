package chaos.board;

import java.util.HashSet;

import chaos.common.Actor;
import chaos.common.Growth;
import chaos.common.PowerUps;
import chaos.common.UndyingGrowth;
import chaos.util.Random;

/**
 * Performs the growth phase.
 * @author Sean A. Irvine
 */
public class Grower {

  private final World mWorld;

  /**
   * Construct a new grower for the specified world.
   * @param world the world
   * @exception NullPointerException if <code>world</code> is null.
   */
  public Grower(final World world) {
    if (world == null) {
      throw new NullPointerException();
    }
    mWorld = world;
  }

  /**
   * Handle growing of growths.
   */
  public void grow() {
    // Mark all existing growths as moved, this is done to make sure we
    // don't grow newly inserted growths.
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a instanceof Growth) {
        a.setMoved(true);
      }
    }

    // Performing growing and death.
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a instanceof Growth && a.isMoved()) {
        final Growth g = (Growth) a;
        final int rate = g.growthRate();
        if (Random.nextInt(100) < rate) {
          g.grow(i, mWorld);
        } else if (Random.nextInt(200) < rate && !(a instanceof UndyingGrowth)) {
          // random condition is equivalent to Random.nextInt(100) < rate/2
          mWorld.getCell(i).reinstate();
        }
      }
    }

    // Handle spread of poisoning
    final HashSet<Cell> poisoned = new HashSet<>();
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a instanceof Growth && a.is(PowerUps.NO_GROW)) {
        for (final Cell c : mWorld.getCells(i, 1, 1, false)) {
          final Actor ac = c.peek();
          if (ac instanceof Growth && ((Growth) ac).getGrowthType() == Growth.GROW_OVER) {
            poisoned.add(c);
          }
        }
      }
    }
    for (final Cell c : poisoned) {
      c.peek().set(PowerUps.NO_GROW, 1);
    }
  }
}
