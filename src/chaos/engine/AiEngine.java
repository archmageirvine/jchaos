package chaos.engine;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.Reachable;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.FreeCastable;
import chaos.common.Killer;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.Mountable;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Rideable;
import chaos.common.Rider;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.Tree;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.Tempest;
import chaos.common.monster.Gollop;
import chaos.common.spell.Request;
import chaos.common.spell.Teleport;
import chaos.common.wizard.Wizard;
import chaos.selector.Selector;
import chaos.util.IntelligentWall;
import chaos.util.NameUtils;
import chaos.util.Random;
import chaos.util.TextEvent;

/**
 * The AI engine. An engine using simple rules to determine the actions to take.
 * @author Sean A. Irvine
 */
public class AiEngine implements PlayerEngine, Serializable {

  private static final boolean DUMP_AI_CAST_FAILURES = Boolean.parseBoolean(System.getProperty("chaos.ai.dump-cast-failures", "false"));
  /** Likelihood of walking into a killer tile.  Higher values mean less likely to walk into a pit. */
  private static final int PIT_BIAS_FACTOR = 40;
  protected final World mWorld;
  private final Reachable mReachable;
  protected final MoveMaster mMoveMaster;
  protected final CastMaster mCastMaster;
  protected final Team mTeam;

  /**
   * Create a new AI engine.
   * @param world the world
   * @param moveMaster the movement rules
   * @param castMaster the casting rules
   */
  public AiEngine(final World world, final MoveMaster moveMaster, final CastMaster castMaster) {
    mWorld = world;
    mReachable = new Reachable(world);
    mMoveMaster = moveMaster;
    mCastMaster = castMaster;
    mTeam = mWorld.getTeamInformation();
  }

  static Set<Cell> getLegalTargets(final World world, final CastMaster castMaster, final Caster caster, final int casterCell, final Castable c) {
    int range = c.getCastRange();
    if (caster instanceof Wizard) {
      range += caster.get(PowerUps.WAND);
    }
    world.notify(new TextEvent(NameUtils.getTextName(c) + " [" + range + "]"));
    // get all cells in range subject to LOS condition
    final boolean los = (c.getCastFlags() & Castable.CAST_LOS) != 0;
    final Set<Cell> potentialSet = world.getCells(casterCell, 0, range, los);
    // eliminate those which are illegal
    final Wizard wiz = world.getWizardManager().getWizard(caster);
    potentialSet.removeIf(cell -> !castMaster.isLegalCast(wiz, c, casterCell, cell.getCellNumber()));
    return potentialSet;
  }

  @Override
  public boolean cast(final Caster caster, final Castable c, final Cell cell) {
    // handle castables where no cell need be selected
    if (c instanceof FreeCastable) {
      mWorld.notify(new TextEvent(NameUtils.getTextName(c) + " [0]"));
      ((FreeCastable) c).cast(mWorld, caster, cell);
    } else if (c != null) {
      final int casterCell = cell.getCellNumber();
      final Set<Cell> potentialSet = getLegalTargets(mWorld, mCastMaster, caster, casterCell, c);
      if (c instanceof Blocker) {
        IntelligentWall.choose(potentialSet, caster, mWorld);
      } else if (c instanceof TargetFilter) {
        ((TargetFilter) c).filter(potentialSet, caster, mWorld);
      } else if (!(c instanceof Monster) && !(c instanceof Tree) && !(c instanceof AbstractGenerator) && !(c instanceof Request) && !(c instanceof Tempest) && !(c instanceof Teleport) && !(c instanceof Exit)) {
        // There is a test that relies on the following message!
        System.err.println("No special handling for " + c.getName());
      }

      if (potentialSet.isEmpty()) {
        if (DUMP_AI_CAST_FAILURES) {
          final String casterDescription;
          if (caster instanceof Wizard) {
            final Selector selector = ((Wizard) caster).getSelector();
            casterDescription = "Wizard[" + (selector == null ? "null" : selector.getClass().getName()) + "]";
          } else {
            casterDescription = caster.getClass().getName();
          }
          System.out.println(casterDescription + " failed to cast " + c.getClass().getName());
        }
        return false; // Failed to cast the spell
      }

      // At this stage, we simply choose at random among the remaining cells
      final Cell[] targets = potentialSet.toArray(new Cell[0]);
      c.cast(mWorld, caster, targets[Random.nextInt(targets.length)], cell);
    }
    return true;
  }

