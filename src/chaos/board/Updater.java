package chaos.board;

import java.util.Arrays;
import java.util.HashSet;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.Conveyance;
import chaos.common.FrequencyTable;
import chaos.common.Inanimate;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Rider;
import chaos.common.State;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.Tempest;
import chaos.common.inanimate.Vortex;
import chaos.common.monster.Solar;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolyshieldEvent;
import chaos.util.Random;
import chaos.util.TextEvent;

/**
 * Performs the update phase.
 * @author Sean A. Irvine
 */
public class Updater {

  private final World mWorld;
  private final int[] mMovementPointsSum;

  /**
   * Construct a new updater for the specified world.
   * @param world the world
   * @throws NullPointerException if <code>world</code> is null.
   */
  public Updater(final World world) {
    if (world == null) {
      throw new NullPointerException();
    }
    mWorld = world;
    mMovementPointsSum = new int[mWorld.getWizardManager().getMaximumPlayerNumber() + 2];
  }

  /**
   * Special updates that must be applied to a wizard, regardless of the
   * wizard's current visibility.
   */
  private void updateWizard(final Wizard wiz) {
    if (wiz != null) {
      wiz.decrement(PowerUps.CONFIDENCE);
      wiz.decrement(PowerUps.BATTLE_CRY);
      wiz.decrement(PowerUps.NO_MOUNT);
      wiz.resetShotsMade();

      // handle uncertainty
      int u = wiz.get(PowerUps.UNCERTAINTY);
      if (u != 0) {
        for (int k = 0; k < Math.min(2, u); ++k) {
          final int cc = Random.nextInt(mWorld.size());
          final Actor a = mWorld.actor(cc);
          if (a == null || a.getState() == State.DEAD) {
            final Cell celli = mWorld.getCell(cc);
            celli.notify(new CellEffectEvent(celli, CellEffectType.WARP_IN));
            Actor ins;
            while (!((ins = FrequencyTable.DEFAULT.getUniformRandomActor()) instanceof Monster) || ins instanceof Inanimate) {
              // do nothing
            }
            if (ins instanceof Solar) {
              ins.setMoved(true);
            }
            ins.setOwner(wiz.getOwner());
            celli.push(ins);
            celli.notify(new CellEffectEvent(celli, CellEffectType.REDRAW_CELL));
            --u;
          }
        }
        wiz.set(PowerUps.UNCERTAINTY, u);
      }
    }
  }

  static final Attribute[] TOP_SCORER_BONUS_ATTRIBUTES = {
    Attribute.AGILITY,
    Attribute.AGILITY_RECOVERY,
    Attribute.INTELLIGENCE,
    Attribute.INTELLIGENCE_RECOVERY,
    Attribute.MAGICAL_RESISTANCE,
    Attribute.MAGICAL_RESISTANCE_RECOVERY,
    Attribute.LIFE
  };

  /** Apply a bonus for the wizard having the highest score. */
  public void applyTopScorerBonus() {
    // Determine wizard(s) with the highest score
    final HashSet<Wizard> best = new HashSet<>();
    int bestScore = 1; // Must have at least one point to qualify
    for (final Wizard wiz : mWorld.getWizardManager().getWizards()) {
      if (wiz != null && wiz.getScore() >= bestScore) {
        if (wiz.getScore() > bestScore) {
          best.clear();
          bestScore = wiz.getScore();
        }
        best.add(wiz);
      }
    }
    // Determine all the affected cells for the top-scoring wizards
    final HashSet<Cell> affected = new HashSet<>();
    for (final Wizard wiz : best) {
      if (wiz.getState() == State.ACTIVE) {
        affected.addAll(mWorld.getCells(wiz.getOwner()));
      }
    }
    // Apply a small bonus to each affected cell
    final Attribute a = TOP_SCORER_BONUS_ATTRIBUTES[Random.nextInt(TOP_SCORER_BONUS_ATTRIBUTES.length)];
    mWorld.notify(new TextEvent("Top scorer bonus"));
    for (final Cell c : affected) {
      final Actor actor = c.peek();
      if (actor != null) {
        actor.increment(a, 1);
      }
    }
    mWorld.notify(new PolyshieldEvent(affected, a, null));
  }

  private void computeTotalMovementPointsPerPlayer() {
    Arrays.fill(mMovementPointsSum, 0);
    for (final Cell c : mWorld) {
      final Actor a = c.peek();
      if (a != null && a.getState() == State.ACTIVE) {
        mMovementPointsSum[a.getOwner() + 2] += a.get(Attribute.MOVEMENT); // todo yeuch!
      }
    }
  }

