package chaos;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import chaos.board.CastMaster;
import chaos.board.Team;
import chaos.board.World;
import chaos.board.WorldUtils;
import chaos.common.Actor;
import chaos.common.CastableList;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.inanimate.Generator;
import chaos.common.wizard.Wizard;
import chaos.engine.AiEngine;
import chaos.engine.HumanEngine;
import chaos.graphics.ChaosScreen;
import chaos.graphics.ImageLoader;
import chaos.graphics.TileManager;
import chaos.selector.GenericScreenSelector;
import chaos.selector.SelectorFactory;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;
import irvine.heraldry.Shape;
import irvine.heraldry.ShapeFactory;
import irvine.util.graphics.AbstractButton;
import irvine.util.graphics.Button;
import irvine.util.graphics.RadioButton;
import irvine.util.graphics.Stipple;

/**
 * Set up that works directly on a generic screen.  Allows for the initial
 * configuration of wizards and generators.
 * @author Sean A. Irvine
 */
public class GenericSetUp implements MouseMotionListener, MouseListener, KeyListener {

  private static final BufferedImage OFF_IMAGE = ImageLoader.getImage("chaos/resources/icons/off.png");
  private static final BufferedImage HUMAN_IMAGE = ImageLoader.getImage("chaos/resources/icons/human.png");
  private static final BufferedImage AI_IMAGE = ImageLoader.getImage("chaos/resources/icons/ai.png");
  private static final BufferedImage SPELLS_IMAGE = ImageLoader.getImage("chaos/resources/icons/s.png");

  /** Used to indicate that no button is selected. */
  private static final int NONE = -1;
  /** Used to indicate mouse is over the continue gadget. */
  private static final int CONT_GADGET = -2;
  private static final Sound SOUND = Sound.getSoundEngine();

  private final Configuration mConfig;
  /** The screen this selector uses. */
  private final ChaosScreen mScreen;
  /** The world. */
  private final World mWorld;
  /** Provides model locking. */
  protected final BooleanLock mLock = new BooleanLock();
  /** Tile width. */
  private final int mSize;
  /** Vertical separation between wizards. */
  private final int mSep;
  /** Half tile width. */
  private final int mHalf;
  /** Array of wizards for configuration purposes. */
  private final Wizard[] mWizards;
  /** Array of wizard state information. */
  private final int[] mState;
  /** Map from 0 up integers into non-null wizards. */
  private final int[] mWizardMap;
  /** Number of valid wizards (i.e. valid entries in <code>mWizardMap</code>). */
  private final int mValidWizards;
  /** Number of generators. */
  private int mGenerators = ChaosProperties.properties().getIntProperty("chaos.generators", 0);
  /** Number of spells. */
  private int mSpells = ChaosProperties.properties().getIntProperty("chaos.spells", 30);
  /** Buttons on set up screen. */
  private final ArrayList<AbstractButton> mButtons = new ArrayList<>();
  /** Flag controlling loading. */
  private boolean mLoadRequest = false;
  private boolean mRavenRequest = false;
  /** Are we starting a scenario. */
  private final boolean mIsScenario;
  /** Vertical offset. */
  private final int mYOffset;
  /** Horizontal offset. */
  private final int mXOffset;
  /** Horizontal offset to spell setting. */
  private final int mXSpellOffset;
  /** Tile manager. */
  private final TileManager mTM;
  private final Chaos mChaos;
  private final Shape[] mTeamIcons;
  private final int[] mSelectedTeam;
  private final BufferedImage mShieldBlank;
  private final int mXSecondColumn;
  private final RadioButton[][] mWizardStateButtons;

