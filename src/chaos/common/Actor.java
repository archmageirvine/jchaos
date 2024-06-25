package chaos.common;

import java.util.EnumMap;
import java.util.Map;

import chaos.board.Cell;
import chaos.board.World;
import chaos.util.Random;

/**
 * This class provides additional methods for any Castable which can
 * appear as a presence on the main board of the game. This layer
 * provides the fundamental statistics for an actor including who
 * owns it, what realm it is in, and certain existential characteristics.<p>
 *
 * Each Actor has a realm which reflects its current origin. Material
 * creatures naturally enough come from the <code>MATERIAL</code>,
 * the undead from <code>ETHERIC</code>, and so on. Certain
 * generic objects may have the realm type <code>NONE</code>.
 * (This could be used for empty space, although that may later be
 * recoded as a terrain).<p>
 *
 * Each actor also has an associated state. Actors are normally in the
 * <code>ACTIVE</code> state, meaning they should be animated and are
 * capable of various game actions. Many Actors also support a <code>
 * DEAD</code> state when they are corpses. All the creatures can also
 * be in the <code>ASLEEP</code> state. Finally, there is a <code>
 * STASIS</code> state for players not currently present (this state
 * is not presently used).<p>
 *
 * Each Actor also has a owner. Normally this is the index of the player
 * responsible for this Actor, but it may also have the special values
 * of <code>OWNER_INDEPENDENT</code> for an object with an independent
 * existence or <code>OWNER_NONE</code> when no owner is appropriate.
 *
 * This class provides the fundamental statistics for life force and
 * magical resistance together with the corresponding recovery rates.
 * The <code>update</code> method applies the recoveries.
 * @author Sean A. Irvine
 */
public abstract class Actor extends Castable {

  /** Actor owned by nobody, for example, empty space */
  public static final int OWNER_NONE = -1;
  /** An independent actors (that is, one with no controlling wizard) */
  public static final int OWNER_INDEPENDENT = -2;

  private final int[] mDefaultValues = new int[Attribute.values().length];
  private final int[] mValues = new int[mDefaultValues.length];

  /** The default realm for this Actor. */
  protected final Realm mNaturalRealm = Realm.NONE;
  /** The realm of this Actor */
  private Realm mRealm = mNaturalRealm;

  /** The movement status of this Actor, false for not moved. */
  private boolean mMovementStatus = false;

  /** The engagement status of this Actor, false for not engaged. */
  private boolean mEngaged = false;

  /**
   * Get the currently specified realm for this Actor.
   * @return the realm
   */
  public Realm getRealm() {
    return mRealm;
  }

  /**
   * Set the realm for this Actor. This may be called when a creature
   * changes realm as the result of magic. For example, raising the
   * dead causes the Actor to become <code>ETHERIC</code>.
   * @param realm new realm
   * @throws NullPointerException if <code>realm</code> is null
   */
  public void setRealm(final Realm realm) {
    if (realm == null) {
      throw new NullPointerException();
    }
    mRealm = realm;
  }

  private State mState = State.ACTIVE;

  /**
   * Return the current state for this Actor.
   * @return current state
   */
  public State getState() {
    return mState;
  }

  /**
   * Set the state of this Actor. An <code>IllegalArgumentException</code>
   * is thrown if the specified state is not valid.
   * @param state new state
   */
  public void setState(final State state) {
    mState = state;
    // update weight based on state
    switch (state) {
      case DEAD:
        mWeight = 0;
        break;
      case ASLEEP:
        mWeight = 1;
        break;
      default:
        mWeight = getDefaultWeight();
        break;
    }
  }

  /** The current owner of this Actor. */
  private int mOwner = OWNER_NONE;

  /**
   * Return the current owner of this Actor.
   * @return the current owner
   */
  public int getOwner() {
    return mOwner;
  }

  /**
   * Set the current owner of this Actor.
   * @param owner new owner
   */
  public void setOwner(final int owner) {
    mOwner = owner;
  }