  /**
   * Special casting that understands about meditations
   * @param caster who is caster
   * @param c what is being cast
   * @param casterCell where the caster is
   * @param multiplicity the total number of instances to be cast
   * @return true if the cast was successful
   */
  public boolean meditationCast(final Caster caster, final Castable c, final Cell casterCell, final int multiplicity) {
    return IntelligentMeditationCaster.cast(mWorld, mCastMaster, caster, casterCell, c, multiplicity);
  }

  /*
   * To assist the computer in the automatic movement of creatures a number
   * is associated with each cell on the board. This number is supposed to
   * represent the desirability of moving to that cell. The larger the
   * number the more desirable the cell and negative numbers indicate cells
   * that should be avoided. For instance, radioactive land should be
   * avoided by most things, and it tends to be given a negative number.
   *
   * The basic desirability of a cell is then modified according to the
   * distance from the present position. This encourages creatures to seek
   * local optimization in their position rather than all creatures aiming
   * for the same single globally optimal square.
   *
   * Because Wizards tend to have a slightly different set of values to
   * normal creatures (e.g. a general desire to avoid conflict) there is a
   * separate weighting for the movement of Wizards.
   */
  private int selectTarget(final Wizard wizard, final int source, int sqmp, final boolean flying) {

    final Cell wcell = mWorld.getCell(wizard);
    final Monster m = (Monster) mWorld.actor(source);
    final boolean useWizardFieldToMove = (wcell != null && wcell.getCellNumber() == source)
      || (m instanceof Caster && m.get(Attribute.COMBAT) < 5);

    final int combat = m.get(Attribute.COMBAT);
    final int p = m.getOwner();

    final ScalarField field;
    if (useWizardFieldToMove) {
      field = new CombinedField(new LocalSmoothedField(new WizardField(mWorld, wizard), mWorld, source), ContourField.createField(mWorld.width(), mWorld.height(), 0.1, 2), 0.99);
    } else {
      field = new LinearSmoothedField(new StandardField(mWorld, wizard), mWorld, source);
      //field = new PowerField(new StandardField(mWorld, wizard), mWorld, source);
    }
    int currentBest = source; // current best option is don't move
    double scoreBest = field.weight(source);
    int countBest = 1;

    // if not flying then can move at most one cell
    if (!flying && sqmp > 2) {
      sqmp = 2;
    }

    final Set<Cell> reachable = flying ? mReachable.getReachableFlying(source, Math.sqrt(sqmp)) : null;
    for (int c = 0; c < mWorld.size(); ++c) {
      if (c == source) {
        continue;
      }
      final int sqd = mWorld.getSquaredDistance(source, c);
      if (sqd > sqmp) {
        continue; // cell is too far away
      }
      if (reachable != null && !reachable.contains(mWorld.getCell(c))) {
        continue; // Unreachable
      }
      final Actor ac = mWorld.actor(c);
      if (ac != null) {
        if (ac instanceof Nuked) {
          continue;
        }
        final int acp = ac.getOwner();
        if (ac instanceof Gollop && acp == p && !((Gollop) ac).shouldMerge(m)) {
          continue; // Don't merge "m" into this Gollop
        }
        if (m instanceof Rider) {
          if (acp == p) {
            // Don't try and move onto own things unless combat < 0 or it is
            // something the wizard can mount or meditate in
            if (!(combat < 0
              || ac instanceof Mountable
              || (ac instanceof Rideable && m.is(PowerUps.RIDE))
              || (ac instanceof Meditation && ((Meditation) ac).getMount() == null))) {
              continue;
            }
          } else if (mTeam.getTeam(p) == mTeam.getTeam(acp)) {
            // Don't try and move onto meditations occupied by team members,
            // unless combat is negative
            if (ac instanceof Meditation) {
              final Actor mount = ((Meditation) ac).getMount();
              if (combat > 0 && mount != null && mTeam.getTeam(mount) == mTeam.getTeam(p)) {
                continue;
              }
            } else if (combat > 0) {
              // never attack team mates
              continue;
            }
          }
        } else {
          if (acp != p || !(ac instanceof Gollop)) {
            // don't attack things from unattackable realms
            if (!Realm.realmCheck(m, ac)) {
              continue;
            }
            // don't bother attacking if combat is 0
            if (combat == 0) {
              continue;
            }
            // don't attack friends
            if (combat >= 0 && mTeam.getTeam(p) == mTeam.getTeam(acp)) {
              continue;
            }
            // don't heal enemies
            if (combat < 0 && mTeam.getTeam(p) != mTeam.getTeam(acp)) {
              continue;
            }
          }
        }
      }
      if (ac instanceof Killer) {
        if (m instanceof Wizard || Random.nextInt(PIT_BIAS_FACTOR + 3 * m.get(Attribute.LIFE)) != 0) {
          continue;
        } else {
          return c;
        }
      }
      final double score = field.weight(c);
      if (score > scoreBest) {
        scoreBest = score;
        currentBest = c;
        countBest = 1;
      } else if (Math.abs(score - scoreBest) < 1e-100 && Random.nextInt(++countBest) == 0) {
        currentBest = c;
      }
    }
    //System.out.println(source + " -> " + currentBest + " " + m.getName() + " " + movingTheWizard + " " + countBest);
    return currentBest;
  }

