package chaos.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.FrequencyTable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Singleton;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import irvine.math.IntegerUtils;

/**
 * Casting utility functions.
 *
 * @author Sean A. Irvine
 */
public final class CastUtils {

  private CastUtils() { }

  /**
   * Test if the content of the given cell could be mutated.
   * @param cell cell to test
   * @return true if the cell can be mutated
   */
  public static boolean isMutatable(final Cell cell) {
    return cell != null && cell.peek() != null && !cell.contains(Wizard.class) && cell.peek().getState() != State.DEAD;
  }

  /**
   * Mutate the content of a cell.
   * @param cell cell to mutate
   * @return true if mutation was successful
   */
  public static boolean mutate(final Cell cell) {
    if (isMutatable(cell)) {
      final Actor current = cell.peek();
      final Class<? extends Actor> curClass = current.getClass();
      Actor a;
      // Make sure the replacement is a different creature.  Note this would
      // infinite loop in any game with only one creature type.  Also need to
      // skip the cat lord etc. to avoid getting two of them.
      do {
        a = FrequencyTable.DEFAULT.getUniformRandomActor();
      } while (a.getClass() == curClass || a instanceof Singleton);
      a.setState(current.getState());
      a.setOwner(current.getOwner());
      // Copy over all power ups
      for (final PowerUps p : PowerUps.values()) {
        a.increment(p, current.get(p));
      }
      if (a instanceof Monster && ((Monster) a).reincarnation() == null) {
        a.set(PowerUps.REINCARNATE, 0);
      }
      cell.pop();
      cell.push(a);
      return true;
    } else {
      return false;
    }
  }

  private static void castInanimate(final Actor what, final Caster caster, final Cell where, final Cell casterCell, final WeaponEffectType type) {
    if (where != null && caster != null) {
      what.setOwner(caster.getOwner()); // this must be before events for sound to work
      if (casterCell != null) {
        where.notify(new WeaponEffectEvent(casterCell, where, type, what));
      }
      where.notify(new CellEffectEvent(where, CellEffectType.MONSTER_CAST_EVENT, what));
      // Need to be careful when inserting onto growths
      if (where.peek() instanceof Growth) {
        while (where.pop() != null) {
          // do nothing
        }
      }
      where.push(what);
      where.notify(new CellEffectEvent(where, CellEffectType.REDRAW_CELL));
    }
  }

  /**
   * Provides an implementation of the common part of meditation casting.
   *
   * @param what what is being cast
   * @param caster who is casting
   * @param where where we are casting
   * @param casterCell where the caster is
   */
  public static void castMeditation(final Actor what, final Caster caster, final Cell where, final Cell casterCell) {
    castInanimate(what, caster, where, casterCell, WeaponEffectType.TREE_CAST_EVENT);
  }

  /**
   * Provides an implementation of the common part of tree casting.
   *
   * @param what what is being cast
   * @param caster who is casting
   * @param where where we are casting
   * @param casterCell where the caster is
   */
  public static void castTree(final Actor what, final Caster caster, final Cell where, final Cell casterCell) {
    castInanimate(what, caster, where, casterCell, WeaponEffectType.TREE_CAST_EVENT);
  }

  /**
   * Provides an implementation of the common part of wall casting.  Does the
   * generating of events, handles casting onto growths, and should be safe
   * to null parameters.
   *
   * @param what what is being cast
   * @param caster who is casting
   * @param where where we are casting
   * @param casterCell where the caster is
   */
  public static void castStone(final Actor what, final Caster caster, final Cell where, final Cell casterCell) {
    castInanimate(what, caster, where, casterCell, WeaponEffectType.STONE_CAST_EVENT);
  }

  /**
   * Provide a simple intrinsic value of a actor.  Uses default life rather than
   * life, so that the value makes sense for dead and injured creatures.  This
   * function can be used by the AI to evaluate the threat level and/or
   * usefulness of a cell.  Null actors have a score of 0.
   *
   * @param a actor to get score for
   * @return actor score
   */
  public static int score(final Actor a) {
    int s = 0;
    if (a != null) {
      s += a.getDefault(Attribute.LIFE);
      if (a instanceof Monster) {
        final Monster m = (Monster) a;
        s += m.get(Attribute.COMBAT) + m.get(Attribute.RANGED_COMBAT) + m.get(Attribute.SPECIAL_COMBAT);
        if (m instanceof Wizard) {
          s <<= 2;
        }
      }
    }
    return s;
  }

