package chaos.util;

import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.Conveyance;
import chaos.common.Dragon;
import chaos.common.FrequencyTable;
import chaos.common.IgnoreShot;
import chaos.common.Monster;
import chaos.common.Named;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.State;
import chaos.common.WakeOnFire;
import chaos.common.inanimate.ManaBattery;
import chaos.common.inanimate.Nuked;
import chaos.common.monster.Horse;
import chaos.common.monster.NamedDeity;
import chaos.common.monster.NamedSnake;
import chaos.common.monster.Thundermare;
import chaos.common.mythos.Horace;
import chaos.common.wizard.Wizard;

/**
 * Combat utility functions.
 * @author Sean A. Irvine
 */
public class CombatUtils extends DefaultEventGenerator {

  /** Combat failed. */
  public static final int COMBAT_FAILED = 0;
  /** Combat succeeded. */
  public static final int COMBAT_SUCCEEDED = 1;
  /** Combat killed the object, but the cell is not vacant. */
  public static final int COMBAT_OK = 2;
  /** Combat failed because target was invulnerable. */
  public static final int INVULNERABLE = -1;

  /** Normal combat */
  public static final int NORMAL = 0;
  /** Ranged combat */
  public static final int RANGED = 1;
  /** Special combat */
  public static final int SPECIAL = 2;

  private static final int DEITY_KILL_THRESHOLD = 25;

  /** Create a new instance of the given monster. */
  private static Monster horror(final Monster m) {
    final Monster rem = (Monster) FrequencyTable.instantiate(m.getClass());
    rem.setState(m.getState());
    rem.setOwner(m.getOwner());
    rem.setRealm(m.getRealm());
    rem.set(PowerUps.REINCARNATE, rem.reincarnation() != null && m.is(PowerUps.REINCARNATE) ? 1 : 0);
    return rem;
  }

  private static final int EXPERIENCE_GAIN = 4;
  private static final Attribute[] EXPERIENCE_ATTR = {
    Attribute.LIFE_RECOVERY, Attribute.MOVEMENT, Attribute.COMBAT, Attribute.RANGE,
    Attribute.RANGED_COMBAT, Attribute.SPECIAL_COMBAT, Attribute.LIFE
  };
  private static final Attribute[] EXPERIENCE_ATTR_REDUCED = {
    Attribute.LIFE_RECOVERY, Attribute.COMBAT, Attribute.LIFE,
  };

  private static void experience(final Actor a, final Cell source) {
    if (a instanceof Named) {
      source.notify(new CellEffectEvent(source, CellEffectType.EXPERIENCE, a));
      source.notify(new CellEffectEvent(source, CellEffectType.REDRAW_CELL));
      for (int k = 0; k < EXPERIENCE_GAIN; ++k) {
        final Attribute attr = EXPERIENCE_ATTR[Random.nextInt(EXPERIENCE_ATTR.length)];
        final Attribute incAttr = a.get(attr) == 0 ? EXPERIENCE_ATTR_REDUCED[Random.nextInt(EXPERIENCE_ATTR_REDUCED.length)] : attr;
        a.increment(incAttr, 1);
      }
    }
  }

  /**
   * Promote the given actor to an actor of the specified class.  For most
   * attributes this retains the best option.
   * @param a actor to promote
   * @param c cell to promote
   * @param promoClass promotion
   * @return the promotion
   */
  public static Actor promote(final Actor a, final Cell c, final Class<? extends Castable> promoClass) {
    final Actor p = (Actor) FrequencyTable.instantiate(promoClass);
    p.setOwner(a.getOwner());
    p.setState(a.getState());
    if (a.getRealm().ordinal() > p.getRealm().ordinal()) {
      p.setRealm(a.getRealm()); // keep the better realm
    }
    p.setMoved(a.isMoved());
    for (final Attribute attr : Attribute.values()) {
      p.set(attr, max(a.get(attr), p.get(attr)));
    }
    for (final PowerUps pu : PowerUps.values()) {
      p.set(pu, max(a.get(pu), p.get(pu)));
    }
    p.setKillCount(a.getKillCount());
    if (p instanceof Monster) {
      final Monster mp = (Monster) p;
      if (a instanceof Monster) {
        mp.setShotsMade(((Monster) a).getShotsMade());
      }
      if (mp.reincarnation() == null) {
        mp.set(PowerUps.REINCARNATE, 0); // In case we promote to something that cannot reincarnate
      }
    }
    if (a instanceof Conveyance) {
      // If p is not a mount then truly bad things will happen.  There is a test
      // to make sure this is validated in the AbstractMonsterTest
      ((Conveyance) p).setMount(((Conveyance) a).getMount());
    }
    c.pop();
    c.push(p);
    return p;
  }