  /**
   * Test if this Actor is currently marked as having moved.
   * @return true if Actor has moved.
   */
  public boolean isMoved() {
    return mMovementStatus;
  }

  /**
   * Set the movement status for this Actor.
   * @param moved true if to set moved flag, false to clear it.
   */
  public void setMoved(final boolean moved) {
    mMovementStatus = moved;
  }

  /**
   * Test if this Actor is currently marked as having engaged.
   * @return true if Actor has engaged.
   */
  public boolean isEngaged() {
    return mEngaged;
  }

  /**
   * Set the movement status for this Actor.
   * @param engaged true if to set engaged flag, false to clear it.
   */
  public void setEngaged(final boolean engaged) {
    mEngaged = engaged;
  }

  protected boolean recover(final Attribute attr) {
    final int current = get(attr);
    final int def = getDefault(attr);
    final int rec = get(attr.recovery());
    if (rec < 0) {
      if (def >= 0) {
        return decrement(attr, -rec);
      } else {
        increment(attr, rec);
      }
    } else {
      if (def >= 0 && (current < def || current < rec)) {
        set(attr, Math.min(Math.max(def, rec), current + rec));
      } else if (def < 0 && current > def) {
        set(attr, Math.max(Math.min(def, rec), current - rec));
      }
    }
    return false;
  }

  /**
   * Perform an update cycle on this actor. This involves applying
   * the recovery rates to the relevant statistics.  The actor is
   * marked as unmoved.  If the result is true then the actor has
   * "died" as a result of the update.
   * @param world the world containing the actor, may be null
   * @param cell the cell containing the actor. may be null
   * @return true if actor dies as a result of the update
   */
  public boolean update(final World world, final Cell cell) {
    final State state = getState();
    boolean dead = false;
    if (state == State.ASLEEP || state == State.ACTIVE) {
      dead = recover(Attribute.LIFE) || recover(Attribute.MAGICAL_RESISTANCE)
        || (state == State.ASLEEP && Random.nextInt(Math.max(1, get(Attribute.LIFE))) <= 2);

      setMoved(false);
      setEngaged(false);
    }
    return dead;
  }

  /**
   * Return the default weight value for this Actor.  For most Monsters this is
   * the same as the default life value.  When different behaviour is required
   * then this method must be overridden.
   * @return weight value
   */
  public int getDefaultWeight() {
    return getDefault(Attribute.LIFE);
  }

  /** The current weight value. */
  private Integer mWeight = null;

  /**
   * Compute the weight value for this Actor.
   * @return weight value
   */
  public int getWeight() {
    if (mWeight == null) {
      mWeight = getDefaultWeight();
    }
    return mWeight;
  }

  private int mKillCount = 0;

  /**
   * Return the number of confirmed kills of this actor.
   * @return kill count
   */
  public int getKillCount() {
    return mKillCount;
  }

  /**
   * Increase the number of confirmed kills by this actor by 1;
   * and return the new value.
   * @return kill count after increment
   */
  public int incrementKillCount() {
    return ++mKillCount;
  }

  /**
   * Set the kill count.
   * @param killCount the new kill count
   */
  public void setKillCount(final int killCount) {
    mKillCount = killCount;
  }

  /**
   * Returns a 64-bit mask used in the computation of line-of-sight
   * conditions.  These bits correspond to an 8&times;8 grid with
   * a 1 bit indicating line of sight is blocked.  Thus, a value of
   * 0L means this actor never blocks line of sight and a value of
   * ~0L means this actor always blocks line of sight.
   * @return masking value
   */
  public abstract long getLosMask();

  /**
   * Increment the given attribute by the specified value.  If the attribute
   * is not relevant to this actor, then no action is taken. This function
   * is only guaranteed to work properly for positive values of <code>inc</code>.
   * @param attr attribute
   * @param inc increment
   */
  public void increment(final Attribute attr, final int inc) {
    if (attr != null) {
      set(attr, get(attr) + inc);
    }
  }