  /**
   * Provide a simple intrinsic value of a cell.  Uses default life rather than
   * life, so that the value makes sense for dead and injured creatures.  This
   * function can be used by the AI to evaluate the threat level and/or
   * usefulness of a cell.  Empty cells have a score of 0.
   *
   * @param c cell to get score for
   * @return cell score
   */
  public static int score(final Cell c) {
    return c == null ? 0 : score(c.peek());
  }

  /**
   * A cell filter which given a set of targets delete all those entries
   * which are not members of the indicated team.  Dead and empty cells
   * are also removed.
   *
   * @param targets cells to filter
   * @param t the team to retain
   * @param teams the teams
   * @return friends
   */
  public static Set<Cell> keepFriends(final Set<Cell> targets, final int t, final Team teams) {
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a == null || teams.getTeam(a) != t || a.getState() == State.DEAD) {
        i.remove();
      }
    }
    return targets;
  }

  /**
   * A cell filter which given a set of targets delete all those entries
   * which are members of the indicated team.  Dead and empty cells are
   * also removed.
   *
   * @param targets cells to filter
   * @param t the team to remove
   * @param teams the teams
   * @return friends
   */
  public static Set<Cell> keepEnemies(final Set<Cell> targets, final int t, final Team teams) {
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a == null || teams.getTeam(a) == t || a.getState() == State.DEAD) {
        i.remove();
      }
    }
    return targets;
  }

  /**
   * Filter out any wizards.
   * @param targets cells to filter
   * @return friends
   */
  public static Set<Cell> dropWizards(final Set<Cell> targets) {
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a instanceof Wizard) {
        i.remove();
      }
    }
    return targets;
  }

  /**
   * Filter out targets with a particular power up.
   * @param targets cells to filter
   * @param pu power up to be dropped
   * @return remaining targets
   */
  public static Set<Cell> dropPowerUp(final Set<Cell> targets, final PowerUps pu) {
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a != null && a.is(pu)) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Filter out any wizards or conveyed wizards.
   * @param targets cells to filter
   * @return friends
   */
  public static Set<Cell> dropWizardsOrConveyedWizards(final Set<Cell> targets) {
    dropWizards(targets);
    for (final Iterator<Cell> i = targets.iterator(); i.hasNext();) {
      final Actor a = i.next().peek();
      if (a instanceof Conveyance && ((Conveyance) a).getMount() instanceof Wizard) {
        i.remove();
      }
    }
    return targets;
  }

  /**
   * Retain the highest scoring targets from the specified set. Only
   * the best equal targets are retained, with best scored by the
   * <code>CastUtils.score</code> function.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> keepHighestScoring(final Set<Cell> targets) {
    // Find highest scoring targets
    int sc = 0;
    for (final Cell c : targets) {
      final int s = score(c);
      if (s > sc) {
        sc = s;
      }
    }
    // Eliminate everything with a lower score
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      if (score(it.next()) < sc) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Retain only awake actors.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> keepAwake(final Set<Cell> targets) {
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a == null || a.getState() != State.ACTIVE) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Retain the fastest targets from the specified set.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> keepFastest(final Set<Cell> targets) {
    int sc = 0;
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a instanceof Monster) {
        final int s = a.get(Attribute.MOVEMENT);
        if (s > sc) {
          sc = s;
        }
      }
    }
    // Eliminate everything with a lower score
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (!(a instanceof Monster) || a.get(Attribute.MOVEMENT) < sc) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * If some targets are animate, then keep only the animates, otherwise
   * keep all the current targets.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> preferAnimates(final Set<Cell> targets) {
    boolean foundAnimate = false;
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a instanceof Monster && !(a instanceof Growth) && !(a instanceof Inanimate)) {
        foundAnimate = true;
        break;
      }
    }
    if (foundAnimate) {
      for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
        final Actor a = it.next().peek();
        if (a == null || a instanceof Growth || a instanceof Inanimate) {
          it.remove();
        }
      }
    }
    return targets;
  }

  /**
   * Prefer inanimates.
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> preferInanimates(final Set<Cell> targets) {
    boolean foundInanimate = false;
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a instanceof Growth || a instanceof Inanimate) {
        foundInanimate = true;
        break;
      }
    }
    if (foundInanimate) {
      for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
        final Actor a = it.next().peek();
        if (!(a instanceof Growth) && !(a instanceof Inanimate)) {
          it.remove();
        }
      }
    }
    return targets;
  }

  /**
   * If some targets are awake, then keep only the awake, otherwise
   * keep all the current targets.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> preferAwake(final Set<Cell> targets) {
    boolean foundAwake = false;
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a.getState() != State.ASLEEP) {
        foundAwake = true;
        break;
      }
    }
    if (foundAwake) {
      for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
        final Actor a = it.next().peek();
        if (a.getState() == State.ASLEEP) {
          it.remove();
        }
      }
    }
    return targets;
  }

  /**
   * Retain the closest targets to the specified origin.
   *
   * @param targets targets to filter
   * @param caster the caster
   * @param world world containing cells
   * @return selected targets
   */
  public static Set<Cell> keepClosest(final Set<Cell> targets, final Caster caster, final World world) {
    int sc = Integer.MAX_VALUE;
    final Cell origin = world.getCell(caster);
    if (origin == null) {
      // Early exit if caster not found
      return targets;
    }
    final int o = origin.getCellNumber();
    for (final Cell c : targets) {
      final int d = world.getSquaredDistance(c.getCellNumber(), o);
      if (d < sc) {
        sc = d;
      }
    }
    // Eliminate everything with a bigger distance
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final int d = world.getSquaredDistance(it.next().getCellNumber(), o);
      if (d > sc) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Update the score and bonus information including event generation.
   *
   * @param caster the caster (or cause)
   * @param a the actor that was killed
   * @param casterCell the location of the cause
   */
  public static void handleScoreAndBonus(final Actor caster, final Actor a, final Cell casterCell) {
    if (a != null && caster instanceof Wizard) {
      final Wizard w = (Wizard) caster;
      w.addScore(a.getDefault(Attribute.LIFE));
      if (a instanceof Bonus) {
        w.addBonus(1, ((Bonus) a).getBonus());
        if (casterCell != null) {
          casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.BONUS, w));
        }
      }
      if (a instanceof Wizard) {
        // Wizard killed, any bonuses it was due are transferred to killer
        final Wizard z = (Wizard) a;
        w.addBonus(z.getBonusSelect(), z.getBonusCount());
      }
    }
  }

  /**
   * Provide a score measuring the suitability of a target for subversion.
   * This is intended to be used by the AI for selecting subversion targets.
   * Stronger creatures are better targets.
   * Low intelligence creatures are easier targets.
   * Undeads, dragons, etc., have a slight preference, all else being equal.
   * Some quantization and random factors to increase randomness.
   *
   * @param a actor to get score for
   * @return actor score
   */
  public static int subversionScore(final Actor a) {
    if (!(a instanceof Monster) || a instanceof Wizard) {
      return 0;
    } else if (a instanceof Conveyance && ((Conveyance) a).getMount() != null) {
      return 0;
    }
    final ChaosProperties p = ChaosProperties.properties();
    final int combat = a.get(Attribute.COMBAT)
      + a.get(Attribute.RANGED_COMBAT);
    double s = a.get(Attribute.LIFE)
      + combat * p.getDoubleProperty("ai.subversion.combat.scale", 5)
      + a.get(Attribute.SPECIAL_COMBAT)
      + a.get(Attribute.MAGICAL_RESISTANCE) * p.getDoubleProperty("ai.subversion.mr.scale", 0.1)
      + Random.nextInt(p.getIntProperty("ai.subversion.randomize", 10));
    if (a instanceof Inanimate || a instanceof Growth) {
      s *= p.getDoubleProperty("ai.subversion.inanimate.scale", 0.3);
    } else if (a.getRealm() == Realm.ETHERIC) {
      s *= p.getDoubleProperty("ai.subversion.undead.scale", 1.7);
    } else if (a instanceof Caster) {
      s *= p.getDoubleProperty("ai.subversion.caster.scale", 4);
    } else if (a.getRealm() != Realm.MATERIAL) {
      s *= p.getDoubleProperty("ai.subversion.nonmaterial.scale", 1.2);
    } else if (combat <= 2) {
      s *= p.getDoubleProperty("ai.subversion.lowcombat.scale", 0.2);
    }
    final int scale = 255 - a.get(Attribute.INTELLIGENCE);
    s *= scale;
    s *= scale;
    s /= 50000; // quantization
    return 1 + (int) s;
  }

  /**
   * Provide a score measuring the suitability of a target for subversion.
   * This is intended to be used by the AI for selecting subversion targets.
   * Stronger creatures are better targets.
   * Low intelligence creatures are easier targets.
   * Undeads, dragons, etc., have a slight preference, all else being equal.
   * Some quantization and random factors to increase randomness.
   *
   * @param c cell to get score for
   * @return cell score
   */
  public static int subversionScore(final Cell c) {
    return c == null ? 0 : subversionScore(c.peek());
  }

  /**
   * Retain the highest scoring targets from the specified set. Only
   * the best equal targets are retained, with best scored by the
   * <code>CastUtils.score</code> function.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> keepHighestSubversionScoring(final Set<Cell> targets) {
    // Find highest scoring targets
    final Set<Cell> results = new HashSet<>();
    int sc = 0;
    for (final Cell c : targets) {
      final int s = subversionScore(c);
      if (s > sc) {
        results.clear();
        sc = s;
      }
      if (s == sc) {
        results.add(c);
      }
    }
    targets.clear();
    targets.addAll(results);
    return targets;
  }

  /**
   * Keep only targets not from the specified realm.
   * @param targets targets to filter
   * @param realm realm to keep
   * @return retained targets
   */
  public static Set<Cell> dropRealm(final Set<Cell> targets, final Realm realm) {
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a == null || a.getRealm() == realm) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Retain the sickest targets from the specified set.  That is, the
   * targets with the biggest deviation from it default life.
   *
   * @param targets targets to filter
   * @return selected targets
   */
  public static Set<Cell> keepSickest(final Set<Cell> targets) {
    int sc = 0;
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a != null) {
        final int s = a.getDefault(Attribute.LIFE) - a.get(Attribute.LIFE);
        if (s > sc) {
          sc = s;
        }
      }
    }
    // Eliminate everything with a lower score
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a == null || a.getDefault(Attribute.LIFE) - a.get(Attribute.LIFE) < sc) {
        it.remove();
      }
    }
    return targets;
  }

  /**
   * Retain the targets corresponding to the owners in the candidate set with
   * the greatest number of putative targets.  This is used by spells like
   * MassMorph where the number of targets is an important consideration.
   *
   * @param targets targets to filter
   * @param world the world
   * @param ignoredTeam targets in this team are ignored
   * @return selected targets
   */
  public static Set<Cell> keepMostFrequentOwner(final Set<Cell> targets, final World world, final int ignoredTeam) {
    int maxOwner = 0;
    int minOwner = 0;
    for (final Cell c : targets) {

      // Find range of owners to consider.
      final Actor a = c.peek();
      if (a != null) {
        final int o = a.getOwner();
        if (o > maxOwner) {
          maxOwner = o;
        } else if (o < minOwner) {
          minOwner = o;
        }
      }
    }

    // Compute number of targets by owner.
    final int[] countByOwners = new int[maxOwner + 1 - minOwner];
    for (final Cell c : targets) {
      final Actor a = c.peek();
      if (a != null) {
        countByOwners[a.getOwner() - minOwner]++;
      }
    }

    // Find biggest count for enemies.
    final Team teams = world.getTeamInformation();
    for (int k = 0; k < countByOwners.length; ++k) {
      if (teams.getTeam(k + minOwner) == ignoredTeam) {
        countByOwners[k] = -1;
      }
    }
    final int bestCount = IntegerUtils.max(countByOwners);

    // Remove everything else.
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext();) {
      final Actor a = it.next().peek();
      if (a == null || countByOwners[a.getOwner() - minOwner] != bestCount) {
        it.remove();
      }
    }
    return targets;
  }
}