  /**
   * Examine adjacent cells for possible places to dump the horror.
   * @param m monster that is a horror
   * @param target current c
   */
  private static void handleHorror(final Monster m, final Cell target) {
    int c = 1;
    Cell insertCell = null;
    for (final Cell cell : Chaos.getChaos().getWorld().getCells(target.getCellNumber(), 1, 1, false)) {
      final Actor a = cell.peek();
      if ((a == null || a.getState() == State.DEAD) && Random.nextInt(c++) == 0) {
        insertCell = cell;
      }
    }
    if (insertCell != null) {
      m.decrement(PowerUps.HORROR);
      insertCell.push(horror(m)); // insert horror at randomly selected cell
    } else if (m instanceof Horace) {
      m.decrement(PowerUps.HORROR);
    }
    m.set(Attribute.LIFE, 1);
  }

  private static boolean applyCombat(final Actor a, final Attribute attr, final int combat) {
    if (combat > 0) {
      return a.decrement(attr, combat);
    } else if (combat < 0) {
      a.increment(attr, -combat);
    }
    return false;
  }

  /**
   * Wizard <code>player</code> is trying to attack <code>target</code> using the
   * given monster. This routine assumes that the attack has already been validated;
   * that is, the realm constraints, team constraints, range checking, etc. have
   * all been checked elsewhere. A limited amount of checking is performed here.
   * Check which are done here include the handling of invulnerable wizards.
   * This routine also copes with negative combat (i.e. healing).
   *
   * Different statistics suffer from combat in different ways. In the
   * case of life, intelligence, and magical resistance any negative
   * value is instant death. For life a value of 0 is also death.
   *
   * Certain other statistics cannot sensibly be negative. The movement
   * allowance should not be negative, nor should the range for ranged
   * combat.
   *
   * Some stats (e.g. the recovery stats) can go negative.  Negative
   * values are effectively poisoning.
   * @param world the world
   * @param player who is making this move
   * @param attacker what is the attacker
   * @param source where is the attacker
   * @param target where is the defender
   * @param type what type of combat
   * @return status code for the combat
   */
  public static int performCombat(final World world, final Wizard player, final Monster attacker, final Cell source, final Cell target, final int type) {
    // The attacker is given as an Actor rather than a cell, because the
    // attacker may actually be mounted or in a meditation.
    final Actor ta = target.peek();
    assert ta != null : "Attempted combat in empty cell";
    if (ta.getState() == State.DEAD || ta instanceof Nuked) {
      if (type == RANGED && ta instanceof WakeOnFire && attacker instanceof Dragon) {
        // Resurrect troll, phoenix
        ta.setOwner(attacker.getOwner());
        Restore.restore(ta);
      }
      return COMBAT_FAILED;
    }
    if (type == RANGED && ta instanceof IgnoreShot) {
      return COMBAT_FAILED;
    }
    // check for attack against invulnerable wizard
    if (type == RANGED && ta.is(PowerUps.REFLECT)) {
      // This next check is critical to preventing infinite loops if a wizard
      // with a bow and reflector shoots a wizard with a reflector!
      if (player != null) {
        // Effectively need to shoot thyself
        performCombat(world, null, attacker, source, source, RANGED);
        return COMBAT_FAILED;
      }
    } else if (ta.is(PowerUps.INVULNERABLE)) {
      return INVULNERABLE;
    }
    // compute the strength of the combat and the applicable stats
    int combat;
    final Attribute attributeToAttack;
    switch (type) {
      case NORMAL:
        combat = attacker.get(Attribute.COMBAT);
        attributeToAttack = attacker.getCombatApply();
        break;
      case RANGED:
        combat = attacker.get(Attribute.RANGED_COMBAT);
        attributeToAttack = attacker.getRangedCombatApply();
        break;
      case SPECIAL:
        combat = attacker.get(Attribute.SPECIAL_COMBAT);
        attributeToAttack = attacker.getSpecialCombatApply();
        break;
      default:
        throw new IllegalArgumentException();
    }
    // modify combat according to power-ups
    final Monster tm = ta instanceof Monster ? (Monster) ta : null;
    if (player != null) {
      if (player.is(PowerUps.BATTLE_CRY)) {
        combat *= 2;
      }
      if (tm != null && tm.is(PowerUps.CLOAKED)) {
        combat /= 2;
      }
    }
    // let viewers know what kind of attack is in progress
    if (type != SPECIAL) {
      target.notify(new AttackCellEffectEvent(target.getCellNumber(), combat, attacker, type));
    }
    boolean kill = false;
    switch (attributeToAttack) {
      // deal with the three potentially fatal cases first
      case INTELLIGENCE:
        if (tm != null) {
          kill = applyCombat(ta, attributeToAttack, combat);
        }
        break;
      case MAGICAL_RESISTANCE:
      case LIFE:
        kill = applyCombat(ta, attributeToAttack, combat);
        break;
      case LIFE_RECOVERY:
      case MOVEMENT:
      case COMBAT:
      case SPECIAL_COMBAT:
      case AGILITY:
      case INTELLIGENCE_RECOVERY:
      case MAGICAL_RESISTANCE_RECOVERY:
        applyCombat(ta, attributeToAttack, combat);
        break;
      case RANGE:
      case RANGED_COMBAT:
        // These two always applied in tandem
        if (ta.get(Attribute.RANGE) != 0) {
          applyCombat(ta, Attribute.RANGE, combat);
          applyCombat(ta, Attribute.RANGED_COMBAT, combat);
        }
        break;
      default:
        break;
    }
    // deal with the death of the target
    if (kill && !lifeLeechRescue(world, target, attributeToAttack)) {
      CastUtils.handleScoreAndBonus(player, ta, source);
      if (tm != null && tm.is(PowerUps.HORROR)) {
        handleHorror(tm, target);
        return COMBAT_FAILED;
      }
      experience(attacker, source);
      final int killCount = attacker.incrementKillCount();
      if (attacker instanceof Promotion) {
        final Promotion p = (Promotion) attacker;
        // Second condition is only for extra safety
        if (killCount >= p.promotionCount() && source.peek() == attacker) {
          source.notify(new CellEffectEvent(source, CellEffectType.MONSTER_CAST_EVENT, promote(attacker, source, p.promotion())));
          source.notify(new CellEffectEvent(source, CellEffectType.REDRAW_CELL));
        }
      } else if (attacker instanceof Horse && ((Horse) attacker).getMount() != null) {
        // Mounted horse, promote to thundermare
        source.notify(new CellEffectEvent(source, CellEffectType.MONSTER_CAST_EVENT, promote(attacker, source, Thundermare.class)));
      }
      if (killCount == DEITY_KILL_THRESHOLD && player != null) { // Must be == to avoid getting one for each extra kill
        final CastableList castableList = player.getCastableList();
        if (castableList != null) {
          world.notify(new AudioEvent(source.getCellNumber(), player, "laugh", attacker));
          castableList.add(Random.nextBoolean() ? new NamedDeity() : new NamedSnake());
        }
      }
      return target.reinstate() ? COMBAT_SUCCEEDED : COMBAT_OK;
    }
    return COMBAT_FAILED;
  }

