package chaos.common;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * This extension of Actor is made for the large class of Actors which
 * can move, take an active part in combat, or have a mind. As such it
 * provides further statistics for these characteristics and refinements
 * to the <code>update</code> method to maintain these characteristics.
 * @author Sean A. Irvine
 */
public abstract class Monster extends Actor {

  /** The attribute that combat applies to */
  private Attribute mCombatApply = null;
  /** The attribute that ranged combat applies to */
  private Attribute mRangedCombatApply = null;
  /** The attribute that special combat applies to */
  private Attribute mSpecialCombatApply = null;

  /**
   * Get the attribute ordinary combat applies to.
   * @return attribute for normal combat
   */
  public Attribute getCombatApply() {
    return mCombatApply;
  }

  /**
   * Set the attribute to which normal combat will be applied.
   * @param apply attribute for normal combat
   */
  public void setCombatApply(final Attribute apply) {
    mCombatApply = apply;
  }

  /**
   * Get the attribute ranged combat applies to.
   * @return attribute for ranged combat
   */
  public Attribute getRangedCombatApply() {
    return mRangedCombatApply;
  }

  /**
   * Set the attribute to which ranged combat will be applied.
   * @param apply attribute for ranged combat
   */
  public void setRangedCombatApply(final Attribute apply) {
    mRangedCombatApply = apply;
  }

  /**
   * Get the attribute special combat applies to.
   * @return attribute for special combat
   */
  public Attribute getSpecialCombatApply() {
    return mSpecialCombatApply;
  }

  /**
   * Set the attribute to which special combat will be applied.
   * @param apply attribute for special combat
   */
  public void setSpecialCombatApply(final Attribute apply) {
    mSpecialCombatApply = apply;
  }

  private int mShotsMadeThisTurn = 0;

  /**
   * Test if this Monster is currently marked as having shot.
   * @return true if Monster has shot.
   */
  public int getShotsMade() {
    return mShotsMadeThisTurn;
  }

  /**
   * Set the shooting status for this Monster.
   * @param shot true if to set shot flag, false to clear it.
   */
  public void setShotsMade(final int shot) {
    mShotsMadeThisTurn = shot;
  }

  /** Reset the number of shots made counter. */
  public void resetShotsMade() {
    mShotsMadeThisTurn = 0;
  }

  /**
   * Increase the number of shots made so far by this actor this turn.
   */
  public void incrementShotsMade() {
    ++mShotsMadeThisTurn;
  }

  /**
   * Return the class this Actor will reincarnate to. If this actor
   * does not reincarnate then return null.
   * @return class to reincarnate as
   */
  public abstract Class<? extends Monster> reincarnation();

  @Override
  public int getCastRange() {
    return 1;
  }

  @Override
  public int getCastFlags() {
    return Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_LOS;
  }

  /**
   * Perform an update cycle on this actor. This involves applying
   * the recovery rates to the relevant statistics.
   * @param world the world containing the actor, may be null
   * @param cell the cell containing the actor. may be null
   * @return true if the monster dies as a result of the update.
   */
  @Override
  public boolean update(final World world, final Cell cell) {
    if (super.update(world, cell)) {
      return true; // already scheduled for death, no point in continuing.
    } else if (getState() != State.ACTIVE && getState() != State.ASLEEP) {
      return false;
    }
    resetShotsMade();
    recover(Attribute.AGILITY);
    recover(Attribute.MOVEMENT);
    recover(Attribute.RANGE);
    recover(Attribute.COMBAT);
    recover(Attribute.RANGED_COMBAT);
    recover(Attribute.SPECIAL_COMBAT);
    return recover(Attribute.INTELLIGENCE);
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (caster != null) {
      setOwner(caster.getOwner());
      if (caster instanceof Wizard) {
        final Wizard w = (Wizard) caster;
        final int torment = w.get(PowerUps.TORMENT);
        if (torment != 0) {
          set(Attribute.LIFE, Math.min(get(Attribute.LIFE), 4));
          if (Random.nextBoolean()) {
            w.decrement(PowerUps.TORMENT);
          }
        }
      }
    }
    if (cell != null) {
      if (this instanceof Singleton && world.isAlive(getClass()) != Actor.OWNER_NONE) {
        // prevent casting of singleton if one already exists
        cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED, this));
      } else {
        if (casterCell != null) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.MONSTER_CAST_EVENT, this));
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.MONSTER_CAST_EVENT, this));
        cell.push(this);
      }
    }
  }

  @Override
  public void increment(final Attribute attr, final int inc) {
    if (attr != null) {
      switch (attr) {
        case MOVEMENT:
          if (!(this instanceof Inanimate)) {
            set(attr, get(attr) + inc);
          }
          break;
        case COMBAT:
        case RANGED_COMBAT:
        case SPECIAL_COMBAT:
          if (getDefault(attr) >= 0) {
            set(attr, get(attr) + inc);
          } else {
            set(attr, get(attr) - inc);
          }
          break;
        default:
          super.increment(attr, inc);
          break;
      }
    }
  }

  @Override
  public boolean decrement(final Attribute attr, final int dec) {
    if (attr != null) {
      switch (attr) {
        case MOVEMENT:
        case MOVEMENT_RECOVERY:
        case INTELLIGENCE_RECOVERY:
        case AGILITY_RECOVERY:
        case COMBAT_RECOVERY:
        case SPECIAL_COMBAT_RECOVERY:
        case RANGE_RECOVERY:
        case RANGED_COMBAT_RECOVERY:
        case AGILITY:
        case RANGE:
        case SHOTS:
          set(attr, get(attr) - dec);
          return false;
        case INTELLIGENCE:
          final int intelligence = get(Attribute.INTELLIGENCE) - dec;
          set(attr, intelligence);
          return intelligence < 0;
        case COMBAT:
        case SPECIAL_COMBAT:
        case RANGED_COMBAT:
          if (getDefault(attr) >= 0) {
            set(attr, Math.max(0, get(attr) - dec));
          } else {
            set(attr, Math.min(0, get(attr) + dec));
          }
          return false;
        default:
          return super.decrement(attr, dec);
      }
    }
    return false;
  }

}