  /**
   * Construct a new set up screen for the given screen.
   * @param config game configuration
   * @param screen the screen
   * @param chaos the world
   * @param isScenario are we starting a scenario
   * @throws NullPointerException if parameter is null
   */
  public GenericSetUp(final Configuration config, final ChaosScreen screen, final Chaos chaos, final boolean isScenario) {
    if (screen == null) {
      throw new NullPointerException();
    }
    mConfig = config;
    mScreen = screen;
    mChaos = chaos;
    mWorld = chaos.getWorld();
    mTM = chaos.getTileManager();
    mIsScenario = isScenario;
    mWizards = mWorld.getWizardManager().getWizards();
    mState = new int[mWizards.length];
    mWizardMap = new int[mWizards.length];
    mSize = mConfig.getCellWidth();
    mSep = mSize << 1;
    mHalf = mSize >>> 1;
    mYOffset = mSize * 3;
    mXOffset = mSize * 3;
    mXSpellOffset = (int) Math.round(mConfig.getPixelWidth() * 0.203125);
    int j = 0;
    for (int i = 0; i < mState.length; ++i) {
      mState[i] = ChaosProperties.properties().getIntProperty("chaos.wizstate" + i, 0);
    }
    for (int i = 0; i < mWizards.length; ++i) {
      if (mWizards[i] != null) {
        mWizardMap[j++] = i;
      }
    }
    // Couple of extra security pixels
    mShieldBlank = Stipple.stipple(0, 0, mSize + 2, Shape.heightAsInt(mSize) + 2).toBufferedImage();
    mValidWizards = j;
    mTeamIcons = new Shape[mWizards.length];
    mSelectedTeam = new int[mTeamIcons.length];
    for (int k = 0; k < mTeamIcons.length; ++k) {
      mTeamIcons[k] = ShapeFactory.createShape(mWorld.getTeamInformation().heraldicKey(k));
      mSelectedTeam[k] = ChaosProperties.properties().getIntProperty("chaos.teamstate" + k, k);
    }
    mXSecondColumn = mScreen.getMainWidth() + mScreen.getRightWidth() - 3 * mSep;
    mWizardStateButtons = new RadioButton[mValidWizards][3];
    for (int wiz = 0; wiz < mValidWizards; ++wiz) {
      final RadioButton[] sibs = mWizardStateButtons[wiz];
      final int w = wiz % 10;
      final int x = wiz < 10 ? mXOffset : mXSecondColumn;
      final int y = mYOffset + w * mSep + 3;
      for (int k = 0; k < sibs.length; ++k) {
        sibs[k] = new RadioButton(x + k * mSep, y, mHalf + 1, mState[mWizardMap[wiz]] == k, sibs);
      }
    }
  }

  @Override
  public void mousePressed(final MouseEvent e) {
  }

  @Override
  public void mouseReleased(final MouseEvent e) {
  }

  @Override
  public void mouseEntered(final MouseEvent e) {
  }

  @Override
  public void mouseExited(final MouseEvent e) {
  }

  @Override
  public void mouseDragged(final MouseEvent e) {
  }

  @Override
  public void keyReleased(final KeyEvent e) {
  }

  private void printString(final Graphics g, final String s, final int y) {
    final FontMetrics fm = g.getFontMetrics();
    final int w = fm == null ? 0 : fm.stringWidth(s);
    g.drawString(s, mConfig.getPixelWidth() / 2 - w / 2, y);
  }


  /**
   * Draw the selection state for a wizard.
   * @param wizNumber displayed wizard number
   */
  private void drawWizardState(final int wizNumber) {
    final Graphics g = mScreen.getGraphics();
    if (g != null) {
      for (final RadioButton b : mWizardStateButtons[wizNumber]) {
        b.paint(g);
      }
      final int w = wizNumber % 10;
      final int x = wizNumber < 10 ? mXOffset : mXSecondColumn;
      g.translate(x, mYOffset + w * mSep);
      g.drawImage(mTM.getSpellTile(mWizards[mWizardMap[wizNumber]]), mSize + mSize / 4 - mXOffset, 0, null);
      final int spos = 3 * mSep;
      g.drawImage(mShieldBlank, spos, 0, null);
      final Shape s = mTeamIcons[mSelectedTeam[mWizardMap[wizNumber]]];
      s.render(g, mSize | 1, spos, 0);
      g.dispose();
    }
  }

  private int width(final Graphics g, final String s) {
    final FontMetrics fm = g.getFontMetrics();
    return fm == null ? s.length() : fm.stringWidth(s);
  }

  private void drawVariableState(final int x, final int value) {
    final int dy = mYOffset + 10 * mSep;
    final Graphics g = mScreen.getGraphics();
    if (g != null) {
      g.setColor(Color.BLACK);
      g.setFont(mScreen.getMonospaceFont());
      g.fillRect(x, dy, width(g, "44"), mSize + 4);
      g.setColor(Color.YELLOW);
      final String s = String.valueOf(value);
      g.drawString("00".substring(0, 2 - s.length()) + s, x, dy + mSize);
      g.dispose();
    }
  }