  /**
   * Test if the cell at t contains something which is valid to attack when
   * engaged cell c is moving. This function is robust enough to handle invalid
   * targets.
   * @param wizard who is moving
   * @param t target cell number
   * @param c cell of mover
   * @return true if the cell is a valid choice
   */
  private boolean engageChoice(final Wizard wizard, final int t, final int c) {
    final Actor a = mWorld.actor(t);
    if (a == null || a.getState() == State.DEAD || a instanceof Killer || a.is(PowerUps.INVULNERABLE)) {
      return false;
    }
    final Actor actor = mWorld.actor(c);
    final int combat = actor.get(Attribute.COMBAT);
    final boolean ownerTest = mTeam.getTeam(wizard) == mTeam.getTeam(a);
    if (combat >= 0 && ownerTest || combat < 0 && !ownerTest) {
      return false;
    }
    return Realm.realmCheck(actor, a);
  }

  /**
   * Given cell c, known to be engaged, select an adjacent cell which satisfies the rules of engagement.
   * @param p wizard moving
   * @param c engaged cell
   * @return cell to attack or -1
   */
  private int chooseEngaged(final Wizard p, final int c) {
    int choice = -1;
    double choiceLethality = Double.NEGATIVE_INFINITY;

    // Determine combat ability of aggressor
    final Actor attacker = mWorld.actor(c);
    final int combat = attacker instanceof Monster ? attacker.get(Attribute.COMBAT) : 0;

    // Try for a wizard or the highest strength monster that can be killed by the mover.
    final Attribute combatApply = attacker instanceof Monster ? ((Monster) attacker).getCombatApply() : Attribute.LIFE;
    for (final Cell cell : mWorld.getCells(c, 1, 1, false)) {
      final int t = cell.getCellNumber();
      if (engageChoice(p, t, c)) {
        if (cell.contains(Wizard.class)) {
          return t;
        }
        final Actor a = cell.peek();
        final double lethality = Weight.lethality(a);
        final int attr = a.get(combatApply);
        if (attr <= combat && lethality > choiceLethality) {
          choiceLethality = lethality;
          choice = t;
        }
      }
    }
    // If nothing suitable above, then choose at random
    if (choice == -1) {
      int count = 0;
      for (final Cell cell : mWorld.getCells(c, 1, 1, false)) {
        final int t = cell.getCellNumber();
        if (engageChoice(p, t, c) && Random.nextInt(++count) == 0) {
          choice = t;
        }
      }
    }
    return choice;
  }

