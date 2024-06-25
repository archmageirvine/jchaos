package chaos.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

import chaos.Chaos;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Conveyance;
import chaos.common.Elemental;
import chaos.common.FrequencyTable;
import chaos.common.Growth;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.growth.Earthquake;
import chaos.common.inanimate.Generator;
import chaos.common.monster.GoblinBomb;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.DefaultEventGenerator;
import chaos.util.NameUtils;


/**
 * Represents a single cell of the world. From this object it is possible
 * to extract the content of the cell at various levels.
 * @author Sean A. Irvine
 */
public class Cell extends DefaultEventGenerator implements Serializable {

  private static final int GOBLIN_BOMB_DAMAGE = 12;

  /** Holds the Actors in this cell */
  private final ArrayList<Actor> mActors = new ArrayList<>();

  /** The number of this cell. */
  private final int mCellNumber;

  /**
   * Construct a new cell with the specified cell number.
   * @param cellNumber cell number
   */
  public Cell(final int cellNumber) {
    mCellNumber = cellNumber;
  }

  /**
   * Return the cell number associated with this cell.
   * @return cell number
   */
  public int getCellNumber() {
    return mCellNumber;
  }

  /**
   * Return a reference to the currently visible actor in this
   * cell. If no actors are present then null is returned. The
   * actor is not affected by this probe.
   * @return the top actor
   */
  public Actor peek() {
    return mActors.isEmpty() ? null : mActors.get(0);
  }

  /**
   * Return a short string describing the contents of a cell.
   * @return description of cell
   */
  public String describe() {
    final Actor a = peek();
    if (a == null) {
      return "Nothing";
    }
    final StringBuilder s = new StringBuilder(NameUtils.getTextName(a));
    s.append(" (");
    final State state = a.getState();
    if (state != State.ACTIVE) {
      s.append(state.toString().toLowerCase(Locale.getDefault()));
    } else {
      if (!(a instanceof Wizard)) {
        final Wizard w = Chaos.getChaos().getWorld().getWizardManager().getWizard(a.getOwner());
        s.append(w == null ? "?" : w.getPersonalName()).append(", ");
      }
      s.append(a.getRealm());
    }
    return s.append(')').toString();
  }

