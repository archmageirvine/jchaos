package chaos.selector;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import chaos.Configuration;
import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Realm;
import chaos.common.monster.FireDemon;
import chaos.common.wizard.Wizard;
import chaos.engine.CursorHelper;
import chaos.graphics.ChaosScreen;
import chaos.graphics.CursorName;
import chaos.graphics.InformationPanel;
import chaos.graphics.TileManager;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.NameUtils;
import chaos.util.RankingComparator;
import chaos.util.Sleep;
import irvine.tile.FadeEffect;
import irvine.tile.TileImage;
import irvine.util.graphics.Stipple;

/**
 * Selector that works directly on generic screen.
 * @author Sean A. Irvine
 */
public class GenericScreenSelector implements Selector, MouseMotionListener, MouseListener, KeyListener {

  /** Maximum number of spells to show on the screen. */
  private static final int MAX_TO_DISPLAY = 24;
  /** Number of columns in spell selection list. */
  private static final int COLS = 3;
  /** Used to indicate that no castable is selected. */
  private static final int NONE = -1;
  /** Used to indicate mouse is over the continue gadget. */
  private static final int CONT_GADGET = -2;
  /** The sound system. */
  private static final Sound SOUND = Sound.getSoundEngine();
  /** Random number generator. */
  private static final Random RANDOM = new Random();
  /** The screen this selector uses. */
  private transient ChaosScreen mScreen = null;
  /** Current array of castables. */
  private Castable[] mCastables = null;
  /** Records the last castable the mouse was over. */
  private int mOld = NONE;
  /** Tile width. */
  private final int mSize;
  /** Index of selected castable. */
  private int mSelected;
  /** Provides model locking. */
  protected transient BooleanLock mLock = new BooleanLock();
  /** Are we currently in information mode. */
  private transient BooleanLock mInfoMode = new BooleanLock(false);
  /** Wizard this selector is for. */
  private final Wizard mWizard;
  /** Horizontal spacing between spells in list. */
  private final int mHorizontalStep;
  /** Vertical spacing between spells in list. */
  private final int mVerticalStep;
  private transient TileManager mTileManager;
  private final Configuration mConfig;
  private boolean mReturnKeyAllowed = false;
  private final CursorHelper mKeyboardSelectLocation = new CursorHelper(COLS, MAX_TO_DISPLAY / COLS);
  private transient BufferedImage mEmptyImage;
  private transient Graphics mGraphics = null;
  private final Strategiser mStrategiser;
  private final int mHorizontalLimit;
  private boolean mLazySelection = false;

  /**
   * Construct a new selector for the given screen and tile manager.
   * @param wizard wizard making selection
   * @param config game configuration
   * @param tileManager tile manager
   * @param world the world
   * @param castMaster casting rules
   * @throws NullPointerException if parameter is null
   */
  public GenericScreenSelector(final Wizard wizard, final Configuration config, final TileManager tileManager, final World world, final CastMaster castMaster) {
    mConfig = config;
    mTileManager = tileManager;
    mSize = mTileManager.getWidth();
    mWizard = wizard;
    mHorizontalStep = mSize * 2;
    mHorizontalLimit = COLS * mHorizontalStep;
    mVerticalStep = mSize + mSize / 2;
    mEmptyImage = Stipple.stipple(0, 0, mSize, mSize).toBufferedImage();
    mStrategiser = new Strategiser(wizard, world, castMaster);
  }

  /**
   * Set the screen.
   * @param screen the screen
   */
  public void setScreen(final ChaosScreen screen) {
    mScreen = screen;
    mGraphics = screen.getGraphics();
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    mLock = new BooleanLock();
    mInfoMode = new BooleanLock();
    mEmptyImage = Stipple.stipple(0, 0, mSize, mSize).toBufferedImage();
    mTileManager = null;
  }

  /** Draw the highlighting graphical effect at a given offset. */
  private void highlight(final int c) {
    if (c > NONE && c < displayLimit()) {
      final int x = mScreen.getXRight() + mHorizontalStep * (c % COLS);
      final int y = mScreen.getYRight() + mVerticalStep * (c / COLS);
      mScreen.highlight(x, y, mSize);
    }
  }

  /** Draw an image in the spell selection area of the screen. */
  private void drawImage(final BufferedImage i, final int c, final Color color) {
    if (c != NONE) {
      synchronized (mGraphics) {
        final int x = mScreen.getXRight() + mHorizontalStep * (c % COLS);
        final int y = mScreen.getYRight() + mVerticalStep * (c / COLS);
        mGraphics.drawImage(i, x, y, null);
        if (color != null) {
          mGraphics.setColor(color);
          mGraphics.fillOval(x + mSize + 6, y + mSize / 2, 3, 3);
        }
      }
    }
  }

