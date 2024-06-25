package chaos.board;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import chaos.common.Actor;
import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.Conveyance;
import chaos.common.Killer;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Rideable;
import chaos.common.Rider;
import chaos.common.State;
import chaos.common.inanimate.Exit;
import chaos.common.monster.Gollop;
import chaos.common.wizard.Wizard;
import chaos.sound.Sound;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.CombatUtils;
import chaos.util.DefaultEventGenerator;
import chaos.util.Merge;
import chaos.util.NameUtils;
import chaos.util.TextEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import irvine.math.r.DoubleUtils;

/**
 * This important class is responsible for validating and carrying out
 * all movement requests from the various engines. It is the sole
 * arbitrator of access to the World for the various players.
 * @author Sean A. Irvine
 */
public class MoveMaster extends DefaultEventGenerator implements Serializable {

  private final World mWorld;
  private final LineOfSight mLOS;
  private final Reachable mReachable;

  /**
   * Construct a new MoveMaster on the given world.
   * @param w a world
   */
  public MoveMaster(final World w) {
    mWorld = w;
    mLOS = new LineOfSight(mWorld);
    mReachable = new Reachable(mWorld);
  }

  /** Maps from a player to the most recently queried actor. */
  private final HashMap<Wizard, Actor> mMostRecentAttention = new HashMap<>();
  /** Stores state information about the current actor. */
  private final HashMap<Wizard, AttentionDetail> mAttentionDetails = new HashMap<>();

  /**
   * See if this actor is the one attention was already focused on.
   * If so return the existing attention detail otherwise construct
   * a new one.
   * @param p who made the request
   * @param a what did they do it on
   * @return attention details for the actor
   */
  private AttentionDetail getAttention(final Wizard p, final Actor a) {
    final Actor prev = mMostRecentAttention.put(p, a);
    if (prev != a) {
      // If prev has moved at all then we must prevent further
      // movement in the near future, this is necessary to
      // prevent certain double movements in cases where the
      // engine swaps after creatures after a partial movement
      if (prev instanceof Monster) {
        final AttentionDetail pad = mAttentionDetails.get(p);
        final int pm = prev.get(Attribute.MOVEMENT);
        final int pms = pm * pm + pm;
        if (pad != null && pad.mSquaredMovementPoints != AttentionDetail.UNKNOWN && pad.mSquaredMovementPoints != pms) {
          prev.setMoved(true);
        }
      }
      mAttentionDetails.put(p, new AttentionDetail());
    }
    return mAttentionDetails.get(p);
  }

  /**
   * Remove any attention this player has. Clean up the state after a move is completed.
   * @param p who made the request
   */
  public void dropAttention(final Wizard p) {
    mMostRecentAttention.remove(p);
    mAttentionDetails.remove(p);
  }

  private int finishSuccessfulMove(final Wizard p, final Monster m) {
    assert m.getOwner() == p.getOwner();
    notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
    m.setMoved(true);
    dropAttention(p);
    return OK;
  }

  /**
   * Get the actor at the specified position. Returns null if there
   * is no actor at the specified position.
   * @param cell cell index
   * @return actor at given index
   */
  public Actor actor(final int cell) {
    return mWorld.actor(cell);
  }

  /**
   * Test if the contents of cell <code>source</code> are engaged.
   * @param player who is asking for the test
   * @param source the cell to test.
   * @return true if the cell is engaged
   */
  public boolean isEngaged(final Wizard player, final int source) {
    final Actor a = actor(source);
    if (a == null) {
      return false;
    }
    final AttentionDetail detail = getAttention(player, a);
    if (!mWorld.checkEngagement(source, true)) {
      // i.e. impossible for this actor to be engaged, this situation
      // can arise, if enemies surrounding a previously compulsorily
      // engaged creature are killed.  Bug#42 Callum Irvine.
      detail.mEngaged = 0;
      detail.mEngageIsCompulsory = true;
      a.setEngaged(false);
      return false;
    } else if (a.isEngaged()) {
      return true;
    } else if (detail.mEngaged == AttentionDetail.UNKNOWN) {
      if (detail.mEngageIsCompulsory) {
        // Engagement is compulsory and we have determined above that engagement will occur.
        detail.mEngaged = 1;
        a.setEngaged(true);
        return true;
      }
      final boolean e = mWorld.checkEngagement(source, false);
      detail.mEngaged = e ? 1 : 0;
      detail.mEngageIsCompulsory = true;
      a.setEngaged(e);
      return e;
    } else {
      return detail.mEngaged == 1;
    }
  }