  private static boolean lifeLeechRescue(final World world, final Cell targetCell, final Attribute attr) {
    final Actor target = targetCell.peek();
    if (!target.is(PowerUps.LIFE_LEECH)) {
      return false;
    }
    if (attr != Attribute.LIFE) {
      return false; // todo handle e.g. magical resistance, intelligence
    }
    final int owner = target.getOwner();
    final List<Cell> leechVictims = new ArrayList<>();
    for (final Cell c : world) {
      final Actor a = c.peek();
      if (a != null && a.getOwner() == owner && a.getState() == State.ACTIVE && !a.is(PowerUps.LIFE_LEECH)) {
        leechVictims.add(c);
      }
    }
    final List<Cell> killedAsResultOfLeech = new ArrayList<>();
    final List<Cell> undraw = new ArrayList<>();
    final int tc = targetCell.getCellNumber();
    int deficit = target.get(attr);
    assert deficit <= 0;
    while (deficit <= 0) {
      if (leechVictims.isEmpty()) {
        break; // Insufficient creations to survive
      }
      final Cell victim = leechVictims.get(Random.nextInt(leechVictims.size()));
      world.notify(new WeaponEffectEvent(tc, victim.getCellNumber(), WeaponEffectType.LINE, target, attr));
      undraw.add(victim);
      target.increment(attr, 1);
      ++deficit;
      if (victim.peek().decrement(attr, 1)) {
        leechVictims.remove(victim);
        killedAsResultOfLeech.add(victim);
      }
    }
    Sleep.sleep(30);
    for (final Cell u : undraw) {
      world.notify(new WeaponEffectEvent(tc, u.getCellNumber(), WeaponEffectType.UNLINE, target));
    }
    for (final Cell died : killedAsResultOfLeech) {
      died.reinstate();
    }
    return deficit > 0; // Test if we were able to survive as a result of the leeching
  }