  private void checkExitOpenCloseStatus(final Exit exit) {
    switch (exit.getExitType()) {
      case ALWAYS_OPEN:
        exit.setOpen(true);
        break;
      case NO_REAL_ENEMY:
        final int owner = exit.getOwner() + 2;
        for (int k = 0; k < mMovementPointsSum.length; ++k) {
          if (k != owner && mMovementPointsSum[k] != 0) {
            exit.setOpen(false);
            return;
          }
        }
        exit.setOpen(true);
        break;
      default:
        exit.setOpen(false);
        break;
    }
  }

  /**
   * Perform the board update phase of the game.  Actions performed include
   * clearing the movement flag; application of recovery rates; cat lord
   * reassignments.  Activity of generators.
   */
  public void update() {
    computeTotalMovementPointsPerPlayer();
    // Mark everything as moved, then unmark certain actors for subsequent special processing
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a != null) {
        a.setMoved(true);
        a.decrement(PowerUps.INVULNERABLE);
        if (a instanceof Monster) {
          ((Monster) a).setShotsMade(a.get(Attribute.SHOTS));
        }
        if (a instanceof Conveyance) {
          final Actor mount = ((Conveyance) a).getMount();
          if (mount != null) {
            mount.setMoved(true);
            if (mount instanceof Monster) {
              ((Monster) mount).setShotsMade(a.get(Attribute.SHOTS));
            }
          }
        }
      }
    }
    for (int i = 0; i < mWorld.size(); ++i) {
      final Cell c = mWorld.getCell(i);
      final Actor a = c.peek();
      if (a instanceof Vortex || a instanceof Tempest) {
        // skip update for now, to avoid order problems
      } else if (a instanceof Exit) {
        checkExitOpenCloseStatus((Exit) a);
      } else if (a != null) {
        // Call the actors update to do recovery.
        if (a.update(mWorld, c)) {
          // Actor update indicates this cell should be terminated as a result
          // of the update, call reinstate on the cell.
          c.notify(new AudioEvent(c, a, "reinstate"));
          c.reinstate();
          // Following makes sure we don't inadvertently make bad changes to this
          // cell as a result of reinstatement.
          continue;
        }
        if (a instanceof Conveyance) {
          final Actor inside = ((Conveyance) a).getMount();
          if (inside != null) {
            inside.setMoved(false);
            inside.decrement(PowerUps.INVULNERABLE);
            if (inside instanceof Monster) {
              ((Monster) inside).resetShotsMade();
            }
          }
          if (a instanceof Meditation) {
            final Meditation m = (Meditation) a;
            if ((m.freeCollapse() || inside != null) && Random.nextInt(m.collapseFactor()) == 0) {
              // Perform collapse.
              c.notify(new CellEffectEvent(c, CellEffectType.MEDITATION_COLLAPSE, c.getMount()));
              c.reinstate();
              if (c.peek() instanceof Rider) {
                final Monster rider = (Monster) c.peek();
                final Wizard w = mWorld.getWizardManager().getWizard(rider);
                if (w != null) {
                  mWorld.notify(new TextEvent("Bonus spell for " + w.getPersonalName()));
                  w.increment(PowerUps.LEVEL);
                  w.addBonus(1, 4 + Random.nextInt(6));
                  c.notify(new CellEffectEvent(c, CellEffectType.BONUS, w));
                }
              }
            }
          }
        }
      }
    }
    // Deal with the cat lord
    final int catLord = mWorld.isCatLordAlive();
    if (catLord >= 0) {
      for (int i = 0; i < mWorld.size(); ++i) {
        final Cell c = mWorld.getCell(i);
        final Actor a = c.peek();
        if (a instanceof Cat) {
          a.setOwner(catLord);
          a.setMoved(true);
        }
      }
    }
    // Finally deal with objects that can move themselves or others about the screen.
    for (int i = 0; i < mWorld.size(); ++i) {
      final Cell c = mWorld.getCell(i);
      final Actor a = c.peek();
      if ((a instanceof Vortex || a instanceof Tempest) && a.update(mWorld, c)) {
        // actor update indicates this cell should be terminated as a result
        // of the update, call reinstate on the cell.
        c.notify(new AudioEvent(c, a, "reinstate"));
        c.reinstate();
      }
    }
    for (final Wizard wiz : mWorld.getWizardManager().getWizards()) {
      updateWizard(wiz);
    }
  }

}