  /**
   * Apply the rules of the game to test if the contents of cell <code>source</code>
   * are currently movable by the specified player.
   * @param player the player performing this request
   * @param source the cell to test
   * @return true if move is permitted
   */
  public boolean isMovable(final Wizard player, final int source) {
    final Actor a = actor(source);
    final AttentionDetail detail = getAttention(player, a); // destroy any existing focus
    if (a == null) {
      return false; // nothing to be moved
    } else if (a.isMoved()) {
      return false; // already moved
    } else if (!(a instanceof Monster)) {
      return false; // not moveable
    } else if (a.getState() != State.ACTIVE) {
      return false; // only active things can move
    } else if (a.getOwner() != player.getOwner()) {
      return false; // you don't own it
    } else if (a.get(Attribute.MOVEMENT) > 0) {
      return true;
    } else if (a.get(Attribute.COMBAT) == 0) {
      return false; // Has no movement AND no combat ability
    }
    // Left with the difficult case of monsters having no movement allowance,
    // e.g. ShadowWood.  These need to be let through if there is an adjacent
    // enemy that can be attacked.
    detail.mEngageIsCompulsory = true;
    return isEngaged(player, source);
  }

  /**
   * Convenience method to test if the cell at source contains something
   * which in theory is mountable by the given rider. It
   * does not take into consideration whether it is feasible for the
   * rider to actually reach the mount.
   * @param rider who is asking for the test
   * @param mountLocation cell to test
   * @return true if <code>mountLocation</code> contains a valid mount
   */
  public boolean isMountable(final Actor rider, final int mountLocation) {
    if (!(rider instanceof Rider) || rider.is(PowerUps.NO_MOUNT)) {
      return false;
    }
    final Actor a = actor(mountLocation);
    if (a == null || a.getState() != State.ACTIVE) {
      return false;
    } else if (a instanceof Meditation && ((Meditation) a).getMount() == null) {
      return true;
    } else if (a.getOwner() != rider.getOwner()) {
      return false;
    } else if (a instanceof Exit) {
      return ((Exit) a).isOpen() && ((Exit) a).getMount() == null;
    } else if (a instanceof Rideable) {
      return rider.is(PowerUps.RIDE) && ((Rideable) a).getMount() == null;
    } else if (a instanceof Conveyance) {
      return ((Conveyance) a).getMount() == null;
    }
    return false;
  }

  /**
   * Convenience method to test if the cell at source is permitted to
   * attack the cell at target. It does not take into consideration whether
   * it is feasible for the source to actually reach the target. This only
   * deals with real combat not the possibility of weapons fire.
   * @param source attacker
   * @param target defender
   * @param type type of combat
   * @return true if source can theoretically attack target
   */
  public boolean isAttackable(final int source, final int target, final int type) {
    return source != target && isAttackable(actor(source), target, type);
  }

  /**
   * Convenience method to test if the given actor is permitted to
   * attack the cell at target. It does not take into consideration whether
   * it is feasible for the source to actually reach the target. This only
   * deals with real combat not the possibility of weapons fire.
   * @param attacker the attacker
   * @param target defender
   * @param type type of combat
   * @return true if source can theoretically attack target
   */
  public boolean isAttackable(final Actor attacker, final int target, final int type) {
    if (!(attacker instanceof Monster)) {
      return false; // only monsters can attack
    }
    if (attacker.getState() != State.ACTIVE) {
      return false; // attacker must be active
    }
    final Actor at = actor(target);
    if (at == null) {
      return false; // attackee must not be nothing
    }
    final State ats = at.getState();
    if (ats != State.ACTIVE && ats != State.ASLEEP) {
      return false; // attackee must be active or asleep
    }
    final int dcv;
    switch (type) {
      case CombatUtils.NORMAL:
        dcv = attacker.getDefault(Attribute.COMBAT);
        break;
      case CombatUtils.SPECIAL:
        dcv = attacker.getDefault(Attribute.SPECIAL_COMBAT);
        break;
      default:
        dcv = attacker.getDefault(Attribute.RANGED_COMBAT);
        break;
    }
    if (ats != State.ASLEEP) {
      final Team team = mWorld.getTeamInformation();
      // Core creatures "attack" both friends and enemies when in healing mode
      if (at.getRealm() != Realm.CORE || dcv >= 0) {
        if (team.getTeam(at) == team.getTeam(attacker) && dcv >= 0) {
          return false; // don't attack team mates unless healing
        } else if (team.getTeam(at) != team.getTeam(attacker) && dcv < 0) {
          return false; // don't heal enemies
        }
      }
    }
    // Special check for confidence
    final Wizard w = mWorld.getWizardManager().getWizard(attacker.getOwner());
    if (w != null && w.is(PowerUps.CONFIDENCE)) {
      return true;
    }
    if (attacker instanceof AttacksUndead && at.getRealm() == Realm.ETHERIC) {
      return true;
    }
    return Realm.realmCheck(attacker, at);
  }