  private static void handleManaBattery(final World world, final int cell) {
    final HashSet<Integer> seen = new HashSet<>();
    for (final Cell c : world.getCells(cell, 1, 1, false)) {
      final Actor a = c.peek();
      if (a != null && a.getState() == State.ACTIVE) {
        seen.add(a.getOwner());
      }
    }
    for (final int player : seen) {
      if (player != Actor.OWNER_INDEPENDENT) {
        final Wizard wizard = world.getWizardManager().getWizard(player);
        if (wizard != null) {
          wizard.addBonus(1, 1);
        }
      }
    }
  }

  private static boolean isHitBySpecialCombat(final MoveMaster mm, final Monster attacker, final Cell target, final Attribute attr) {
    if (!mm.isAttackable(attacker, target.getCellNumber(), SPECIAL)) {
      return false; // e.g. cannot attacks undeads from the living
    }
    final Actor a = target.peek();
    if (a instanceof IgnoreShot) {
      return false; // pits, nuked squares
    }
    if (a.is(PowerUps.INVULNERABLE)) {
      return false;
    }
    if (attr == Attribute.LIFE || attr == Attribute.LIFE_RECOVERY
      || attr == Attribute.MAGICAL_RESISTANCE || attr == Attribute.MAGICAL_RESISTANCE_RECOVERY) {
      return true; // All actors can be attacked in these attributes
    }
    return a instanceof Monster;
  }

  private static void doSpecialCombat(final World world, final MoveMaster mm, final Cell c, final Monster m, final int sc) {
    final Attribute scApply = m.getSpecialCombatApply();
    if (sc != 0) {
      // Found a creature with nonzero special combat.  Need to examine all
      // adjacent cells for potential targets.
      c.notify(new CellEffectEvent(c, CellEffectType.HIGHLIGHT_EVENT));
      c.notify(new TextEvent(NameUtils.getTextName(m) + ": Special Combat"));
      final int cn = c.getCellNumber();
      final int distance = Math.max(1, m.get(PowerUps.TALISMAN));
      final Set<Cell> adjacent = world.getCells(cn, 1, distance, false);
      final Set<Cell> targets = new HashSet<>();
      // Filter out the cells that can actually be attacked.
      for (final Cell t : adjacent) {
        // this needs to take better account of power ups
        if (isHitBySpecialCombat(mm, m, t, scApply)) {
          targets.add(t);
        }
      }
      if (!targets.isEmpty()) {
        world.notify(new PolycellAttackEvent(targets, m, sc));
        final WizardManager wm = world.getWizardManager();
        final int pp = m.getOwner();
        for (final Cell t : targets) {
          assert t != null;
          if (t.peek() != null) {
            final Wizard wiz = wm.getWizard(pp);
            performCombat(world, wiz, m, c, t, CombatUtils.SPECIAL);
          }
        }
        world.notify(new PolycellEffectEvent(targets, CellEffectType.REDRAW_CELL));
      }
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
    }
  }

  /**
   * Perform special combat.
   * @param world world where combat is to occur
   * @param mm movement rules checker
   */
  public static void performSpecialCombat(final World world, final MoveMaster mm) {
    // Cells are considered in a random order, to prevent any specific player
    // getting an advantage by being at the top-left of the screen
    for (final Iterator<Cell> i = world.randomIterator(); i.hasNext(); ) {
      final Cell c = i.next();
      final int cn = c.getCellNumber();
      final Actor a = c.peek();
      if (a instanceof ManaBattery && a.getState() == State.ACTIVE) {
        handleManaBattery(world, cn);
      } else if (a instanceof Monster && a.getState() != State.DEAD) {
        final Monster m = (Monster) a;
        final int sc = m.get(Attribute.SPECIAL_COMBAT);
        doSpecialCombat(world, mm, c, m, sc);
      }
      // Cascade down to mounted creatures
      if (a instanceof Conveyance) {
        final Actor mount = ((Conveyance) a).getMount();
        if (mount instanceof Monster) { // should always be true
          final int sc = mount.get(Attribute.SPECIAL_COMBAT) / 2;
          doSpecialCombat(world, mm, c, (Monster) mount, sc);
        }
      }
    }
  }
}
