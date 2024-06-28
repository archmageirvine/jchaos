package chaos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.Grower;
import chaos.board.MoveMaster;
import chaos.board.Updater;
import chaos.board.World;
import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Cat;
import chaos.common.FrequencyTable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.free.MagicWand;
import chaos.common.inanimate.Generator;
import chaos.common.monster.CatLord;
import chaos.common.monster.Solar;
import chaos.common.wizard.Wizard;
import chaos.engine.AiEngine;
import chaos.engine.HumanEngine;
import chaos.engine.PlayerEngine;
import chaos.graphics.Animator;
import chaos.graphics.ChaosScreen;
import chaos.graphics.CursorName;
import chaos.graphics.GenericScoreDisplay;
import chaos.graphics.GenericScreen;
import chaos.graphics.ScoreDisplay;
import chaos.graphics.TextScoreDisplay;
import chaos.graphics.TileManager;
import chaos.graphics.TileManagerFactory;
import chaos.scenario.Scenario;
import chaos.scenario.ScenarioUtils;
import chaos.selector.GenericScreenSelector;
import chaos.setup.Configure;
import chaos.sound.Sound;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ChaosProperties;
import chaos.util.CombatUtils;
import chaos.util.CurrentMoverEvent;
import chaos.util.EventListener;
import chaos.util.EventLogger;
import chaos.util.MovementUtils;
import chaos.util.NameUtils;
import chaos.util.Sleep;
import chaos.util.TeamUtils;
import chaos.util.TextEvent;
import irvine.util.CliFlags;
import irvine.util.CliFlags.Validator;

/**
 * Chaos main program.
 * @author Sean A. Irvine
 */
public final class Chaos implements Serializable {

  // Care is needed with static initialization in this class.  The initialization
  // of certain other classes depends on settings of ChaosProperties which get
  // set as part of the Configure class mechanism.  In particular, no Castable
  // object should be static in this class.

  // Singleton
  private static Chaos sChaos = null;

  private final Generator mGenerator = new Generator();
  private final MagicWand mWand = new MagicWand();
  private final CatLord mCatLord = new CatLord();
  private final Solar mSolar = new Solar();

  private World mWorld;
  private final Configuration mConfig;
  private final boolean mTexasTradeEm;
  private MoveMaster mMoveMaster;

  // This should be private!
  Scenario mScenario = null;

  private transient CastMaster mCastMaster = null;
  private transient Grower mGrower = null;
  private transient Updater mUpdater = null;
  private transient PlayerEngine mAI = null;
  private transient TileManager mTM = null;


  /** Number of turns remaining, or -1 for infinite turns. */
  private int mTurns = -1;
  int mCurrentTurn = 0;

  public int getCurrentTurn() {
    return mCurrentTurn;
  }