  /**
   * Convenience method to test if the cell at source is permitted to
   * attack the cell at target. It does not take into consideration whether
   * it is feasible for the source to actually reach the target. This only
   * deals with real combat not the possibility of weapons fire.
   * @param source attacker
   * @param target defender
   * @return true if source can theoretically attack target
   */
  public boolean isAttackable(final int source, final int target) {
    return isAttackable(source, target, CombatUtils.NORMAL);
  }

  /**
   * Get the movement points available for the actor at the specified cell.
   * It returns the square of the movements available right at this time.
   * It returns 0 if the actor cannot be moved at this time. This ignores
   * any potential engagement issue which might prevent movement.
   * @param player who is asking for the test
   * @param source the cell to get squared movement points for
   * @return the squared distance of movement available
   */
  public int getSquaredMovementPoints(final Wizard player, final int source) {
    final Actor a = actor(source);
    final AttentionDetail detail = getAttention(player, a);
    if (a == null || a.isMoved()) {
      return 0; // request for movement points of non-Actor or already moved
    } else if (detail.mSquaredMovementPoints == AttentionDetail.UNKNOWN) {
      final int move = a.get(Attribute.MOVEMENT);
      detail.mSquaredMovementPoints = move > 0 ? move * move + move : 0;
    }
    return detail.mSquaredMovementPoints;
  }

  /**
   * This function pops the contents of <code>source</code> and pushes them
   * onto <code>target</code>. This is the simplest kind of move from
   * <code>source</code> to <code>target</code>.
   * @param source source cell
   * @param target target cell
   */
  private void simpleQuickMove(final int source, final int target) {
    notify(new CellEffectEvent(target, CellEffectType.MOVEMENT_EVENT));
    final Actor current = mWorld.getCell(source).pop();
    if (current != null) {
      mWorld.getCell(target).push(current);
    }
  }

  /**
   * Actor at <code>source</code> enter meditation at <code>target</code>.
   * No checking is performed. The conditions are assumed to have already
   * been met.
   * @param source cell containing actor about to meditate
   * @param target cell containing meditation
   */
  private void mountConveyance(final int source, final int target) {
    ((Conveyance) actor(target)).setMount(mWorld.getCell(source).pop());
  }

  /** Request is not permitted */
  public static final int ILLEGAL = -1;
  /** Request was accepted and actioned */
  public static final int OK = 0;
  /** Request was accepted but failed because destination was invulnerable */
  public static final int INVULNERABLE = 1;
  /** Request was accepted but combat attempt was unsuccessful */
  public static final int COMBAT_FAILED = 2;
  /** Request was accepted but further movement is possible */
  public static final int PARTIAL = 3;

