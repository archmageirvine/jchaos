package chaos.board;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

import chaos.common.Actor;
import chaos.common.Conveyance;
import chaos.common.State;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Maintains warped actors.
 * @author Sean A. Irvine
 */
public class WarpSpace implements Serializable {

  /** Time spent in warp space. */
  private static final int WARP_TIME = 5;

  /**
   * Noddy class to hold warped actors.
   */
  private static class Warpee implements Serializable {
    /** Actor in warp space. */
    Actor mActor;
    /** Time remaining. */
    int mTime = WARP_TIME;

    Warpee(final Actor actor) {
      mActor = actor;
    }
  }

  /** Actors currently in warp space. */
  private final HashSet<Warpee> mWarpee = new HashSet<>();

  /**
   * Warp the specified actor into warp space.
   * @param cell cell to warp
   * @param cause the actor causing this warp event (can be null)
   */
  public void warpOut(final Cell cell, final Actor cause) {
    if (cell != null) {
      final Actor a = cell.pop();
      if (a != null) {
        // In theory the actor should never be null, since this means Hide,
        // Vanish, etc. was cast on an empty cell.  But this happened at
        // least once in the wild.  Thus if we ever actually see a null
        // warp, we just ignore it.
        cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_OUT, cause));
        mWarpee.add(new Warpee(a));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  /**
   * Silently remove all items belonging to a player from warp space.
   * @param owner owner to remove
   */
  void prune(final int owner) {
    mWarpee.removeIf(w -> w.mActor.getOwner() == owner);
  }

  /**
   * Test if warp space contains the specified actor.
   * @param actor actor to test for
   * @return true if the actor is in warp space
   */
  public boolean contains(final Actor actor) {
    for (final Warpee w : mWarpee) {
      final Actor a = w.mActor;
      if (a == actor) {
        return true;
      } else if (a instanceof Conveyance && ((Conveyance) a).getMount() == actor) {
        return true;
      }
    }
    return false;
  }

  /**
   * Warp actors into the world.  Actors having spent the required time in warp
   * space are returned to normal space.  If there is no empty space in
   * ordinary space, then another actor is warped out to make space.  Those
   * not yet ready to be warped have their times reduced.
   * @param world world to warp into
   */
  public void warpIn(final World world) {
    if (world != null) {
      for (final Iterator<Warpee> i = mWarpee.iterator(); i.hasNext(); ) {
        final Warpee w = i.next();
        if (--w.mTime <= 0) {
          // Time to warp back into ordinary space
          int c = -1;
          int count = 1;
          // Try and find an empty cell first
          for (int j = 0; j < world.size(); ++j) {
            if (world.actor(j) == null && Random.nextInt(count++) == 0) {
              c = j;
            }
          }
          if (c == -1) {
            // No empty cells, try and find one with a corpse instead
            for (int j = 0; j < world.size(); ++j) {
              if (world.actor(j).getState() == State.DEAD && Random.nextInt(count++) == 0) {
                c = j;
              }
            }
          }
          final Actor current;
          if (c == -1) {
            // No empty cells and no corpses, choose any random cell and warp
            // it out, making the new space available.
            c = Random.nextInt(world.size());
            // Because of difficulties of concurrent modification, we cannot simply
            // call warpOut here. We simulate the warp and tidy up later.
            final Cell cell = world.getCell(c);
            cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_OUT, w.mActor));
            current = cell.pop();
            cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
          } else {
            current = null;
          }
          final Cell cell = world.getCell(c);
          cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_IN, w.mActor));
          cell.push(w.mActor);
          cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
          if (current == null) {
            i.remove(); // we are done with this entry
          } else {
            // replace entry with what we exchanged
            w.mTime = WARP_TIME;
            w.mActor = current;
          }
        }
      }
    }
  }

}