  /** Draw a spell in the spell selection area of the screen. */
  private void drawSpell(final int c, final Castable[] aiSelection) {
    if (c > NONE && c < displayLimit()) {
      if (mCastables[c] != null) {
        final Color color;
        if (aiSelection == null) {
          color = null;
        } else if (mCastables[c].equals(aiSelection[0])) {
          color = Color.GREEN;
        } else if (mCastables[c].equals(aiSelection[1])) {
          color = Color.RED;
        } else {
          color = null;
        }
        drawImage(mTileManager.getSpellTile(mCastables[c]), c, color);
      } else {
        drawImage(mEmptyImage, c, null);
      }
    }
  }

  private int displayLimit() {
    return Math.min(MAX_TO_DISPLAY, mCastables.length);
  }

  private static final String NO_SPELLS = "NO SPELLS!";

  private void writeNoSpellAvailable() {
    synchronized (mGraphics) {
      mGraphics.setColor(Color.YELLOW);
      mGraphics.setFont(mScreen.getPhaseFont());
      final FontMetrics fm = mGraphics.getFontMetrics();
      final int w = mScreen.getPhaseWidth() - (fm == null ? 0 : fm.stringWidth(NO_SPELLS));
      if (w >= 0) {
        mGraphics.drawString(NO_SPELLS, mScreen.getXPhaseOffset() + w / 2, mConfig.getPixelHeight() / 8);
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
  public void keyTyped(final KeyEvent e) {
  }

  @Override
  public void keyPressed(final KeyEvent e) {
  }

  /**
   * Return the castable index corresponding to the mouse position or NONE
   * if there is not corresponding castable.
   * @param e a mouse event
   * @return castable
   */
  private int pixelToCell(final MouseEvent e) {
    // if possible convert event position to castable
    final int ycell = e.getY() - mScreen.getYRight();
    final int xcell = e.getX() - mScreen.getXRight();
    if (xcell >= 0 && ycell >= 0 && xcell < mHorizontalLimit && ycell < mVerticalStep * (Math.min(mCastables.length, MAX_TO_DISPLAY) + COLS - 1) / COLS && (xcell % mHorizontalStep) < mSize && (ycell % mVerticalStep) < mSize) {
      final int i = COLS * (ycell / mVerticalStep) + (xcell / mHorizontalStep);
      if (i < Math.min(mCastables.length, MAX_TO_DISPLAY)) {
        return i;
      }
    }
    return NONE;
  }

  /**
   * User requested information on a spell from the list, so do our best to display it.
   * @param cell cell requested
   */
  protected void informationDisplay(final int cell) {
    if (cell >= 0 && cell < mCastables.length) {
      synchronized (mScreen.lock()) {
        mInfoMode.setValue(true);
        InformationPanel.informationDisplay(mScreen, mCastables[cell], mGraphics);
        try {
          // timeout after 2 minutes ... just in case the user is confused
          mInfoMode.waitUntilFalse(120000);
        } catch (final InterruptedException e) {
          // too bad
        } finally {
          // in case we exited via the timeout
          mInfoMode.setValue(false);
        }
      }
    }
  }

  private void showInformationDisplay(final int cell) {
    if (cell != NONE) {
      // Don't hold up the event handling queue when doing the information
      // display.  This is critical not only for performance, but because
      // we rely on this very queue to terminate the information window!
      final Thread t = new Thread(() -> {
        informationDisplay(cell);
        mScreen.blankCenter();
      });
      t.start();
    }
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    // handle a click while in information mode
    final boolean oldInfoState = mInfoMode.isTrue();
    if (oldInfoState) {
      mInfoMode.setValue(false);
      // Continue you to try and react to the click as if we were
      // not already in info mode.  This allows immediately reselect
      // of info mode or spell selection
    }
    final int cell = pixelToCell(e);
    if (cell != NONE && mCastables[cell] != null) {
      if (e.getButton() == MouseEvent.BUTTON1) {
        mSelected = cell; // remember clicked cell
        mLock.setValue(true);
      } else if (e.getButton() == MouseEvent.BUTTON3) {
        showInformationDisplay(cell);
      }
      e.consume(); // stop anyone else taking this event
    } else if (mScreen.isPositionInContinueGadget(e.getX(), e.getY())) {
      // user clicked continue gadget
      mLock.setValue(true);
    } else if (!oldInfoState) {
      // use clicked outside of expected area
      Sound.ding();
    }
  }

  @Override
  public void mouseMoved(final MouseEvent e) {
    final int c = pixelToCell(e);
    if (c == NONE && mScreen.isPositionInContinueGadget(e.getX(), e.getY())) {
      // mouse is over continue gadget
      if (mOld != CONT_GADGET) {
        mOld = CONT_GADGET;                // we are in continue
        mScreen.highlight((Realm) null);   // reset pentagram
        highlight(NONE);                   // don't highlight a spell
        mScreen.highlightContinue(true);
      }
      return;
    }
    if (mOld != c) {
      drawSpell(mOld, null);                    // unhighlight spell
      mScreen.highlight((Realm) null);    // reset pentagram
      mScreen.highlightContinue(false);   // unhighlight continue
      describeCastable(c);
      highlight(c); // highlight current cell
      mOld = c;     // and remember where it is
    }
  }

  private void describeCastable(final int cell) {
    if (cell == NONE || cell >= mCastables.length || mCastables[cell] == null) {
      mScreen.writeMessage("");
    } else {
      final Castable cast = mCastables[cell];
      mScreen.writeMessage(mWizard, cast, mTileManager);
      if (cast instanceof Actor) {
        mScreen.highlight(((Actor) cast).getRealm());
      } else {
        mScreen.highlight(Realm.NONE);
      }
    }
  }

  private boolean atLeastOneNonNull(final Castable[] castables) {
    for (final Castable c : castables) {
      if (c != null) {
        return true;
      }
    }
    return false;
  }

  private void writePhase(final String phase, final String sec, final BufferedImage image) {
    if (mScreen != null) {
      mScreen.writePhase(phase, sec, image);
    }
  }

  private Castable select(final Castable[] castables, final boolean noSelectionAllowed, final String action, final Castable[] aiSelection) {
    if (castables != null) {
      writePhase(action, mWizard.getPersonalName(), mTileManager.getSpellTile(mWizard));
      mReturnKeyAllowed = noSelectionAllowed;
      // select cross hair pointer
      mScreen.setCursor(CursorName.CROSS);
      mScreen.setContinueGadget(true);
      // draw 'em
      mCastables = Arrays.copyOf(castables, castables.length);
      if (atLeastOneNonNull(castables)) {
        for (int i = 0; i < Math.min(castables.length, MAX_TO_DISPLAY); ++i) {
          drawSpell(i, aiSelection);
        }
        if (!noSelectionAllowed) {
          mScreen.setContinueGadget(false);
        }
      } else {
        writeNoSpellAvailable();
        mReturnKeyAllowed = true;
      }
      // do selection
      mSelected = NONE;
      mKeyboardSelectLocation.reset();
      mLock.setValue(false);
      mScreen.addMouseMotionListener(this);
      mScreen.addMouseListener(this);
      mScreen.addKeyListener(this);
      try {
        mLock.waitUntilTrue(0);
      } catch (final InterruptedException e) {
        // too bad
      }
      mScreen.removeKeyListener(this);
      mScreen.removeMouseListener(this);
      mScreen.removeMouseMotionListener(this);
      // bells & whistles
      SOUND.playwait(RANDOM.nextInt(70) == 0
          ? "chaos/resources/sound/misc/Yeah"
          : "chaos/resources/sound/misc/Beep",
        Sound.SOUND_INTELLIGENT);
      return mSelected == NONE || mSelected >= mCastables.length ? null : mCastables[mSelected];
    }
    return null;
  }

  private int findCastable(final Castable[] castables, final Castable chosen) {
    for (int k = 0; k < castables.length; ++k) {
      if (castables[k] == chosen) {
        return k;
      }
    }
    return NONE;
  }

  private void fadeSelection(final Castable[] castables, final String result, final Castable chosen) {
    if (mScreen == null) {
      return;
    }
    if (chosen != null) {
      // Find the selected spell so we can fade it
      final int selected = findCastable(castables, chosen);
      mScreen.writeMessage(result + ": " + NameUtils.getTextName(chosen));
      if (selected != NONE) {
        final FadeEffect rot = new FadeEffect(new TileImage(mTileManager.getSpellTile(chosen)), Stipple.stipple(0, 0, mSize, mSize));
        TileImage i;
        while ((i = rot.next()) != null) {
          drawImage(i.toBufferedImage(), selected, null);
          Sleep.sleep(100);
        }
      }
    } else {
      mScreen.writeMessage("Selected: Nothing");
    }
    // tidy up screen, by restoring blue stipple on the right
    mScreen.blankRight();
    mScreen.highlight((Realm) null);
    mScreen.setContinueGadget(false);
  }

  @Override
  public Castable[] select(final Castable[] castables, final boolean texas) {
    mLazySelection = false;
    final Castable[] res = new Castable[2];
    final Castable[] aiSelection = mStrategiser.select(castables, true);
    final Castable c = select(castables, true, "SPELL SELECTION", aiSelection);
    res[0] = mLazySelection ? aiSelection[0] : c;
    fadeSelection(castables, "Selected", res[0]);
    if (texas) {
      markUsed(castables, res[0], res[0] != null);
      if (mLazySelection) {
        res[1] = aiSelection[1];
      } else {
        final Castable aiDiscard = res[0] == aiSelection[1] ? aiSelection[0] : aiSelection[1];
        final Castable discard = select(castables, false, "DISCARDING", aiSelection);
        // There is one really evil thing that can happen here.  The player
        // might have chosen the AI preference for discard, then pressed lazy
        // select.  If this is the case punish the player by discarding the
        // best spell.
        res[1] = mLazySelection ? aiDiscard : discard;
      }
      fadeSelection(castables, "Discarded", res[1]);
    }
    return res;
  }

  private void markUsed(final Castable[] castables, final Castable c, final boolean b) {
    if (b) {
      for (int j = 0; j < castables.length; ++j) {
        if (castables[j] == c) {
          castables[j] = null;
          break;
        }
      }
    }
  }

  private boolean mSawF = false; // for the fire demon easter egg

  @Override
  public void keyReleased(final KeyEvent e) {
    if (mInfoMode.isTrue()) {
      e.consume();
      mInfoMode.setValue(false);
    } else {
      final int prevPosition = mKeyboardSelectLocation.getPosition();
      final int code = e.getKeyCode();
      if (code == KeyEvent.VK_F) {
        mSawF = true;
        e.consume();
      } else if (mKeyboardSelectLocation.update(e)) {
        e.consume();
        if (mKeyboardSelectLocation.getPosition() >= displayLimit()) {
          mKeyboardSelectLocation.setPosition(0);
        }
        drawSpell(prevPosition, null); // unhighlight previous
        highlight(mKeyboardSelectLocation.getPosition());
        describeCastable(mKeyboardSelectLocation.getPosition());
        if (mSawF && code == KeyEvent.VK_D && mCastables.length > 0) {
          mCastables[0] = new FireDemon();
          mSelected = 0;
          mLock.setValue(true);
        }
      } else {
        e.consume();
        switch (code) {
          case KeyEvent.VK_ENTER:
          case KeyEvent.VK_S:
          case KeyEvent.VK_NUMPAD5:
          case KeyEvent.VK_BEGIN:
            if (prevPosition != NONE && prevPosition < mCastables.length && mCastables[prevPosition] != null) {
              mSelected = prevPosition;
              mLock.setValue(true);
            } else if (mReturnKeyAllowed) {
              mLock.setValue(true);
            } else {
              Sound.ding();
            }
            break;
          case KeyEvent.VK_I:
            showInformationDisplay(prevPosition);
            break;
          case KeyEvent.VK_L: // lazy key spell selection
            mLazySelection = true;
            mSelected = 0;
            mLock.setValue(true);
            break;
          default:
            //System.out.println("Saw " + code);
            break;
        }
        mSawF = false;
      }
    }
  }

  @Override
  public Castable[] selectBonus(final Castable[] spells, final int count) {
    // First sort them by ranking.  This helps ensure that if the player has a
    // very large number of bonus spells that the best ones are displayed first.
    mKeyboardSelectLocation.reset();
    Arrays.sort(spells, RankingComparator.REVERSE_COMPARATOR);
    final ArrayList<Castable> res = new ArrayList<>();
    final int limit = Math.min(count, MAX_TO_DISPLAY);
    for (int k = 1; k <= limit; ++k) {
      mScreen.writeMessage("Selecting " + k + " of " + count + " bonus spells.");
      // Call the GUI to make the selection
      final Castable c = select(spells, true, "BONUS SELECTION", null);
      if (c == null) {
        // User opted not to select a bonus spell, assume they want to exit bonus
        // spell selection all together.
        break;
      }
      // Add the selected spell to the result set
      fadeSelection(spells, "Selected", c);
      res.add(c);
      // Remove it from the list of candidates
      for (int j = 0; j < spells.length; ++j) {
        if (spells[j] == c) {
          spells[j] = null;
          break;
        }
      }
    }
    mScreen.blankRight();
    return res.toArray(new Castable[0]);
  }
}
