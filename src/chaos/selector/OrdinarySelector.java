package chaos.selector;

import java.util.Set;

import chaos.Chaos;
import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Drainer;
import chaos.common.FreeCastable;
import chaos.common.Meditation;
import chaos.common.Monster;
import chaos.common.Mountable;
import chaos.common.PowerUp;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Rideable;
import chaos.common.State;
import chaos.common.beam.PlasmaBeam;
import chaos.common.free.Ferengi;
import chaos.common.free.MassResurrect;
import chaos.common.free.Repulsion;
import chaos.common.free.Summons;
import chaos.common.spell.Bury;
import chaos.common.spell.RaiseDead;
import chaos.common.spell.Stop;
import chaos.common.wizard.Wizard;
import chaos.util.Random;
import chaos.util.RankingTable;

/**
 * A rule-based selector.
 *
 * @author Sean A. Irvine
 */
public class OrdinarySelector implements Selector {

  /** Array index for number of meditations on the board. */
  protected static final int MEDITATIONS = 0;
  /** Array index for fastest on board. */
  protected static final int FASTEST = MEDITATIONS + 1;
  /** Array index for number of materials. */
  protected static final int MATERIALS = FASTEST + 1;
  /** Array index for number of undeads. */
  protected static final int UNDEADS = MATERIALS + 1;
  /** Array index for number of corpses. */
  protected static final int CORPSES = UNDEADS + 1;
  /** Array index for number of adjacent owned cells. */
  protected static final int OWNED = CORPSES + 1;
  /** Array index for number of adjacent enemy cells. */
  protected static final int ENEMY = OWNED + 1;
  /** Array index for number of adjacent empty cells. */
  protected static final int EMPTY = ENEMY + 1;
  /** One more than the last statistic. */
  protected static final int MAX_STAT = EMPTY + 1;

  /** Constant used in randomly modifying spell rankings. */
  private static final int VARIATION = (int) Math.sqrt(RankingTable.getMaximumRanking());
  /** Amount to boost free castable when wizard is surrounded. */
  private static final int FREE_BOOST = VARIATION + 3;
  /** Large boost used in certain circumstances. */
  private static final int BIG_BOOST = RankingTable.getMaximumRanking() >>> 1;
  /** Medium boost used in certain circumstances. */
  private static final int STD_BOOST = RankingTable.getMaximumRanking() >>> 2;
  /** Low boost used in certain circumstances. */
  private static final int LOW_BOOST = RankingTable.getMaximumRanking() >>> 3;

  /** The wizard this selector is for. */
  protected final Wizard mWizard;
  /** The world this selector is for. */
  protected final World mWorld;
  protected final CastMaster mCastMaster;

  /**
   * Construct a selector for the specified wizard and world.
   *
   * @param wizard the wizard
   * @param world the world
   * @param castMaster casting rules
   */
  public OrdinarySelector(final Wizard wizard, final World world, final CastMaster castMaster) {
    mWizard = wizard;
    mWorld = world;
    mCastMaster = castMaster;
  }

