package chaos.common;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.common.free.Horde;
import chaos.common.inanimate.Spawner;
import chaos.common.wizard.Wizard;
import chaos.engine.AiEngine;
import chaos.engine.PlayerEngine;

/**
 * This extension of Monster is made for those monsters capable of casting
 * spells. This includes those limited to casting the occasional effect,
 * such as the Azer casting quench through to full-blown wizards.
 * @author Sean A. Irvine
 */
public abstract class Caster extends Monster {

  private PlayerEngine mPlayerEngine = null;

  {
    // default realm for casters
    setRealm(Realm.MATERIAL);
  }

  /**
   * Return the PlayerEngine for performing this Caster's spells.
   * @return player engine
   */
  public PlayerEngine getPlayerEngine() {
    return mPlayerEngine;
  }

  /**
   * Set the PlayerEngine for this Caster.
   * @param engine caster's player engine
   */
  public void setPlayerEngine(final PlayerEngine engine) {
    mPlayerEngine = engine;
  }

  /**
   * Get the next Castable or null if nothing is to be cast.
   * @return Castable to be cast
   */
  public abstract Castable getCastable();

  /**
   * Perform any required casting taking careful account of multiplicity.
   * @param cell the cell containing the caster.
   */
  public void doCasting(final Cell cell) {
    final Castable castable = getCastable();
    if (castable != null && cell != null) {
      if (Chaos.isVerbose()) {
        final String code = this instanceof Wizard ? "Wizard" : "Other";
        System.out.println("Cast" + code + ": " + getOwner() + " " + castable.getClass().getName());
      }
      int multiplicity = 1;
      final int d = get(PowerUps.DOUBLE);
      if (d != 0) {
        multiplicity *= 2;
        decrement(PowerUps.DOUBLE);
      }
      final int t = get(PowerUps.TRIPLE);
      if (t != 0) {
        multiplicity *= 3;
        decrement(PowerUps.TRIPLE);
      }
      final Actor actor = cell.peek(); // Remember this in case actor moves during casting
      Cell casterCell = cell;
      final PlayerEngine engine = getPlayerEngine();
      if (engine == null) {
        // Ideally this should not happen, if does just skip the cast
        System.err.println("Huh! Null player engine for " + getName());
        Thread.dumpStack();
      } else if (castable instanceof Meditation && engine instanceof AiEngine) {
        ((AiEngine) engine).meditationCast(this, castable, casterCell, multiplicity);
      } else {
        if (castable instanceof Multiplicity) {
          multiplicity *= ((Multiplicity) castable).getMultiplicity();
        }
        boolean retainName = true;
        while (multiplicity-- != 0) {
          // Need to be very careful here.  Because we could be using double or
          // triple of a spell with multiplicity then each one cast must be a
          // new instance (technically only for Actors). Making copies is hard
          // for some things like Horde which have additional state.  Nasty are
          // named creatures, where only the first should really have the name
          // as proposed on the spell list.  Experience indicates that it is
          // very dangerous to use the exact object passed in here.
          if (!engine.cast(this, getCopy(castable, retainName), casterCell)) {
            // User aborted the spell. Don't continue with the multiplicity
            return;
          }
          // The following evilness is to handle cases where we have multiplicity,
          // but that the spell itself might have moved the wizard, e.g. teleport
          // with double, perhaps also Hide and Vanish.
          if (casterCell.peek() != actor) {
            casterCell = Chaos.getChaos().getWorld().getCell(actor);
            if (casterCell == null) {
              return; // Abort if we can no longer find the caster
            }
          }
          retainName = false;
        }
      }
    }
  }

  private Castable getCopy(final Castable castable, final boolean retainName) {
    if (castable instanceof Spawner) {
      return new Spawner(((Spawner) castable).getSpawn());
    }
    if (castable instanceof Horde) {
      return new Horde(((Horde) castable).getSpawn());
    }
    final Castable copy = FrequencyTable.instantiate(castable.getClass());
    if (retainName && copy instanceof Named) {
      ((Named) copy).setPersonalName(((Named) castable).getPersonalName());
    }
    return copy;
  }
}