  void doCasting() {
    // Wizard's cast their spells (in player order).
    for (final Wizard w : mWorld.getWizardManager().getWizards()) {
      if (w != null && w.getState() != State.DEAD) {
        mWorld.notify(new CurrentMoverEvent(w.getOwner()));
        w.doCasting(mWorld.getCell(w));
      }
    }
    mWorld.notify(new CurrentMoverEvent(Actor.OWNER_NONE));
    // All other Casters cast their spells
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a instanceof Caster) {
        final Caster c = (Caster) a;
        if (!(c instanceof Wizard) && c.getState() == State.ACTIVE) {
          c.doCasting(mWorld.getCell(i));
        }
      }
    }
    mWorld.notify(new TextEvent(""));
  }

  void doPostCastUpdate(final ChaosScreen screen) {
    writePhase(screen, "GENERATORS", null, mGenerator);
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a instanceof AbstractGenerator) {
        ((AbstractGenerator) a).generate(mWorld, i);
      }
    }
    writePhase(screen, "GROWTH", null, null);
    mGrower.grow();
    writePhase(screen, "UPDATE", null, null);
    mWorld.getWarpSpace().warpIn(mWorld);
    mUpdater.update();
    if (mScenario == null) {
      mUpdater.applyTopScorerBonus();
    }
  }

  private boolean keepPlaying(final ChaosScreen screen) throws IOException {
    if (mTurns != -1 && --mTurns == 0) {
      return false;
    }
    if (ScenarioUtils.isScenarioChainDone(this, screen)) {
      return false;
    }
    final int alive = mWorld.getWizardManager().getActiveCount();
    if (alive == 0) {
      System.out.println("Every wizard is dead");
      return false;
    }
    if (alive > 1 || mScenario != null) {
      return true;
    }
    // Only 1 wizard left alive, need to check for independents.
    for (int i = 0; i < mWorld.size(); ++i) {
      final Actor a = mWorld.actor(i);
      if (a != null && a.getState() == State.ACTIVE && a.getOwner() == Actor.OWNER_INDEPENDENT) {
        // found an independent
        return true;
      }
    }
    return false;
  }

  void doMovement(final ChaosScreen screen) {
    final boolean catLordAlive = MovementUtils.markAllCatsAsMoved(mWorld);
    for (final Wizard w : mWorld.getWizardManager().getWizards()) {
      if (w != null && w.getState() == State.ACTIVE) {
        mWorld.notify(new CurrentMoverEvent(w.getOwner()));
        if (w.is(PowerUps.FROZEN)) {
          mWorld.notify(new TextEvent("Frozen"));
          w.decrement(PowerUps.FROZEN);
        } else {
          final PlayerEngine engine = w.getPlayerEngine();
          if (engine != null) {
            writePhase(screen, "MOVEMENT", NameUtils.getTextName(w), w);
            mMoveMaster.dropAttention(w);
            MovementUtils.clearMovement(mWorld, w, catLordAlive);
            engine.moveAll(w);
            while (w.is(PowerUps.MOVE_IT)) {
              w.decrement(PowerUps.MOVE_IT);
              writePhase(screen, "MOVE IT!", NameUtils.getTextName(w), w);
              mMoveMaster.dropAttention(w);
              MovementUtils.clearMovement(mWorld, w, catLordAlive);
              engine.moveAll(w);
            }
          }
        }
      }
    }
    final Wizard indp = mWorld.getWizardManager().getIndependent();
    mWorld.notify(new CurrentMoverEvent(indp.getOwner()));
    writePhase(screen, "MOVEMENT", "INDEPENDENTS", null);
    mMoveMaster.dropAttention(indp);
    MovementUtils.clearMovement(mWorld, indp, catLordAlive);
    if (indp.is(PowerUps.FROZEN)) {
      mWorld.notify(new TextEvent("Frozen"));
      indp.decrement(PowerUps.FROZEN);
    } else {
      getAI().moveAll(indp);
    }
    mWorld.notify(new CurrentMoverEvent(Actor.OWNER_NONE));
  }

  private void writePhase(final ChaosScreen screen, final String phase, final String sec, final Castable castable) {
    if (screen != null) {
      screen.writePhase(phase, sec, getTileManager().getSpellTile(castable));
    }
  }

  void playChaos(final ChaosScreen screen, final ScoreDisplay scoredisplay, final int turnLimit) throws IOException {
    while (keepPlaying(screen) && (turnLimit == 0 || mCurrentTurn < turnLimit)) {
      if (scoredisplay != null) {
        writePhase(screen, "SCORES", null, null);
        scoredisplay.showScores(mCurrentTurn);
      }
      try {
        if (screen != null) {
          screen.blankCenter();
          for (int c = 0; c < mWorld.size(); ++c) {
            final Cell cell = mWorld.getCell(c);
            cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
          }
          mWorld.notify(new TextEvent("Saving Game"));
          synchronized (screen.lock()) {
            saveGame(this, ".chaos_savedgame");
          }
          mWorld.notify(new TextEvent("Finished Saving"));
        }
      } catch (final IOException e) {
        // too bad
      }
      ++mCurrentTurn;
      // Scenario update must occur after saving for consistent behaviour
      if (!ScenarioUtils.update(mWorld, mScenario)) {
        // Death in scenario
        if (isVerbose()) {
          System.out.println("Scenario update returned false.");
        }
        break;
      }
      if (isVerbose()) {
        System.out.println("Turn number: " + mCurrentTurn);
      }
      if (mScenario == null) {
        TeamUtils.smashTeams(mWorld);
      }
      for (final Wizard w : mWorld.getWizardManager().getWizards()) {
        if (w != null && w.getState() == State.ACTIVE && !mWorld.getWarpSpace().contains(w)) {
          mWorld.notify(new CurrentMoverEvent(w.getOwner()));
          w.bonusSelect();
          w.select(mTexasTradeEm);
        }
      }
      mWorld.notify(new CurrentMoverEvent(Actor.OWNER_NONE));
      writePhase(screen, "CASTING", null, mWand);
      doCasting();
      doPostCastUpdate(screen);
      doMovement(screen);
      final int catLordOwner = mWorld.isCatLordAlive();
      if (catLordOwner != Actor.OWNER_NONE) {
        writePhase(screen, "MOVEMENT", "CAT LORD", mCatLord);
        final Wizard w = mWorld.getWizardManager().getWizard(catLordOwner);
        if (w != null && w.is(PowerUps.FROZEN)) {
          // Cat lord is frozen via its owner.
          mWorld.notify(new TextEvent("Frozen"));
        } else {
          // All cats must now be moved, clear their movement
          // flag, then invoke the AI engine with the appropriate wizard
          MovementUtils.clearMovement(mWorld, Cat.class, catLordOwner);
          mMoveMaster.dropAttention(w);
          getAI().moveAll(w);
        }
      }
      // Mark all solars unmoved before trying to move them, cannot do in the
      // update loop because then they can move more than once.
      writePhase(screen, "MOVEMENT", "SOLARS", mSolar);
      MovementUtils.clearMovement(mWorld, Solar.class, -1);
      for (final Wizard w : mWorld.getWizardManager().getWizards()) {
        if (w != null) {
          getAI().moveAll(w);
        }
      }
      getAI().moveAll(mWorld.getWizardManager().getIndependent());
      writePhase(screen, "SPECIAL", "COMBAT", null);
      CombatUtils.performSpecialCombat(mWorld, mMoveMaster);
      if (screen != null) {
        screen.setCursor(CursorName.CROSS); // ensure cursor (Bug#159)
      }
    }
  }

  /**
   * Get an instance. Hopefully can remove this function one day.
   * @return instance
   */
  public static synchronized Chaos getChaos() {
    if (sChaos == null) {
      sChaos = new Chaos(new Configuration(32, 24, true, 32, 24), false);
    }
    return sChaos;
  }

  /** For testing purposes. */
  public static void reset() {
    sChaos = null;
    sVerbose = false;
  }

  static void setChaos(final Chaos chaos) {
    sChaos = chaos;
  }

  // Example of how to play a bunch of games to see which AI is better
  // (n=0; while [ $n -lt 50 ]; do java chaos.Chaos -x Ai,Ai -t 5000 | grep active | grep -v Independent | gawk '{print $1}'; n=$[n+1]; done) | sort | uniq -c

  // Example of spell utility calculation
  // THIS IS OBSOLETE USE THE RunGames class CODE!
  // for ((k=0; k<50000; ++k)); do echo "Playing game $k"; java chaos.Chaos -s -v -t 100 2>&1 | grep -E -w '^CXX|active|dead' | grep -v Independent | gawk '{if (NF>3) {print $1,$(NF-2)} else {print $0}}' | tac | gawk '{if (NF==2) {map[$1]=$2;} else {print map[$2],$3;}}' | tee -a ~/spell.log; done
  // cat ~/spell.log | gawk '{if (NF!=1) {print $0}}' | sort -k 2 | uniq -c | gawk '{if ($2=="active") {a=$1;} else {print (a+0.0)/(a+$1), $3; a=0;}}' | sort -nr | gawk '{printf "%0.4f %s\n",$1,$2;}' | tr '_' ' ' >~/spell.rank

  private static final String VERBOSE_FLAG = "verbose";
  private static final String EXPERIMENTAL_FLAG = "Xexperiment";
  private static final String SELECTOR_FLAG = "Xselector";
  private static final String LOAD_FLAG = "load";
  private static final String SCENARIO_FLAG = "scenario";
  private static final String SCREEN_FLAG = "screen";
  private static final String NATIVE_SCREEN_FLAG = "native-screen";
  private static final String TURNS_FLAG = "turns";
  private static final String MIN_WIDTH_FLAG = "min-width";
  private static final String MIN_HEIGHT_FLAG = "min-height";
  private static final String WIDTH_FLAG = "width";
  private static final String HEIGHT_FLAG = "height";
  private static final String SPECIAL_FLAG = "special";
  private static final String TEXAS_FLAG = "texas";
  private static final String RANKING_FLAG = "ranking";
  private static final String WIZARDS_FLAG = "Xwizards";
  private static final String GENERATORS_FLAG = "Xgenerators";
  private static final String LEECH_FLAG = "Xleech";

  private static class ChaosValidator implements Validator {
    @Override
    public boolean isValid(final CliFlags flags) {
      if ((Integer) flags.getValue(MIN_WIDTH_FLAG) < 1 || (Integer) flags.getValue(MIN_HEIGHT_FLAG) < 1) {
        flags.setParseMessage("Width and height must be positive.");
        return false;
      }
      return true;
    }
  }

  private static CliFlags makeFlags() {
    final CliFlags flags = new CliFlags("Chaos");
    flags.registerOptional('v', VERBOSE_FLAG, "Print various logging information.");
    flags.registerOptional('u', TEXAS_FLAG, "Texas trade'em mode.");
    flags.registerOptional('x', EXPERIMENTAL_FLAG, String.class, "Ai,Ai", "Experimental playoff between specified AIs", "Ai");
    flags.registerOptional(SELECTOR_FLAG, String.class, "Selector,Selector", "Experimental playoff between specified spell selectors", "Strategiser");
    flags.registerOptional('l', LOAD_FLAG, String.class, "file", "Load a savedgame");
    flags.registerOptional(SCENARIO_FLAG, String.class, "name", "Load a scenario");
    flags.registerOptional(NATIVE_SCREEN_FLAG, "Run on existing screen if possible.");
    flags.registerOptional('t', TURNS_FLAG, Integer.class, "INTEGER", "Specify a turn limit for the game", 0);
    flags.registerOptional(MIN_WIDTH_FLAG, Integer.class, "INTEGER", "Minimum columns", 24);
    flags.registerOptional(MIN_HEIGHT_FLAG, Integer.class, "INTEGER", "Minimum rows", 18);
    flags.registerOptional(SCREEN_FLAG, Integer.class, "INTEGER", "Which display should be used", -1);
    flags.registerOptional('W', WIDTH_FLAG, Integer.class, "INTEGER", "Width of world", -1);
    flags.registerOptional('H', HEIGHT_FLAG, Integer.class, "INTEGER", "Height of world", -1);
    flags.registerOptional('s', SPECIAL_FLAG, "Perform a special experimental run");
    flags.registerOptional(RANKING_FLAG, "Perform a special experimental run to rank actors");
    flags.registerOptional(WIZARDS_FLAG, Integer.class, "INTEGER", "Specify number of wizards for an experiment", 10);
    flags.registerOptional(GENERATORS_FLAG, Integer.class, "INTEGER", "Specify number of generators for an experiment", 0);
    flags.registerOptional(LEECH_FLAG, "Set leech mode for an experiment");
    return flags;
  }

  private static int getTurnLimit(final CliFlags flags) {
    if (flags.isSet(TURNS_FLAG)) {
      return (Integer) flags.getValue(TURNS_FLAG);
    }
    return 0;
  }

  static void saveGame(final Chaos chaos, final String fileName) throws IOException {
    try (final ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
      oos.writeObject(chaos);
    }
  }

  static Chaos loadGame(final String fileName) throws IOException {
    try (final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
      final Chaos c = (Chaos) ois.readObject();
      c.initTransients();
      return c;
    } catch (final ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean sVerbose = false;

  /**
   * Test if verbose mode is active.
   * @return true for verbose
   */
  public static boolean isVerbose() {
    return sVerbose;
  }

  /**
   * Construct a new instance of chaos.
   * @param config configuration information
   * @param texas true for texas trade'em mode
   */
  public Chaos(final Configuration config, final boolean texas) {
    mConfig = config;
    mTexasTradeEm = texas;
    setWorld(new World(config.getWorldCols(), config.getWorldRows()));
    initTransients();
  }

  private void initTransients() {
    if (getConfig().getCellBits() == 5) {
      mTM = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE32);
    } else {
      mTM = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    }
    mCastMaster = new CastMaster(mWorld);
    mGrower = new Grower(mWorld);
    mUpdater = new Updater(mWorld);
    mAI = new AiEngine(mWorld, mMoveMaster, mCastMaster);
  }

  /**
   * Set the world (including updating other objects that need to know).
   * @param world the world
   */
  public void setWorld(final World world) {
    mWorld = world;
    mMoveMaster = new MoveMaster(mWorld);
    initTransients();
  }

  public void setScenario(final Scenario scenario) {
    mScenario = scenario;
  }

  public Configuration getConfig() {
    return mConfig;
  }

  public World getWorld() {
    return mWorld;
  }

  public TileManager getTileManager() {
    return mTM;
  }

  public MoveMaster getMoveMaster() {
    return mMoveMaster;
  }

  public CastMaster getCastMaster() {
    return mCastMaster;
  }

  public PlayerEngine getAI() {
    return mAI;
  }

  private void prepareReporting() {
    if (isVerbose()) {
      System.out.println("This game has: " + FrequencyTable.DEFAULT.getNumberOfSpells() + " spells");
      System.out.println(getConfig().toString());
      final EventLogger el = new EventLogger(getWorld());
      getWorld().register(el);
      getMoveMaster().register(el);
    }
  }

  void printFinalScores() {
    final World w = getWorld();
    final TextScoreDisplay t = new TextScoreDisplay(w, w.getWizardManager().getWizards(), w.getWizardManager().getIndependent());
    t.showScores(mCurrentTurn);
  }

  private void initWizardEngines(final ChaosScreen screen, final EventListener animator) {
    for (final Wizard w : getWorld().getWizardManager().getWizards()) {
      if (w != null) {
        if (w.getPlayerEngine() instanceof HumanEngine) {
          final HumanEngine eng = (HumanEngine) w.getPlayerEngine();
          eng.setScreen(screen, getTileManager());
          eng.register(animator);
          final GenericScreenSelector selector = new GenericScreenSelector(w, getConfig(), getTileManager(), getWorld(), getCastMaster());
          selector.setScreen(screen);
          w.setSelector(selector);
        }
      }
    }
  }

  private void exec(final ChaosScreen screen, final int turnLimit) throws IOException {
    // Main entry point after loading or starting a new game.
    // We have to set up the animator, various engines, and other GUI related items
    final World w = getWorld();
    final Animator animator = new Animator(w, screen, getTileManager());
    w.register(animator);
    getMoveMaster().register(animator);
    initWizardEngines(screen, animator);
    final ScoreDisplay scoredisplay = new GenericScoreDisplay(getConfig(), w, screen);
    animator.start();
    playChaos(screen, scoredisplay, turnLimit);
    printFinalScores();
  }

  private static Scenario getScenario(final CliFlags flags) throws IOException {
    return flags.isSet(SCENARIO_FLAG) ? Scenario.load((String) flags.getValue(SCENARIO_FLAG)) : null;
  }

  /**
   * Chaos main program.  Call with -h for usage information.
   * @param args arguments
   * @throws Exception if an exception occurs
   */
  public static void main(final String... args) throws Exception {
    final CliFlags flags = makeFlags();
    flags.setValidator(new ChaosValidator());
    flags.setFlags(args);
    sVerbose = flags.isSet(VERBOSE_FLAG);
    final boolean headless = flags.isSet(EXPERIMENTAL_FLAG) || flags.isSet(SELECTOR_FLAG) || flags.isSet(SPECIAL_FLAG) || flags.isSet(RANKING_FLAG);
    final int minimumWidth = (Integer) flags.getValue(MIN_WIDTH_FLAG);
    final int minimumHeight = (Integer) flags.getValue(MIN_HEIGHT_FLAG);
    final Scenario scenario = getScenario(flags);
    if (sVerbose) {
      System.out.println("Using scenario: " + scenario);
    }
    final int worldRows = scenario != null ? scenario.getHeight() : (Integer) flags.getValue(HEIGHT_FLAG);
    final int worldCols = scenario != null ? scenario.getWidth() : (Integer) flags.getValue(WIDTH_FLAG);

    if (scenario == null && !headless && !Configure.configure()) {
      // Not headless and user failed to complete set up options
      return;
    }

    final Configuration config = new Configuration(minimumWidth, minimumHeight, headless, worldRows, worldCols);
    Sleep.sleep(0); // force loading and calibration of sleeper

    final ChaosProperties properties = ChaosProperties.properties();
    final boolean texas = scenario == null && (properties.getBooleanProperty(ChaosProperties.TEXAS_PROPERTY, false) || flags.isSet(TEXAS_FLAG));
    final boolean fsem = properties.getBooleanProperty(ChaosProperties.FSEM_PROPERTY, true) && !flags.isSet(NATIVE_SCREEN_FLAG);

    Sound.getSoundEngine().setSoundLevel(properties.getIntProperty(ChaosProperties.SOUND_PROPERTY, Sound.SOUND_INTELLIGENT));
    if (headless) {
      final Chaos c = new Chaos(config, texas);
      if (scenario != null) {
        scenario.init(c, null);
      }
      sChaos = c; // ideally wouldn't need this singleton
      c.prepareReporting();
      Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
      if (flags.isSet(EXPERIMENTAL_FLAG) || flags.isSet(SELECTOR_FLAG)) {
        ChaosExperimental.runExperimentalMode(c, (String) flags.getValue(EXPERIMENTAL_FLAG), (String) flags.getValue(SELECTOR_FLAG), getTurnLimit(flags), isVerbose());
      } else if (flags.isSet(RANKING_FLAG)) {
        RankingModeExperiment.runRankingMode();
      } else {
        ChaosExperimental.runSpecialMode(c, getTurnLimit(flags), (Integer) flags.getValue(WIZARDS_FLAG), (Integer) flags.getValue(GENERATORS_FLAG), flags.isSet(LEECH_FLAG));
      }
      return;
    }

    // We are about to enter the realm of graphics, possibly switching the
    // resolution of the screen.  We need to be extra careful that we tidy
    // up after ourselves in the event of an exception.
    ChaosScreen screen = null;
    try {
      screen = new GenericScreen(config, fsem, (int) flags.getValue(SCREEN_FLAG));
      screen.blank();
      final Chaos chaos;
      if (flags.isSet(LOAD_FLAG)) {
        chaos = loadGame((String) flags.getValue(LOAD_FLAG));
      } else {
        Chaos lchaos = new Chaos(config, texas);
        if (scenario != null) {
          scenario.init(lchaos, screen);
        } else {
          final boolean[] conditions = new GenericSetUp(config, screen, lchaos, false).setUp();
          if (conditions[1]) {
            // User selected Raven
            final Scenario raven = Scenario.load("chaos/resources/scenario/raven/beetle_mania.scn");
            final Configuration c = new Configuration(minimumWidth, minimumHeight, false, raven.getHeight(), raven.getWidth());
            lchaos = new Chaos(c, false); // force non-texas
            raven.init(lchaos, screen);
          } else if (conditions[0]) {
            // User selected load from start up screen
            try {
              lchaos = loadGame(".chaos_savedgame");
            } catch (final IOException e) {
              // too bad for now, probably means no such file
            } catch (final RuntimeException e) {
              e.printStackTrace(); // perhaps a serialization issue?
            }
          } else if (properties.getBooleanProperty(ChaosProperties.RANDOMIZE_PLAY_ORDER_PROPERTY, false)) {
            lchaos.getWorld().getWizardManager().shuffle(lchaos.getWorld().getTeamInformation());
          }
        }
        chaos = lchaos;
      }
      sChaos = chaos; // ideally wouldn't need this singleton
      chaos.prepareReporting();
      chaos.exec(screen, getTurnLimit(flags));
    } catch (final Exception e) {
      e.printStackTrace();
    } finally {
      if (screen != null) {
        screen.close();
        System.exit(0); // Only need exit if we had a screen
      }
    }
  }
}