  private void considerSwappingConveyance(final Wizard wizard, final Monster monster, final int source) {
    if (wizard.get(Attribute.MOVEMENT) > 0) {
      final int p = wizard.getOwner();
      final int currentConveyanceLife = monster.get(Attribute.LIFE);
      for (final Cell c : mWorld.getCells(source, 1, 1, false)) {
        final Actor a = c.peek();
        if (a != null
          && a.get(Attribute.LIFE) > currentConveyanceLife
          && (a.getOwner() == p || (a instanceof Meditation && ((Meditation) a).getMount() == null))) {
          // Found a better conveyance, so swap
          mMoveMaster.dismount(wizard, source, c.getCellNumber());
        }
      }
    }
  }

  /**
   * Attempt move of cell.  Given that <code>wizard</code> is currently
   * in the movement phase, attempt to move the contents of cell
   * <code>source</code>.
   * @param wizard wizard currently in movement phase
   * @param source cell to move
   */
  private void move(final Wizard wizard, int source) {
    while (mMoveMaster.isMovable(wizard, source)) {
      final Monster monster = (Monster) mWorld.actor(source);
      int squaredMovementPoints = mMoveMaster.getSquaredMovementPoints(wizard, source);
      final boolean flying = monster.is(PowerUps.FLYING);
      final boolean engaged = mMoveMaster.isEngaged(wizard, source);

      if (engaged) {
        squaredMovementPoints = 2;
      } else if (monster instanceof Conveyance && ((Conveyance) monster).getMount() == wizard) {
        considerSwappingConveyance(wizard, monster, source);
      }

      if (squaredMovementPoints > 0) {
        if (engaged) {
          if (monster.get(Attribute.COMBAT) != 0) {
            // Only bother trying to attack if we have nonzero combat
            final int target = chooseEngaged(wizard, source);
            if (target != -1 && monster.get(Attribute.COMBAT) != 0) {
              if (mMoveMaster.move(wizard, source, target) == MoveMaster.OK) {
                source = target;
              }
            }
          }
          monster.setMoved(true);
        } else {
          final int target = selectTarget(wizard, source, squaredMovementPoints, flying);
          if (target != source && target >= 0) {
            final int moveResult = mMoveMaster.move(wizard, source, target);
            if (moveResult == MoveMaster.ILLEGAL) {
              monster.setMoved(true);
            } else if (moveResult == MoveMaster.OK || moveResult == MoveMaster.PARTIAL) {
              source = target;
            }
          } else {
            // failed to find better place to be, mark as moved
            monster.setMoved(true);
          }
        }
      }
    }
    // attempt the shooting part of the move
    shoot(wizard, source);
    shootConveyance(wizard, source);
  }

  private Set<Cell> legalShootCandidates(final Set<Cell> candidates, final Monster shooter) {
    final Team team = mWorld.getTeamInformation();
    final int rc = shooter.get(Attribute.RANGED_COMBAT);
    final int shooterTeam = team.getTeam(shooter);
    for (final Iterator<Cell> it = candidates.iterator(); it.hasNext(); ) {
      final Actor a = it.next().peek();
      if (a == null
        || a.getState() == State.DEAD
        || a instanceof Killer
        || (rc >= 0) == (shooterTeam == team.getTeam(a))
        || !Realm.realmCheck(shooter, a)) {
        it.remove();
      }
    }
    return candidates;
  }