  /**
   * Player is requesting that the content of cell <code>source</code>
   * be moved to cell <code>target</code>. If the operation is successfully
   * carried out then true is returned, otherwise false is returned.
   * The operation may fail for any number of reasons, such as: player
   * does not own cell at source, it has already moved this turn, it is
   * engaged, it is not something than can be moved. In carrying out the
   * move it may be necessary to do mounting or combat.
   * @param player the player performing this move
   * @param source cell to moved
   * @param target where to move to
   * @return status indicate result of move
   */
  public int move(final Wizard player, final int source, final int target) {
    // quick exit for trivial move or attempts to move off the board
    if (source == target || source < 0 || target < 0) {
      return ILLEGAL;
    }
    final int size = mWorld.size();
    if (source >= size || target >= size || !isMovable(player, source)) {
      return ILLEGAL;
    }
    // get the detail information for the request
    final Actor a = actor(source);
    if (!(a instanceof Monster)) {
      return ILLEGAL; // only monsters can move
    }
    final Monster m = (Monster) a;
    final AttentionDetail detail = getAttention(player, m);
    assert detail != null : "AttentionDetail structure was null";
    // reject if movement is not possible due to lack of movement points
    int squaredMovementPoints = getSquaredMovementPoints(player, source);
    assert squaredMovementPoints >= 0 : "Negative movement points";
    // handle engagement, complicated by the case of 0 movement points
    if (squaredMovementPoints == 0) {
      detail.mEngageIsCompulsory = true;
    }
    final boolean isEngaged = isEngaged(player, source);
    if (isEngaged) {
      squaredMovementPoints = Math.max(squaredMovementPoints, 2);
    }
    // compute squared distance between source and target
    final int delta = mWorld.getSquaredDistance(source, target);
    // quick reject if delta exceeds available points
    if (delta > squaredMovementPoints) {
      return ILLEGAL;
    }
    // immediate reject if engaged and trying to move more than 1 cell
    if (isEngaged && delta > 2) {
      return ILLEGAL;
    }
    // unless actor can fly also need to reject movement greater than 1 cell
    final boolean flying = m.is(PowerUps.FLYING);
    if (delta > 2 && !flying) {
      return ILLEGAL;
    }
    if (flying) {
      final Set<Cell> reachable = mReachable.getReachableFlying(source, Math.sqrt(squaredMovementPoints));
      if (!reachable.contains(mWorld.getCell(target))) {
        return ILLEGAL;
      }
    }
    notify(new CellEffectEvent(source, CellEffectType.HIGHLIGHT_EVENT));
    final Actor ta = actor(target);
    if (!isEngaged && isMountable(m, target)) {
      mountConveyance(source, target);
      return finishSuccessfulMove(player, m);
    }
    if (ta instanceof Gollop && ta.getOwner() == m.getOwner() && !(m instanceof Wizard)) {
      Sound.getSoundEngine().playwait("chaos/resources/sound/misc/merge", Sound.SOUND_INTELLIGENT);
      Merge.merge((Gollop) ta, mWorld.getCell(source));
      return finishSuccessfulMove(player, m);
    }
    if (isAttackable(source, target)) {
      m.setMoved(true);
      if (ta instanceof Killer) {
        final Cell tcell = mWorld.getCell(target);
        tcell.reinstate(); // remove the killer tile
        simpleQuickMove(source, target); // move the creature
        tcell.reinstate(); // kill the creature
        final Actor ka = actor(target);
        if (ka != null && ka.getState() == State.DEAD) {
          tcell.pop(); // get rid of any corpse
        }
        return finishSuccessfulMove(player, m);
      }
      // move permitted, this is COMBAT
      final int cs = CombatUtils.performCombat(mWorld, player, m, mWorld.getCell(source), mWorld.getCell(target), CombatUtils.NORMAL);
      switch (cs) {
        case CombatUtils.INVULNERABLE:
          return INVULNERABLE;
        case CombatUtils.COMBAT_FAILED:
          return COMBAT_FAILED;
        default:
          if (cs == CombatUtils.COMBAT_OK) {
            // cell was not vacated
            return COMBAT_FAILED;
          }
          break;
      }
      if (m.get(Attribute.MOVEMENT) > 0) {
        // exclude movement for items with no movement points
        simpleQuickMove(source, target);
      }
      return finishSuccessfulMove(player, m);
    }
    // attempt to move to non-active cell while engaged
    if (isEngaged) {
      notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
      return ILLEGAL;
    }
    // handle movement onto empty cells and corpses
    if (ta == null || ta.getState() == State.DEAD) {
      final int smp = detail.mSquaredMovementPoints;
      // generate a message saying what is happening
      final StringBuilder sb = new StringBuilder().append(NameUtils.getTextName(m)).append(": movement points = ").append(DoubleUtils.NF2.format(Math.sqrt(smp)));
      if (flying) {
        sb.append(" (flying)");
      }
      notify(new TextEvent(sb.toString()));
      simpleQuickMove(source, target);
      notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
      detail.mEngaged = AttentionDetail.UNKNOWN;
      if (flying || (detail.mSquaredMovementPoints += delta - (int) (Math.sqrt((delta << 2) * smp) + 0.5)) <= 0) {
        // at end of move need to test if we are now engaged to enemy
        if (isEngaged(player, target)) {
          // curtail movement points, this ensures that even if the movement
          // points were just exhausted that the combat can proceed
          detail.mSquaredMovementPoints = 3;
          return PARTIAL;
        } else {
          return finishSuccessfulMove(player, m);
        }
      }
      return PARTIAL;
    }
    if (isMountable(player, target) && m instanceof Rider) {
      // player is mounting valid mount
      if (ta instanceof Conveyance) {
        ((Conveyance) ta).setMount(mWorld.getCell(source).pop());
      }
      return finishSuccessfulMove(player, m);
    }
    // anything else is presumeably illegal
    notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
    return ILLEGAL;
  }