  /**
   * Make a pass over the entire world collecting some statistics.
   *
   * @return statistics array
   */
  private int[] gatherStats(final Cell wcell, final int wizardNumber) {
    final int[] s = new int[MAX_STAT];
    final int[] r = new int[Realm.values().length];
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a != null) {
        final State state = a.getState();
        if (state == State.ACTIVE) {
          r[a.getRealm().ordinal()]++;
        } else if (state == State.DEAD) {
          s[CORPSES]++;
        }
        if (a instanceof Meditation) {
          s[MEDITATIONS]++;
        }
        if (a instanceof Monster) {
          final Monster m = (Monster) a;
          if (m.get(Attribute.MOVEMENT) > s[FASTEST]) {
            s[FASTEST] = m.get(Attribute.MOVEMENT);
          }
        }
      }
    }
    s[MATERIALS] = r[Realm.MATERIAL.ordinal()] + r[Realm.MYTHOS.ordinal()];
    s[UNDEADS] = r[Realm.ETHERIC.ordinal()];

    // examine adjacent cells
    final Set<Cell> adj = mWorld.getCells(wcell.getCellNumber(), 1, 1, false);
    for (final Cell c : adj) {
      final Actor a = c.peek();
      if (a != null && a.getState() != State.DEAD) {
        s[a.getOwner() == wizardNumber ? OWNED : ENEMY]++;
      } else {
        s[EMPTY]++;
      }
    }

    return s;
  }

  /**
   * Get a score for the given castable.  The higher the score the
   * better this spell.
   *
   * @param c the castable to get a score for
   * @param s sundry statistics
   * @param casterCell cell containing the caster
   * @return score
   */
  protected int getScore(final Castable c, final int[] s, final Cell casterCell) {
    // This really needs a complete rewrite ... it needs to be much cleaner and smarter
    int score = RankingTable.getRanking(c);
    assert score >= 0;

    // Adjustment for completely surrounded wizard
    if (s[EMPTY] == 0) {
      if (c instanceof Monster) {
        return 0;
      } else if (c instanceof FreeCastable) {
        score += FREE_BOOST;
      }
    }

    // Adjustment for double and triple
    if (mWizard.get(PowerUps.DOUBLE) + mWizard.get(PowerUps.TRIPLE) > 0 && (c.getCastFlags() & Castable.CAST_SINGLE) != 0) {
      return 1;
    }

    // Adjustment for power-up wizard already has
    if (c instanceof PowerUp && mWizard.is(((PowerUp) c).getPowerUpType())) {
      return 1;
    }

    if (s[EMPTY] < 7 && c instanceof Summons) {
      score -= BIG_BOOST;
    }

    // Drainers are best applied when there are many undeads
    if (c instanceof Drainer && s[UNDEADS] > 6) {
      score += STD_BOOST;
    }

    // Adjustment for meditations
    if (c instanceof Meditation) {
      score -= s[MEDITATIONS] >>> 1;
    }

    // Adjustment for exposed wizards
    if (casterCell.peek() == mWizard && (c instanceof Meditation || c instanceof Mountable || (c instanceof Rideable && mWizard.is(PowerUps.RIDE)))) {
      score += BIG_BOOST;
    }

    if (c instanceof Stop && s[FASTEST] >= 6) {
      score += STD_BOOST;
    }

    if (s[CORPSES] >= 9 && (c instanceof RaiseDead || c instanceof Bury || c instanceof MassResurrect)) {
      score += STD_BOOST;
    }

    if (c instanceof Repulsion) {
      if (s[EMPTY] == 0) {
        score += BIG_BOOST;
      } else if (s[EMPTY] > 3) {
        score -= STD_BOOST;
      }
    }

    if (s[OWNED] >= 4 && !(c instanceof Monster)) {
      score += LOW_BOOST;
    }

    // could make this more general, lowers likelihood of these spells near start of game
    if (c instanceof Drainer || c instanceof PlasmaBeam || c instanceof Ferengi) {
      final int turn = Chaos.getChaos().getCurrentTurn();
      if (turn < 10) {
        score /= 10 - turn;
      }
    }
    if (c instanceof Ferengi) {
      final int cnt = mWorld.getWizardManager().getActiveCount();
      if (cnt <= 2) {
        score -= BIG_BOOST;
      } else if (cnt <= 4) {
        score -= LOW_BOOST;
      }
    }

    // adjustment for legality of cast
    final int wcn = casterCell.getCellNumber();
    if (c instanceof FreeCastable) {
      if (!mCastMaster.isLegalCast(mWizard, c, wcn, wcn)) {
        score = 0;
      }
    } else {
      // check there is at least one valid cell for this cast
      boolean ok = false;
      for (int i = 0; i < mWorld.size(); ++i) {
        if (mCastMaster.isLegalCast(mWizard, c, wcn, i)) {
          ok = true;
          break;
        }
      }
      if (!ok) {
        score = 0;
      }
    }
    // subclasses can extend this score method to provide personalities

    return score + Random.nextInt(VARIATION);
  }

  @Override
  public Castable[] select(final Castable[] castables, final boolean texas) {
    if (SelectorUtils.noSelectionPossible(castables, mWizard)) {
      return SelectorUtils.NO_SELECTION;
    }
    // find cell containing wizard, exit if cell not found
    final Cell wcell = mWorld.getCell(mWizard);
    if (wcell == null) {
      return SelectorUtils.NO_SELECTION;
    }
    assert wcell.getCellNumber() >= 0 && wcell.getCellNumber() < mWorld.size();

    final int[] s = gatherStats(wcell, mWizard.getOwner());

    // choose the best option
    int bestsc = 0;
    Castable best = null;
    for (final Castable c : castables) {
      final int sc = getScore(c, s, wcell);
      if (sc > bestsc) {
        bestsc = sc;
        best = c;
      }
    }

    final Castable worst = texas ? SelectorUtils.worst(castables, best) : null;
    return new Castable[] {best, worst};
  }

  @Override
  public Castable[] selectBonus(final Castable[] castables, final int count) {
    return SelectorUtils.best(castables, count);
  }
}