  /**
   * Test if this cell contains the specified actor.  Will look
   * deeply into the cell include buried actors and mounted actors.
   * Returns true if the actor does occur in this cell.
   * @param actor actor to search for
   * @return true if the actor is in the cell
   * @throws NullPointerException if <code>actor</code> is null.
   */
  public boolean contains(final Actor actor) {
    if (actor == null) {
      throw new NullPointerException();
    }
    for (Actor a : mActors) {
      if (a == actor) {
        return true;
      }
      while (a instanceof Conveyance) {
        a = ((Conveyance) a).getMount();
        if (a == actor) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Test if this cell contains the specified type. Will look
   * deeply into the cell include buried actors and mounted actors.
   * Returns true if the actor does occur in this cell.
   * @param clazz type to search for
   * @return true if the type is in the cell
   * @throws NullPointerException if <code>clazz</code> is null.
   */
  public boolean contains(final Class<? extends Actor> clazz) {
    if (clazz == null) {
      throw new NullPointerException();
    }
    for (Actor a : mActors) {
      if (clazz.isInstance(a)) {
        return true;
      }
      while (a instanceof Conveyance) {
        a = ((Conveyance) a).getMount();
        if (clazz.isInstance(a)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Push an actor into the top position of this Cell.
   * @param actor actor to put in top position
   * @throws NullPointerException if <code>actor</code> is null.
   */
  public void push(final Actor actor) {
    if (actor == null) {
      throw new NullPointerException();
    }
    synchronized (mActors) {
      mActors.add(0, actor);
    }
    notify(new CellEvent(getCellNumber()));
  }

  /**
   * Return a reference to the currently visible actor in this
   * cell and remove it from the top position to reveal any
   * actor below. If there are no Actors present then null is
   * returned.
   * @return the top actor
   */
  public Actor pop() {
    if (mActors.isEmpty()) {
      return null;
    } else {
      final Actor a;
      synchronized (mActors) {
        a = mActors.remove(0);
      }
      notify(new CellEvent(getCellNumber()));
      return a;
    }
  }

  /**
   * Test if the cell is essentially vacant.
   * @return true if something can move onto the cell
   */
  private boolean blocking() {
    final Actor a = peek();
    return a != null && a.getState() != State.DEAD;
  }

  /** Handle reinstate call on a wizard. */
  private boolean wizardReinstate(final Wizard w) {
    // some power-ups are lost immediately
    final int pw = w.getOwner();
    w.set(PowerUps.MOVE_IT, 0);
    w.set(PowerUps.UNCERTAINTY, 0);
    notify(new CellEffectEvent(mCellNumber, CellEffectType.WIZARD_EXPLODE));

    // loop through entire world looking for things belonging to this player
    // and take appropriate action to destroy or sleep them.
    final World world = Chaos.getChaos().getWorld();
    if (world != null) {
      world.getWarpSpace().prune(pw);
      for (int i = 0; i < world.size(); ++i) {
        final Cell ci = world.getCell(i);
        if (ci == this) {
          continue;
        }

        // silently sleep all buried things of this wizard
        for (int j = 1; j < ci.mActors.size(); ++j) {
          final Actor ca = ci.mActors.get(j);
          if (ca.getOwner() == pw && ca.getState() == State.ACTIVE) {
            ca.setOwner(0);
            ca.setState(State.ASLEEP);
          }
        }

        final Actor ca = ci.peek();
        if (ca != null && ca.getOwner() == pw) {
          if (ca instanceof Meditation || ca instanceof Generator) {
            // EXPLODE
            ci.reinstate(); // recurse on this cell
          } else if (ca.getState() == State.ACTIVE) {
            ca.setOwner(0);
            ca.setState(State.ASLEEP);
          }
        }
      }
    }

    // handle wizard with Lich Lord power-up
    if (w.is(PowerUps.LICH_LORD)) {
      // modify some power-ups
      w.set(PowerUps.BOW, 0);
      w.set(PowerUps.SWORD, 0);
      w.decrement(PowerUps.LICH_LORD);
      w.setRealm(Realm.ETHERIC);
      w.set(Attribute.LIFE, w.getDefault(Attribute.LIFE));
      // EFFECT
      return false; // wizard survived
    }

    // handle wizard with Dead Revenge power-up
    if (w.is(PowerUps.DEAD_REVENGE)) {
      // Set up the generator on top of the wizard
      w.decrement(PowerUps.DEAD_REVENGE);
      final Generator g = new Generator();
      g.setOwner(w.getOwner());
      push(g);
      // This player cannot cast spells any more, but technically remains alive below the generator.
      w.setSelector(null);
      return false; // generator here now
    }

    // if we reach here then the wizard is really dead
    pop();
    w.setState(State.DEAD); // needed to detect end of game
    return !blocking();
  }


  /**
   * Performs the reinstate operation on a cell.  Essentially, this function
   * should be called whenever the top-level object in the cell has suffered
   * a fatality. The purpose of this function is then to make sure that the
   * cell accurately reflects its new contents. This is not simply a matter
   * of replacing the current contents with a dead image, as there are a number
   * of special cases which need to be handled rather carefully.<p>
   *
   * Generally the caller will want to know if the cell is vacant at the end
   * of the reinstatement. Therefore, this function returns <code>true</code>
   * if it is possible to move onto the cell and <code>false</code> otherwise.<p>
   *
   * It is safe to call this function at any time. If the cell is empty then
   * no action is taken and true is returned. If there is a corpse in the
   * cell it is popped. In the case of mounts and meditations any mounted
   * actor is replaces the mount or meditation. Special handling is included
   * with wizards with the lich or dead revenge power-ups. The death of growths
   * may expose previously buried actors. Elemental creatures die to produce
   * another actor. Reincarnation is performed as required. Handles the hidden
   * horror and the goblin bomb.
   * @return true if cell is now vacant.
   */
  public boolean reinstate() {
    final Actor a = peek();
    if (a == null) {
      return true;
    } else if (a.getState() == State.DEAD) {
      pop();
      return !blocking();
    } else if (a instanceof Wizard) {
      return wizardReinstate((Wizard) a);
    }
    // Generator is a special case, because it might have come from a wizard with a
    // Dead Revenge spell. In this case, the wizard will be "under" the Generator
    // and another round of reinstate is called (e.g. the wizard might have a second
    // and subsequent Dead Revenge).
    if (a instanceof Generator) {
      pop();
      notify(new CellEvent(getCellNumber()));
      return reinstate();
    }
    // handle reincarnation
    if (a instanceof Monster && a.is(PowerUps.REINCARNATE)) {
      // this monster has reincarnation set, but a complication arises
      // if it is mounted. We can only do reincarnation if the mounted
      // creature can also be retained.
      final Monster m = (Monster) a;
      Actor mount = null;
      if (m instanceof Conveyance) {
        mount = ((Conveyance) m).getMount();
      }
      final Class<? extends Monster> re = m.reincarnation();
      if (re != null) {
        // construct reincarnation and preserve basic qualities
        final Monster rem = (Monster) FrequencyTable.instantiate(re);
        rem.setState(m.getState());
        rem.setOwner(m.getOwner());
        rem.setRealm(m.getRealm());
        rem.set(PowerUps.REINCARNATE, rem.reincarnation() != null ? 1 : 0);
        if (mount != null) {
          if (rem instanceof Conveyance) {
            ((Conveyance) rem).setMount(mount);
            pop();
            push(rem);
            return false;
          }
          // otherwise reincarnation will fail due to mount
          // situation is handled further below
        } else {
          pop();
          push(rem);
          return false;
        }
      }
    }
    if (a instanceof Elemental) {
      pop();
      final Actor elementalReplacement = ((Elemental) a).getElementalReplacement();
      if (elementalReplacement instanceof Earthquake) {
        notify(new CellEffectEvent(this, CellEffectType.EARTHQUAKE, elementalReplacement));
      }
      push(elementalReplacement);
      return false;
    }
    if (a instanceof Growth) {
      // Killing a growth may expose something from underneath
      pop();
      final Actor na = peek();
      if (na == null) {
        return true;
      }
      if (na.getState() != State.DEAD) {
        na.set(Attribute.LIFE, na.getDefault(Attribute.LIFE) >> 1);
        na.setMoved(false);
        if (na instanceof Monster) {
          ((Monster) na).resetShotsMade();
        }
      }
      return !blocking();
    } else if (a instanceof GoblinBomb) {
      notify(new CellEffectEvent(mCellNumber, CellEffectType.BOMB));
      pop(); // burnt during the explosion!
      final World world = Chaos.getChaos().getWorld();
      for (final Cell c : world.getCells(mCellNumber, 1, 1, false)) {
        final Actor aa = c.peek();
        if (aa != null) {
          if (aa.getState() == State.DEAD || aa.decrement(Attribute.LIFE, GOBLIN_BOMB_DAMAGE)) {
            c.reinstate();
          }
        }
      }
      return !blocking();
    }
    // handle mounts
    final Actor mounted;
    if (a instanceof Conveyance && (mounted = ((Conveyance) a).getMount()) != null) {
      pop();
      push(mounted);
      return false; // Cell is definitely not vacant
    }

    // normal death
    if (!(a instanceof NoDeadImage)) {
      a.setState(State.DEAD);
      // set some stats as appropriate for the dead
      a.set(Attribute.LIFE, 0);
      a.set(Attribute.LIFE_RECOVERY, 0);
      a.set(Attribute.MAGICAL_RESISTANCE, 0);
      a.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 0);
      if (a instanceof Monster) {
        final Monster m = (Monster) a;
        m.set(Attribute.INTELLIGENCE, 0);
        m.set(Attribute.INTELLIGENCE_RECOVERY, 0);
        m.set(Attribute.AGILITY, 0);
        m.set(Attribute.AGILITY_RECOVERY, 0);
        m.set(Attribute.COMBAT, 0);
        m.set(Attribute.COMBAT_RECOVERY, 0);
        m.set(Attribute.RANGED_COMBAT, 0);
        m.set(Attribute.RANGED_COMBAT_RECOVERY, 0);
        m.set(Attribute.SPECIAL_COMBAT, 0);
        m.set(Attribute.SPECIAL_COMBAT_RECOVERY, 0);
        m.set(Attribute.RANGE, 0);
        m.set(Attribute.RANGE_RECOVERY, 0);
        m.set(Attribute.MOVEMENT, 0);
        m.set(Attribute.MOVEMENT_RECOVERY, 0);
      }
      // make sure we remove any existing corpses first
      while (pop() != null) {
        // do nothing
      }
      push(a);
      // this notify needed because of possible image change
      notify(new CellEvent(getCellNumber()));
    } else {
      pop();
    }
    return !blocking();
  }

  /**
   * Return the mount or meditator in this cell.  If there is no
   * mount of meditator then null is returned.
   * @return the mount
   */
  public Actor getMount() {
    final Actor a = peek();
    if (a instanceof Conveyance) {
      return ((Conveyance) a).getMount();
    }
    return null;
  }

  @Override
  public String toString() {
    return String.valueOf(mCellNumber);
  }
}