  /**
   * Draw the currently selected number of generators.
   */
  private void drawGeneratorState() {
    drawVariableState(mXOffset, mGenerators);
  }

  /**
   * Draw the currently selected number of spells.
   */
  private void drawSpellState() {
    drawVariableState(mXSpellOffset + mSep, mSpells);
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      final int x = e.getX();
      final int y = e.getY();
      for (final AbstractButton b : mButtons) {
        if (b.contains(x, y)) {
          b.act();
          e.consume();
          return;
        }
      }
      for (int k = 0; k < mWizardStateButtons.length; ++k) {
        for (final RadioButton b : mWizardStateButtons[k]) {
          if (b.contains(x, y)) {
            b.act();
            drawWizardState(k);
            e.consume();
            return;
          }
        }
      }
      // check for click in a wizard activation oval or shield
      final int xx;
      if (x > mXOffset + 3 * mSep && x < mXOffset + 4 * mSep) {
        xx = 0;
      } else if (x > mXSecondColumn + 3 * mSep && x < mXSecondColumn + 4 * mSep) {
        xx = 10;
      } else {
        xx = -1;
      }
      if (xx >= 0 && y > mYOffset - mHalf && y < mYOffset + mValidWizards * mSep) {
        final int q = (y - mYOffset) / mSep + xx;
        final int w = mWizardMap[q];
        mSelectedTeam[w]++;
        mSelectedTeam[w] %= mValidWizards;
        drawWizardState(q);
        SOUND.playwait("chaos/resources/sound/misc/Beep", Sound.SOUND_INTELLIGENT);
        e.consume();
      } else if (mScreen.isPositionInContinueGadget(x, y)) {
        // Set up is complete.
        finalizeWizards();
        mLock.setValue(true);
        e.consume();
      }
    }
  }

  /** Records the last castable the mouse was over. */
  private int mOld = NONE;

  private AbstractButton mHighlightedButton = null;

  @Override
  public void mouseMoved(final MouseEvent e) {
    final int x = e.getX();
    final int y = e.getY();
    for (final AbstractButton b : mButtons) {
      if (b.contains(x, y)) {
        mScreen.highlight(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        if (mHighlightedButton != b && mHighlightedButton != null) {
          final Graphics g = mScreen.getGraphics();
          if (g != null) {
            mHighlightedButton.paint(g);
            g.dispose();
          }
        }
        mHighlightedButton = b;
        return;
      }
    }
    if (mHighlightedButton != null) {
      final Graphics g = mScreen.getGraphics();
      if (g != null) {
        mHighlightedButton.paint(g);
        g.dispose();
      }
    }
    if (mScreen.isPositionInContinueGadget(x, y)) {
      // mouse is over continue gadget
      if (mOld != CONT_GADGET) {
        mOld = CONT_GADGET;                // we are in continue
        mScreen.highlightContinue(true);
      }
    } else {
      mOld = NONE;
      mScreen.highlightContinue(false);
    }
  }

  private static void humanizeWizard(final Wizard wiz, final Chaos chaos, final ChaosScreen screen, final int width) {
    final GenericScreenSelector selector = new GenericScreenSelector(wiz, chaos.getConfig(), chaos.getTileManager(), chaos.getWorld(), chaos.getCastMaster());
    selector.setScreen(screen);
    wiz.setSelector(selector);
    final HumanEngine eng = new HumanEngine(chaos, width);
    eng.setScreen(screen, chaos.getTileManager());
    wiz.setPlayerEngine(eng);
    wiz.setState(State.ACTIVE);
  }

  private int getWizardState(final int wiz) {
    final RadioButton[] b = mWizardStateButtons[wiz];
    for (int k = 0; k < b.length; ++k) {
      if (b[k].isSet()) {
        mState[mWizardMap[wiz]] = k; // update property version as well
        return k;
      }
    }
    throw new IllegalStateException();
  }

  /** Complete the set up of the wizards. */
  private void finalizeWizards() {
    final Team team = mWorld.getTeamInformation();
    final CastMaster castMaster = mChaos.getCastMaster();
    final ChaosProperties properties = ChaosProperties.properties();
    final boolean lifeLeech = properties.getBooleanProperty(ChaosProperties.LIFE_LEECH_PROPERTY, false);

    for (int j = 0; j < mValidWizards; ++j) {
      final int wNum = mWizardMap[j];
      final Wizard w = mWizards[wNum];
      team.explicitSetTeam(wNum, mSelectedTeam[wNum]);
      w.setCastableList(new CastableList(100, mSpells, 24));
      if (lifeLeech) {
        w.set(PowerUps.LIFE_LEECH, 1);
      }
      switch (getWizardState(j)) {
        case 1: // Human controlled
          humanizeWizard(w, mChaos, mScreen, mConfig.getCellBits());
          break;
        case 2: // AI
          w.setSelector(SelectorFactory.randomSelector(w, mWorld, castMaster));
          w.setPlayerEngine(new AiEngine(mWorld, mChaos.getMoveMaster(), castMaster));
          w.setState(State.ACTIVE);
          break;
        default:
          w.setState(State.DEAD);
          break;
      }
    }
    team.explicitSetTeam(Actor.OWNER_INDEPENDENT, -1);
    WorldUtils.insertWizards(mWorld, mWizards, mIsScenario);
    WorldUtils.insertGenerators(mWorld, mGenerators);
    properties.setProperty("chaos.generators", String.valueOf(mGenerators));
    properties.setProperty("chaos.spells", String.valueOf(mSpells));
    for (int i = 0; i < mState.length; ++i) {
      properties.setProperty("chaos.wizstate" + i, String.valueOf(mState[i]));
      properties.setProperty("chaos.teamstate" + i, String.valueOf(mSelectedTeam[i]));
    }
    properties.save();
  }

  private void cyclicSpellsIncrement(final int v) {
    mSpells += v;
    mSpells %= 100;
  }

  private void cyclicGeneratorsIncrement(final int v) {
    mGenerators += v;
    mGenerators %= 100;
  }

  /**
   * Handle the various keys that the selector understands.
   * @param e key event
   */
  @Override
  public void keyTyped(final KeyEvent e) {
    if (e != null) {
      final char c = Character.toLowerCase(e.getKeyChar());
      if (Character.toLowerCase(c) == '\n') {
        // Set up is complete.
        finalizeWizards();
        e.consume();
        mLock.setValue(true);
      } else if (c >= '0' && c <= '9') {
        mGenerators *= 10;
        cyclicGeneratorsIncrement(c - '0');
        drawGeneratorState();
        e.consume();
      }
    }
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    // These need to be here, because getKeyCode() is undefined in keyTyped()
    if (e != null) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_RIGHT:
          cyclicSpellsIncrement(1);
          drawSpellState();
          e.consume();
          break;
        case KeyEvent.VK_LEFT:
          cyclicSpellsIncrement(99);
          drawSpellState();
          e.consume();
          break;
        case KeyEvent.VK_UP:
          cyclicSpellsIncrement(10);
          drawSpellState();
          e.consume();
          break;
        case KeyEvent.VK_DOWN:
          cyclicSpellsIncrement(90);
          drawSpellState();
          e.consume();
          break;
        default:
          break;
      }
    }
  }

  private Button createUpButton(final int x, final int y) {
    return new Button(ImageLoader.upArrow(), x, y) {
      @Override
      public void act() {
        cyclicGeneratorsIncrement(1);
        drawGeneratorState();
      }
    };
  }

  private Button createDownButton(final int x, final int y) {
    return new Button(ImageLoader.downArrow(), x, y) {
      @Override
      public void act() {
        cyclicGeneratorsIncrement(99);
        drawGeneratorState();
      }
    };
  }

  private Button createSpellUpButton(final int x, final int y) {
    return new Button(ImageLoader.upArrow(), x, y) {
      @Override
      public void act() {
        cyclicSpellsIncrement(1);
        drawSpellState();
      }
    };
  }

  private Button createSpellDownButton(final int x, final int y) {
    return new Button(ImageLoader.downArrow(), x, y) {
      @Override
      public void act() {
        cyclicSpellsIncrement(99);
        drawSpellState();
      }
    };
  }

  private Button createLoadButton(final int x, final int y) {
    return new Button(Objects.requireNonNull(ImageLoader.getImage("chaos/resources/icons/load.png")), x, y) {
      @Override
      public void act() {
        mLoadRequest = true;
        mLock.setValue(true);
      }
    };
  }

  private Button createRavenButton(final int x, final int y) {
    return new Button(Objects.requireNonNull(ImageLoader.getImage("chaos/resources/icons/raven.png")), x, y) {
      @Override
      public void act() {
        mRavenRequest = true;
        mLock.setValue(true);
      }
    };
  }

  /**
   * Draw the static parts of the set up screen.
   * @return true if the set up screen was drawn.
   */
  private boolean drawSetUpScreen() {
    final Graphics g = mScreen.getGraphics();
    if (g == null) {
      return false;
    }
    int dy = mYOffset - mSep + 3;
    int dx = -3;
    final int tw = mSize + mSize / 4;
    g.drawImage(OFF_IMAGE, mXOffset + dx, dy, null);
    g.drawImage(OFF_IMAGE, mXSecondColumn + dx, dy, null);
    dx += mSep;
    g.drawImage(HUMAN_IMAGE, mXOffset + dx, dy, null);
    g.drawImage(HUMAN_IMAGE, mXSecondColumn + dx, dy, null);
    dx += mSep;
    g.drawImage(AI_IMAGE, mXOffset + dx, dy, null);
    g.drawImage(AI_IMAGE, mXSecondColumn + dx, dy, null);
    dy += 11 * mSep - 3;
    g.drawImage(mTM.getSpellTile(new Generator()), tw, dy, null);
    g.drawImage(SPELLS_IMAGE, mXSpellOffset, dy, null);
    g.setColor(Color.CYAN);
    dx += mXOffset;
    final Button generatorUpArrow = createUpButton(dx - tw, dy);
    generatorUpArrow.paint(g);
    mButtons.add(generatorUpArrow);
    final Button generatorDownArrow = createDownButton(dx - tw, dy + mSize / 2);
    generatorDownArrow.paint(g);
    mButtons.add(generatorDownArrow);
    final int rr = mSize * 4 + 1;
    final Button spellUpArrow = createSpellUpButton(mXSpellOffset + rr, dy);
    spellUpArrow.paint(g);
    mButtons.add(spellUpArrow);
    final Button spellDownArrow = createSpellDownButton(mXSpellOffset + rr, dy + mSize / 2);
    spellDownArrow.paint(g);
    mButtons.add(spellDownArrow);
    final Button load = createLoadButton(3 * mConfig.getPixelWidth() / 4, mScreen.getPowerUpYOffset() - mSize);
    load.paint(g);
    mButtons.add(load);
    final Button raven = createRavenButton(3 * mConfig.getPixelWidth() / 4 - 2 * mSize, mScreen.getPowerUpYOffset() - mSize);
    raven.paint(g);
    mButtons.add(raven);
    g.setColor(Color.YELLOW);
    g.setFont(mScreen.getPhaseFont());
    printString(g, "Java Chaos", (int) Math.round(0.1458 * mConfig.getPixelHeight()));
    printString(g, "by Sean A. Irvine", (int) Math.round(0.1666666 * mConfig.getPixelHeight()));
    printString(g, "(sairvin@gmail.com)", (int) Math.round(0.1875 * mConfig.getPixelHeight()));
    printString(g, "2008-2022", (int) Math.round(0.2083333 * mConfig.getPixelHeight()));
    printString(g, "v 4.0", (int) Math.round(0.22916666 * mConfig.getPixelHeight()));
    printString(g, "Inspired by Julian Gollop's Chaos", (int) Math.round(0.2708333 * mConfig.getPixelHeight()));
    for (int j = 0; j < mValidWizards; ++j) {
      drawWizardState(j);
    }
    drawGeneratorState();
    drawSpellState();
    g.dispose();
    return true;
  }

  /**
   * Perform the set up.
   * @return array of special start up features
   */
  public boolean[] setUp() {
    if (drawSetUpScreen()) {
      mLock.setValue(false);
      mScreen.addMouseMotionListener(this);
      mScreen.addMouseListener(this);
      mScreen.addKeyListener(this);
      mScreen.setContinueGadget(true);
      try {
        mLock.waitUntilTrue(0);
      } catch (final InterruptedException e) {
        // too bad
      }
      mScreen.removeKeyListener(this);
      mScreen.removeMouseListener(this);
      mScreen.removeMouseMotionListener(this);
      mScreen.blank(); // completely clear for game start
      // bells & whistles
      SOUND.playwait("chaos/resources/sound/misc/Yeah", Sound.SOUND_INTELLIGENT);
    }
    return new boolean[] {mLoadRequest, mRavenRequest};
  }
}