  /**
   * Check if the Monster at source is capable of shooting.
   * @param player player currently moving
   * @param source cell to check
   * @return true if can shoot
   */
  public boolean isShootable(final Wizard player, final int source) {
    return ShootingRules.isShootable(player, actor(source));
  }

  /**
   * Check if the mount at source is capable of shooting.
   * @param player player currently moving
   * @param source cell to check
   * @return true if can shoot
   */
  public boolean isShootableConveyance(final Wizard player, final int source) {
    return ShootingRules.isShootableConveyance(player, actor(source));
  }

  /**
   * Test for shooting. The object at <code>source</code> is attempting
   * to shoot <code>target</code>. This function returns true if the shot
   * is possible.
   * @param player player moving
   * @param source location of object shooting
   * @param target location of target
   * @return true if shot is possible
   */
  public boolean isShootable(final Wizard player, final int source, final int target) {
    return target >= 0 && target < mWorld.size()
      && isShootable(player, source)
      && ShootingRules.isShootable(source, target, (Monster) mWorld.actor(source), mWorld, mLOS);
  }

  /**
   * Test for shooting. The object mounted at <code>source</code> is attempting
   * to shoot <code>target</code>. This function returns true if the shot
   * is possible.
   * @param player player moving
   * @param source location of object shooting
   * @param target location of target
   * @return true if shot is possible
   */
  public boolean isShootableConveyance(final Wizard player, final int source, final int target) {
    return target >= 0 && target < mWorld.size()
      && isShootableConveyance(player, source)
      && ShootingRules.isShootable(source, target, (Monster) ((Conveyance) mWorld.actor(source)).getMount(), mWorld, mLOS);
  }

  private void shoot(final Wizard player, final int source, final int target, final Monster m) {
    notify(new WeaponEffectEvent(source, target, WeaponEffectType.RANGED_COMBAT_EVENT, m));
    final Actor ta = mWorld.actor(target);
    if (ta != null) {
      CombatUtils.performCombat(mWorld, player, m, mWorld.getCell(source), mWorld.getCell(target), CombatUtils.RANGED);
    }
  }

  /**
   * Perform ranged combat.
   * @param player player currently moving
   * @param source actor shooting
   * @param target target of shot
   * @return status information
   */
  public int shoot(final Wizard player, final int source, final int target) {
    if (!isShootable(player, source, target)) {
      return ILLEGAL;
    }
    shoot(player, source, target, (Monster) mWorld.actor(source));
    // Because it is possible that actor may have been promoted or killed
    // we reget the actor.  Most times this will be the same monster as above.
    final Actor a = mWorld.actor(source);
    if (a instanceof Monster) {
      ((Monster) a).incrementShotsMade();
    }
    return OK;
  }

  /**
   * Perform ranged combat from a conveyance.
   * @param player player currently moving
   * @param source actor containing the mount
   * @param target target of shot
   * @return status information
   */
  public int shootFromConveyance(final Wizard player, final int source, final int target) {
    if (!isShootableConveyance(player, source, target)) {
      return ILLEGAL;
    }
    final Monster m = (Monster) ((Conveyance) mWorld.actor(source)).getMount();
    shoot(player, source, target, m);
    // Because it is possible that actor may have been promoted or killed
    // we reget the actor.  Most times this will be the same monster as above.
    // Tricky because actual conveyance may have died.
    Actor a = mWorld.actor(source);
    if (a != m && a instanceof Conveyance) {
      a = ((Conveyance) a).getMount();
    }
    if (a instanceof Monster) {
      ((Monster) a).incrementShotsMade();
    }
    return OK;
  }