  private int probabilityOfShootingReflector(final Monster shooter) {
    final Wizard w = mWorld.getWizardManager().getWizard(shooter);
    if (w != null && w.getPlayerEngine() instanceof HumanEngine) {
      return 0; // Human player pressed "lazy" key
    }
    final int intel = shooter.get(Attribute.INTELLIGENCE);
    return 100 - intel; // Doesn't matter if this returns negative
  }

  private Cell chooseShootTarget(final Set<Cell> candidates, final Monster shooter) {
    Cell target = null;
    double tw = Double.NEGATIVE_INFINITY;
    int bestc = 0; // used for fair selection
    final int rp = probabilityOfShootingReflector(shooter);
    final int rc = shooter.get(Attribute.RANGED_COMBAT);
    if (rc >= 0) {
      // In the first instance look for something that can be killed
      for (final Cell c : candidates) {
        final Actor a = c.peek();
        if ((a.is(PowerUps.REFLECT) || a.is(PowerUps.INVULNERABLE)) && Random.nextInt(100) >= rp) {
          continue;  // Don't shoot enemy with reflector or invulnerability
        }
        final int v = a.get(shooter.getRangedCombatApply());
        if (v - rc < 0) {
          final double wa = Weight.lethality(a);
          if (wa >= tw) {
            if (wa > tw) {
              target = c;
              tw = wa;
              bestc = 0;
            } else if (Random.nextInt(++bestc) == 0) {
              target = c;
            }
          }
        }
      }
      if (target != null) {
        return target;
      }
    }
    // Nothing we can kill, choose the most lethal thing
    for (final Cell c : candidates) {
      final Actor a = c.peek();
      if (rc >= 0 && (a.is(PowerUps.REFLECT) || a.is(PowerUps.INVULNERABLE)) && Random.nextInt(100) >= rp) {
        continue;  // Don't shoot enemy with reflector
      }
      final double wa = Weight.lethality(a);
      if (wa >= tw) {
        if (wa > tw) {
          target = c;
          tw = wa;
          bestc = 0;
        } else if (Random.nextInt(++bestc) == 0) {
          target = c;
        }
      }
    }
    return target;
  }

  private Cell shoot(final int source, final Monster m) {
    // compute (range+0.5)^2 without resorting to floating point
    final int rangeSquared = m.get(Attribute.RANGE) * m.get(Attribute.RANGE) + m.get(Attribute.RANGE);
    return chooseShootTarget(legalShootCandidates(mWorld.getCells(source, 1, (int) Math.sqrt(rangeSquared + 0.25), !m.is(PowerUps.ARCHERY)), m), m);
  }

  private void shoot(final Wizard wizard, final int source) {
    while (mMoveMaster.isShootable(wizard, source)) {
      // above test guarantees this cell contains a monster
      final Monster m = (Monster) mWorld.actor(source);
      final Cell target = shoot(source, m);
      if (target == null || mMoveMaster.shoot(wizard, source, target.getCellNumber()) != MoveMaster.OK) {
        break;
      }
    }
  }

  private void shootConveyance(final Wizard wizard, final int source) {
    while (mMoveMaster.isShootableConveyance(wizard, source)) {
      // above test guarantees this cell contains a monster
      final Monster m = (Monster) ((Conveyance) mWorld.actor(source)).getMount();
      final Cell target = shoot(source, m);
      if (target == null || mMoveMaster.shootFromConveyance(wizard, source, target.getCellNumber()) != MoveMaster.OK) {
        break;
      }
    }
  }

  @Override
  public void moveAll(final Wizard wizard) {
    if (wizard == null) {
      return;
    }

    // try and move the wizard first
    final Cell wcell = mWorld.getCell(wizard);
    if (wcell != null) {
      move(wizard, wcell.getCellNumber());
    }
    // move everything else
    for (int c = 0; c < mWorld.size(); ++c) {
      move(wizard, c);
    }
    // and again in case things can now move that couldn't before
    for (int c = 0; c < mWorld.size(); ++c) {
      move(wizard, c);
    }
  }
}