  /**
   * Decrement the given attribute by the specified value.  If the attribute
   * is not relevant to this actor, then no action is taken.  If the actor
   * should die as a result, then true is returned.  The final value may
   * not always correspond to a straightforward subtraction. This function
   * is only guaranteed to work properly for positive values of <code>dec</code>.
   * @param attr attribute
   * @param dec decrement
   * @return true if actor should die as a result
   */
  public boolean decrement(final Attribute attr, final int dec) {
    if (attr != null) {
      final int v = get(attr);
      switch (attr) {
        case LIFE:
          final int life = v - dec;
          set(attr, life);
          return life <= 0;
        case MAGICAL_RESISTANCE:
          final int magicalResistance = v - dec;
          set(attr, magicalResistance);
          return magicalResistance < 0;
        case LIFE_RECOVERY:
        case MAGICAL_RESISTANCE_RECOVERY:
          set(attr, v - dec);
          return false;
        default:
          return false;
      }
    }
    return false;
  }

  /**
   * Get the value for a given attribute.  If this actor does not have the given
   * attribute then 0 is returned.
   * @param attr attribute
   * @return value of the attribute
   */
  public int get(final Attribute attr) {
    return attr == null ? 0 : mValues[attr.ordinal()];
  }

  /**
   * Set the value for a given attribute subject to maximum and minimum constraints.
   * @param attr attribute
   * @param value new value
   */
  public void set(final Attribute attr, final int value) {
    if (attr != Attribute.MOVEMENT || !(this instanceof Inanimate)) {
      mValues[attr.ordinal()] = Math.max(attr.min(), Math.min(attr.max(), value));
    }
  }

  protected void setDefault(final Attribute attr, final int value) {
    final int a = attr.ordinal();
    mDefaultValues[a] = value;
    set(attr, value);
    if (attr == Attribute.RANGE && value != 0) {
      mDefaultValues[Attribute.SHOTS.ordinal()] = 1;
      set(Attribute.SHOTS, 1);
    }
  }

  /**
   * Get the default value for a given attribute.  If this actor does not have the given
   * attribute then 0 is returned.
   * @param attr attribute
   * @return value of the attribute
   */
  public int getDefault(final Attribute attr) {
    if (attr == null) {
      return 0;
    }
    return mDefaultValues[attr.ordinal()];
  }

  /** Store the power-up statistics for this actor. */
  private final Map<PowerUps, Integer> mPowers = new EnumMap<>(PowerUps.class);

  /**
   * Retrieve a given power-up statistic of this actor.
   * @param statistic statistic to get
   * @return value of statistic
   */
  public int get(final PowerUps statistic) {
    final Integer i = mPowers.get(statistic);
    return i == null ? 0 : i;
  }

  /**
   * Test if the actor has the given power up to any degree
   * @param statistic power up
   * @return true if the actor has this power up
   */
  public boolean is(final PowerUps statistic) {
    return get(statistic) > 0;
  }

  /**
   * Set the given power-up statistic to the specified value.
   * @param statistic statistic to set
   * @param value value to set it to
   */
  public void set(final PowerUps statistic, final int value) {
    mPowers.put(statistic, value);
  }

  /**
   * Decrement a power up count by 1 provided it is positive.  This is
   * provided as a convenience to a get/set pair.
   * @param statistic statistic to decrement
   */
  public void decrement(final PowerUps statistic) {
    final int v = get(statistic);
    if (v > 0) {
      set(statistic, v - 1);
    }
  }

  /**
   * Increment a power up count.
   * @param statistic statistic to increment
   * @param amount amount to increment by
   */
  public void increment(final PowerUps statistic, final int amount) {
    set(statistic, get(statistic) + amount);
  }

  /**
   * Increment a power up count by 1.
   * @param statistic statistic to increment
   */
  public void increment(final PowerUps statistic) {
    increment(statistic, 1);
  }

}
