package chaos.board;

import static chaos.common.Castable.CAST_LOS;

import java.io.Serializable;

import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.Tree;
import chaos.common.beam.Beam;
import chaos.common.wizard.Wizard;
import chaos.util.DefaultEventGenerator;
import chaos.util.TextEvent;

/**
 * This important class is responsible for validating and carrying out
 * all cast requests from the various engines.
 *
 * @author Sean A. Irvine
 */
public class CastMaster extends DefaultEventGenerator implements Serializable {

  /** The world we are working in. */
  private final World mWorld;
  /** Line of sight handle. */
  private final LineOfSight mLOS;

  /**
   * Construct a new CastMaster on the given world.
   *
   * @param w a world
   */
  public CastMaster(final World w) {
    mWorld = w;
    mLOS = new LineOfSight(w);
  }

  /**
   * Check if it is possible to plant a tree in the indicated cell.
   * Returns false if a tree is in any adjacent cell.  It does not
   * validate that cell itself is allowed.
   *
   * @param cell cell we are testing
   * @return true if a tree is allowed here
   */
  public boolean checkTreeAllowed(final int cell) {
    for (final Cell c : mWorld.getCells(cell, 1, 1, false)) {
      if (c.peek() instanceof Tree) {
        return false;
      }
    }
    return true;
  }

  /**
   * Tests if a spell casting is legal.  Tests if a cast of <code>spell</code>
   * from <code>source</code> to <code>target</code> is legal.
   *
   * @param player player owning this caster
   * @param spell what is to be cast
   * @param source location of caster
   * @param target location of target
   * @return true if the cast is legal
   */
  public boolean isLegalCast(final Wizard player, final Castable spell, final int source, final int target) {

    if (spell instanceof FreeCastable) {
      return source == target;
    }

    final int cflags = spell.getCastFlags();
    if ((cflags & Castable.CAST_ANY) != 0) {
      // If we are not a beam, then range doesn't matter
      if (!(spell instanceof Beam)) {
        return true;
      }
    } else {
      final Actor a = mWorld.actor(target);
      if (a == null) {
        if ((cflags & Castable.CAST_EMPTY) == 0) {
          notify(new TextEvent("Cannot cast on empty space"));
          return false;
        }
      } else {
        final boolean isDead = a.getState() == State.DEAD;
        if (isDead && (cflags & Castable.CAST_DEAD) == 0) {
          notify(new TextEvent("Cannot cast on the dead"));
          return false;
        }

        if (a instanceof Inanimate && (cflags & Castable.CAST_INANIMATE) == 0) {
          notify(new TextEvent("Cannot cast on inanimates"));
          return false;
        }

        if (!isDead && a instanceof Monster && (cflags & Castable.CAST_LIVING) == 0) {
          notify(new TextEvent("Cannot cast on creatures"));
          return false;
        }

        if (a instanceof Wizard && (cflags & Castable.CAST_NOEXPOSEDWIZARD) != 0) {
          notify(new TextEvent("Cannot cast on wizards"));
          return false;
        }

        if ((cflags & Castable.CAST_NOWIZARDCELL) != 0 && mWorld.getCell(target).contains(Wizard.class)) {
          notify(new TextEvent("Cannot cast on cells containing a wizard"));
          return false;
        }

        if (a instanceof Growth && (cflags & Castable.CAST_GROWTH) == 0) {
          notify(new TextEvent("Cannot cast on growths"));
          return false;
        }

        if (a.getOwner() == Actor.OWNER_INDEPENDENT && (cflags & Castable.CAST_NOINDEPENDENTS) != 0) {
          notify(new TextEvent("Cannot cast on independents"));
          return false;
        }

        if (a.getRealm() != Realm.ETHERIC && (cflags & Castable.CAST_MUSTBEUNDEAD) != 0) {
          notify(new TextEvent("Can only cast on undead"));
          return false;
        }
      }

      if (spell instanceof Tree && (player == null || !player.is(PowerUps.ARBORIST)) && !checkTreeAllowed(target)) {
        notify(new TextEvent("Too close to an existing tree"));
        return false;
      }
    }

    // compute range and test distance requirement
    final double range = 0.5 + getRange(player, spell);
    if (mWorld.getSquaredDistance(source, target) > (int) (range * range)) {
      notify(new TextEvent("Out of range"));
      return false;
    }

    // test line of sight
    if ((cflags & CAST_LOS) != 0 && !mLOS.isLOS(source, target)) {
      if (player == null || (!player.is(PowerUps.CRYSTAL_BALL) && !player.is(PowerUps.DEPTH))) {
        notify(new TextEvent("No line of sight"));
        return false;
      }
    }
    return true;
  }

  /**
   * Get the range for casting a given spell with a given caster.
   * @param caster the caster
   * @param castable what is to be cast
   * @return the range
   */
  public static int getRange(final Caster caster, final Castable castable) {
    int range = castable.getCastRange();
    if (caster != null && !(castable instanceof FreeCastable) && !(castable instanceof Beam)) {
      range += caster.get(PowerUps.WAND);
      if (caster.is(PowerUps.DEPTH) && !(castable instanceof Actor)) {
        range = Castable.MAX_CAST_RANGE;
      }
    }
    return range;
  }

}