  /**
   * Moves a mount from this cell from the mount position to the top
   * of the cell.  This is done as an interim step in dismounting.
   * This function assumes the mount does indeed exist and that all
   * types are values are already correct.
   * @param source source cell
   */
  private void dismountLocally(final int source) {
    // Because of Bug#304 care is needed here.  In the situation where a mounted
    // wizard attacks and kills the owner of the conveyance the wizard is riding,
    // then the dismount will have already happened by the time we get here.
    if (actor(source) instanceof Conveyance) {
      final Conveyance c = (Conveyance) actor(source);
      final Actor mount = c.getMount();
      c.setMount(null);
      mWorld.getCell(source).push(mount);
    }
  }

  /**
   * Player is requesting that the mount of cell <code>source</code> be
   * dismounted to cell <code>target</code>. The return code indicates if the
   * dismount has been successful.  Note this also performs full combat
   * resolution for mounted combat.
   * @param player the player performing this move
   * @param source cell containing mount
   * @param target where to move to
   * @return status indicate result of move
   */
  public int dismount(final Wizard player, final int source, final int target) {
    // Check some basic conditions for dismounting
    if (source == target || source < 0 || target < 0) {
      return ILLEGAL;
    }
    final int size = mWorld.size();
    if (source >= size || target >= size || isEngaged(player, source)) {
      return ILLEGAL;
    }
    // Check it really is a mount and mounted and the mount has not already moved.
    final Cell scell = mWorld.getCell(source);
    final Actor mount = scell.getMount();
    if (!(mount instanceof Monster) || mount.isMoved()) {
      return ILLEGAL;
    }
    final Monster m = (Monster) mount;
    final AttentionDetail detail = getAttention(player, m);
    final int delta = mWorld.getSquaredDistance(source, target);
    if (delta > 2) {
      return ILLEGAL;
    }
    notify(new CellEffectEvent(source, CellEffectType.HIGHLIGHT_EVENT));
    final Actor ta = actor(target);
    // Handle dismount into another meditation.
    if (isMountable(m, target)) {
      dismountLocally(source);
      mountConveyance(source, target);
      return finishSuccessfulMove(player, m);
    }
    // Handle dismount onto empty cells and corpses.
    if (ta == null || ta.getState() == State.DEAD) {
      dismountLocally(source);
      simpleQuickMove(source, target);
      notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
      // engagement is compulsory when dismounting next to something, but we don't
      // know for sure if anything is there
      detail.mEngaged = AttentionDetail.UNKNOWN;
      detail.mEngageIsCompulsory = true;
      // At end of move need to test if we are now engaged to enemy
      if (isEngaged(player, target)) {
        // Curtail movement points, this ensures that even if the movement
        // points were just exhausted that the combat can proceed
        detail.mSquaredMovementPoints = 3;
        return PARTIAL;
      } else {
        return finishSuccessfulMove(player, m);
      }
    }
    // Need to be careful to check the mount with respect to combat,
    // and not the current top contents. Do this by temporary swap in cell top
    scell.push(m);
    final boolean canAttack = isAttackable(source, target);
    scell.pop();
    if (canAttack) {
      m.setMoved(true);
      switch (CombatUtils.performCombat(mWorld, player, m, mWorld.getCell(source), mWorld.getCell(target), CombatUtils.NORMAL)) {
        case CombatUtils.INVULNERABLE:
          return INVULNERABLE;
        case CombatUtils.COMBAT_FAILED:
        case CombatUtils.COMBAT_OK: // cell was not vacated as a result
          return COMBAT_FAILED;
        default:
          dismountLocally(source);
          simpleQuickMove(source, target);
          return finishSuccessfulMove(player, m);
      }
    }
    // Handle dismounting into a conveyance other than meditation.
    if (isMountable(player, target) && m instanceof Wizard) {
      dismountLocally(source);
      ((Conveyance) ta).setMount(mWorld.getCell(source).pop());
      return finishSuccessfulMove(player, m);
    }
    // Anything else is presumeably illegal.
    notify(new CellEffectEvent(-1, CellEffectType.HIGHLIGHT_EVENT));
    return ILLEGAL;
  }
}
