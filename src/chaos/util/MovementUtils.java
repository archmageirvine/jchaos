package chaos.util;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.Conveyance;
import chaos.common.Monster;
import chaos.common.monster.Solar;
import chaos.common.wizard.Wizard;

/**
 * Movement utility functions.
 * @author Sean A. Irvine
 */
public final class MovementUtils {

  private MovementUtils() {
  }

  private static void setOrClearMovement(final Actor a, final boolean setMovement) {
    if (a != null) {
      a.setMoved(setMovement);
      if (a instanceof Monster) {
        ((Monster) a).setShotsMade(setMovement ? a.get(Attribute.SHOTS) : 0);
      }
      if (a instanceof Conveyance) {
        setOrClearMovement(((Conveyance) a).getMount(), setMovement);
      }
    }
  }

  /**
   * Clear movement of actors of a given class and set movement of everything else.
   * @param world the world
   * @param clazz class of entity to clear movement flag for
   * @param newOwner new owner of this entity type (or -1 if owner should not be changed)
   */
  public static void clearMovement(final World world, final Class<?> clazz, final int newOwner) {
    for (int k = 0; k < world.size(); ++k) {
      final Actor a = world.actor(k);
      if (a != null) {
        final boolean hit = clazz.isInstance(a);
        setOrClearMovement(a, !hit);
        if (hit && newOwner > Actor.OWNER_NONE) {
          a.setOwner(newOwner);
        }
      }
    }
  }

  /**
   * Clear the movement flags for a player.
   * @param world the world
   * @param w the wizard
   * @param excludeCats should cats be excluded
   */
  public static void clearMovement(final World world, final Wizard w, final boolean excludeCats) {
    final int p = w.getOwner();
    w.setMoved(false);
    w.setEngaged(false);
    w.resetShotsMade();
    for (int k = 0; k < world.size(); ++k) {
      final Actor a = world.actor(k);
      if (a != null && a.getOwner() == p && !(a instanceof Solar)) {
        if (!excludeCats || !(a instanceof Cat)) {
          a.setEngaged(false);
          setOrClearMovement(a, false);
        }
      }
    }
  }

  /**
   * Mark all cats as having already moved if the cat lord is alive.
   * @param world the world
   * @return true if the cat lord is alive
   */
  public static boolean markAllCatsAsMoved(final World world) {
    final boolean catLordAlive = world.isCatLordAlive() != Actor.OWNER_NONE;
    if (catLordAlive) {
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a instanceof Cat) {
          setOrClearMovement(a, true);
        }
      }
    }
    return catLordAlive;
  }
}
