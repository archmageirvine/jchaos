package chaos.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import chaos.Chaos;
import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.Realm;
import chaos.common.wizard.Wizard;
import chaos.graphics.ChaosScreen;
import chaos.graphics.CursorName;
import chaos.graphics.InfoDisplay;
import chaos.graphics.InformationPanel;
import chaos.graphics.TileManager;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.DefaultEventGenerator;
import chaos.util.HighlightEvent;
import chaos.util.NameUtils;
import chaos.util.ShootIconEvent;
import chaos.util.TextEvent;
import chaos.util.WingsIconEvent;

/**
 * An engine for GUI controlled human movement.
 * @author Sean A. Irvine
 */
public class HumanEngine extends DefaultEventGenerator implements PlayerEngine, MouseListener, MouseMotionListener, KeyListener, Serializable {

  private static final int NOTHING_SELECTED = -1;
  private static final int MOUSE_IS_OVER_CONTINUE_BUTTON = -2;
  private static final Sound SOUND = Sound.getSoundEngine();

  private final World mWorld;
  private final MoveMaster mMoveMaster;
  private final CastMaster mCastMaster;
  private final HumanMoveHelper mMoveHelper;
  private final int mWidthBits;
  private final AiEngine mAiEngine;
  private transient ChaosScreen mScreen = null;
  private transient BooleanLock mDone = new BooleanLock();
  private transient InfoDisplay mInfoDisplay = null;
  private transient BooleanLock mInfoMode = new BooleanLock(false);
  private Wizard mWizard = null;
  private Castable mCastable = null;
  private Caster mCaster = null;
  private Cell mCasterCell = null;
  private volatile boolean mCastAborted = false;
  private boolean mUseAutoSelect = false;
  private HumanEngineState mState = HumanEngineState.AWAITING_SELECT;
  private int mOldCell = NOTHING_SELECTED;
  private int mActive = NOTHING_SELECTED;
  private int mWingsCell = NOTHING_SELECTED;
  private int mShootCell = NOTHING_SELECTED;
  private CursorHelper mCursorHelper;

  /**
   * Human GUI engine.
   * @param widthBits width of images used
   * @param chaos underlying game
   */
  public HumanEngine(final Chaos chaos, final int widthBits) {
    mWidthBits = widthBits;
    mWorld = chaos.getWorld();
    mMoveMaster = chaos.getMoveMaster();
    mCastMaster = chaos.getCastMaster();
    mAiEngine = new AiEngine(mWorld, mMoveMaster, mCastMaster);
    mCursorHelper = new CursorHelper(mWorld.width(), mWorld.height());
    mMoveHelper = new HumanMoveHelper(mWorld);
  }

  /**
   * Set the screen this human engine should use.
   * @param screen the screen
   * @param tileManager tile manager
   * @exception NullPointerException if <code>screen</code> is null
   */
  public void setScreen(final ChaosScreen screen, final TileManager tileManager) {
    if (screen == null) {
      throw new NullPointerException();
    }
    mScreen = screen;
    mInfoDisplay = new InfoDisplay(mScreen, mScreen.getGraphics(), tileManager, mWorld);
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    mDone = new BooleanLock();
    mInfoMode = new BooleanLock();
  }

  // Return the cell number corresponding to the position of the mouse or -1 for an invalid cell.
  private int pixelToCell(final MouseEvent e) {
    final int ycell = (e.getY() - mScreen.getYOffset()) >> mWidthBits;
    final int xcell = (e.getX() - mScreen.getXOffset()) >> mWidthBits;
    if (xcell >= 0 && ycell >= 0 && xcell < mWorld.width() && ycell < mWorld.height()) {
      return ycell * mWorld.width() + xcell;
    } else {
      return NOTHING_SELECTED;
    }
  }

  private void cleanUp() {
    mWingsCell = NOTHING_SELECTED;
    mShootCell = NOTHING_SELECTED;
    notify(new WingsIconEvent(NOTHING_SELECTED));
    notify(new ShootIconEvent(NOTHING_SELECTED));
    notify(new HighlightEvent(null));
    notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
  }

  private void informationDisplay(final int cell) {
    final Actor a = mWorld.actor(cell);
    if (a != null && !(a instanceof Wizard)) {
      mInfoMode.setValue(true);
      synchronized (mScreen.lock()) {
        InformationPanel.informationDisplay(mScreen, a, mScreen.getGraphics());
        try {
          mInfoMode.waitUntilFalse(120000); // 2 minutes
        } catch (final InterruptedException e) {
          mInfoMode.setValue(false);
        } finally {
          mInfoMode.setValue(false);
        }
      }
    }
  }

  private void describeMove(final int cell) {
    final Monster m = (Monster) mWorld.actor(cell);
    if (!mMoveMaster.isEngaged(mWizard, cell) && m.is(PowerUps.FLYING)) {
      mScreen.setCursor(CursorName.WINGS);
    }
    mMoveHelper.setTargets(mWorld.actor(cell), cell, mWizard, mMoveMaster);
    notify(new HighlightEvent(mMoveHelper.getPossiblePlacesToMove()));
    notify(new TextEvent(HumanEngineHelper.describeMove(mWorld, mMoveMaster, mWizard, cell)));
  }

  @Override
  public void mousePressed(final MouseEvent e) { }
  @Override
  public void mouseReleased(final MouseEvent e) { }
  @Override
  public void mouseEntered(final MouseEvent e) { }
  @Override
  public void mouseExited(final MouseEvent e) { }
  @Override
  public void mouseDragged(final MouseEvent e) { }
  @Override
  public void keyTyped(final KeyEvent e) { }

  private void handleButton3() {
    cleanUp();
    switch (mState) {
    case SHOOTING:
      notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
      notify(new TextEvent("Ranged combat aborted"));
      mountedShooting();
      break;
    case MOUNTED_SHOOTING:
      mState = HumanEngineState.AWAITING_SELECT;
      mScreen.setCursor(CursorName.CROSS);
      mActive = NOTHING_SELECTED;
      notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
      notify(new TextEvent("Ranged combat aborted"));
      break;
    case MOVE_IN_PROGRESS:
      notify(new TextEvent("Move aborted"));
      shooting();
      break;
    case DISMOUNTING:
      mState = HumanEngineState.AWAITING_SELECT;
      mScreen.setCursor(CursorName.CROSS);
      mActive = NOTHING_SELECTED;
      notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
      notify(new TextEvent("Dismount aborted"));
      break;
    case CAST_CLICK:
      mState = HumanEngineState.IDLE;
      mScreen.setCursor(CursorName.BLANK);
      notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
      notify(new TextEvent("Cast aborted"));
      mCastAborted = true;
      mDone.setValue(true); // last to prevent race condition
      break;
    default:
      break;
    }
  }

  private void updateInfoDisplay(final Actor a, final int panelNumber) {
    synchronized (mInfoDisplay) {
      mInfoDisplay.showInfo(a, panelNumber);
    }
  }

  private void shootingSetUpInfo(final Actor a, final String text) {
    mShootCell = mActive;
    mScreen.setCursor(CursorName.SHOOT);
    updateInfoDisplay(a, 0);
    notify(new TextEvent(text));
  }

  private void setUpForShot() {
    mState = HumanEngineState.SHOOTING;
    final Actor a = mWorld.actor(mActive);
    if (a instanceof Monster) {
      final int range = a.get(Attribute.RANGE);
      final Set<Integer> targets = new HashSet<>();
      for (final Cell c : mWorld.getCells(mActive, 1, range, !a.is(PowerUps.ARCHERY))) {
        final int cn = c.getCellNumber();
        if (mMoveMaster.isShootable(mWizard, mActive, cn)) {
          targets.add(cn);
        }
      }
      notify(new HighlightEvent(targets));
    }
    shootingSetUpInfo(a, "Shooting");
  }

  private void setUpForMountedShot() {
    mState = HumanEngineState.MOUNTED_SHOOTING;
    final Actor a = ((Conveyance) mWorld.actor(mActive)).getMount();
    if (a instanceof Monster) {
      final Monster m = (Monster) a;
      final int range = m.get(Attribute.RANGE);
      final Set<Integer> targets = new HashSet<>();
      for (final Cell c : mWorld.getCells(mActive, 1, range, !m.is(PowerUps.ARCHERY))) {
        final int cn = c.getCellNumber();
        if (mMoveMaster.isShootableConveyance(mWizard, mActive, cn)) {
          targets.add(cn);
        }
      }
      notify(new HighlightEvent(targets));
    }
    shootingSetUpInfo(a, "Mounted shooting");
  }

  private boolean mountedShooting() {
    if (mMoveMaster.isShootableConveyance(mWizard, mActive)) {
      setUpForMountedShot();
      return true;
    } else {
      mActive = NOTHING_SELECTED;
      mState = HumanEngineState.AWAITING_SELECT;
      mScreen.setCursor(CursorName.CROSS);
      notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
      notify(new TextEvent(""));
      return false;
    }
  }

  private boolean shooting() {
    if (mMoveMaster.isShootable(mWizard, mActive)) {
      setUpForShot();
      return true;
    } else {
      return mountedShooting();
    }
  }

  private boolean updateSelection(final int cell) {
    if (mMoveMaster.isMovable(mWizard, cell)) {
      mActive = cell;
      mState = HumanEngineState.MOVE_IN_PROGRESS;
      updateInfoDisplay(mWorld.actor(cell), 0);
      notify(new CellEffectEvent(cell, CellEffectType.HIGHLIGHT_EVENT));
      describeMove(cell);
      return true;
    } else if (mMoveMaster.isShootable(mWizard, cell)) {
      mActive = cell;
      setUpForShot();
      notify(new CellEffectEvent(cell, CellEffectType.HIGHLIGHT_EVENT));
      return true;
    } else if (mMoveMaster.isShootableConveyance(mWizard, cell)) {
      mActive = cell;
      setUpForMountedShot();
      notify(new CellEffectEvent(cell, CellEffectType.HIGHLIGHT_EVENT));
      return true;
    }
    return false;
  }

  private void updateAutoSelection() {
    if (mUseAutoSelect) {
      final int s = mWorld.size();
      final int last = (mActive + s) % s; // ensure positive for loop termination
      int autoCell = mActive;
      cleanUp(); // reset state in preparation for new selection
      do {
        autoCell = (autoCell + 1) % s;
        if (updateSelection(autoCell)) {
          SOUND.playwait("chaos/resources/sound/misc/Select", Sound.SOUND_INTELLIGENT);
          return;
        }
      } while (autoCell != last);
      mActive = NOTHING_SELECTED;
      mState = HumanEngineState.AWAITING_SELECT;
      mUseAutoSelect = false;
    }
  }

  private void performDismount(final int cell) {
    switch (mMoveMaster.dismount(mWizard, mActive, cell)) {
    case MoveMaster.ILLEGAL:
      notify(new TextEvent("Cannot dismount to that cell"));
      break;
    case MoveMaster.OK:
      Sound.beep();
      mActive = cell; // so that shooting will work
      shooting();
      break;
    case MoveMaster.COMBAT_FAILED:
    case MoveMaster.INVULNERABLE:
      Sound.beep();
      shooting();
      break;
    default:
      break;
    }
  }

  private int smartWalkingMove(final int source, final int destination) {
    // User has indicated move of active creature to specified cell
    final int prior = mMoveHelper.prior(destination);
    if (source != destination && prior >= 0 && prior != source && prior != destination) {
      final int down = smartWalkingMove(source, prior);
      if (down == MoveMaster.ILLEGAL) {
        return MoveMaster.ILLEGAL;
      }
    }
    // We have moved from source to prior already, now do the next step
    return mMoveMaster.move(mWizard, prior, destination);
  }

  private boolean performMoveInProgress(final int cell) {
    cleanUp();
    mScreen.setCursor(CursorName.BLANK);
    final Actor source = mWorld.actor(mActive);
    final int moveState = cell >= 0 && mMoveHelper.distance(cell) >= 0 ? smartWalkingMove(mActive, cell) : mMoveMaster.move(mWizard, mActive, cell);
    mScreen.setCursor(CursorName.CROSS);
    switch (moveState) {
    case MoveMaster.ILLEGAL:
      notify(new TextEvent("Cannot move to that cell"));
      return false;
    case MoveMaster.PARTIAL:
      notify(new HighlightEvent(null));
      Sound.beep();
      mActive = cell;
      notify(new CellEffectEvent(cell, CellEffectType.HIGHLIGHT_EVENT));
      describeMove(cell);
      return false;
    case MoveMaster.OK:
      updateInfoDisplay(mWorld.actor(cell), 1);
      Sound.beep();
      // Usually the mover will now be in cell, but this might not be the case
      // if the mover and no movement or the target cell was an elemental. Cf. Bug#353
      // Handling of promotion here also dubious
      if (source.equals(mWorld.actor(cell))) {
        mActive = cell;
      } else {
        final Actor d = mWorld.actor(cell);
        if (d instanceof Conveyance && source.equals(((Conveyance) d).getMount())) {
          // Looks like a successful mount happened, cascade down to shooting from the mount
          mActive = cell;
          mountedShooting();
        }
        // Heuristic checking for situation where a move resulted in promotion
        if (source instanceof Promotion) {
          final Actor now = mWorld.actor(cell);
          if (now != null && now.getOwner() == source.getOwner() && ((Promotion) source).promotion().equals(now.getClass())) {
            mActive = cell;
          }
        }
      }
      break;
    case MoveMaster.COMBAT_FAILED:
    case MoveMaster.INVULNERABLE:
      if (!source.equals(mWorld.actor(mActive))) {
        // Although the combat etc. might have failed, the creature might have moved.
        final Cell newLocation = mWorld.getCell(source);
        if (newLocation != null) {
          mActive = newLocation.getCellNumber();
        }
      }
      updateInfoDisplay(mWorld.actor(cell), 1);
      Sound.beep();
      shooting();
      break;
    default:
      break;
    }
    if (!shooting()) {
      updateAutoSelection();
      return true;
    }
    return false;
  }

  private boolean isActiveFlying() {
    final Actor a = mWorld.actor(mActive);
    return a != null && a.is(PowerUps.FLYING) && !mMoveMaster.isEngaged(mWizard, mActive);
  }

  private boolean updateKeyboardControlledCursorAndMoveIfAppropriate(final int keyCode) {
    mUseAutoSelect = true;
    if (mActive != NOTHING_SELECTED) {
      final int oldcell;
      if (mState == HumanEngineState.CAST_CLICK) {
        oldcell = mShootCell;
      } else if (mWingsCell != NOTHING_SELECTED && isActiveFlying()) {
        oldcell = mWingsCell;
      } else if ((mState == HumanEngineState.SHOOTING || mState == HumanEngineState.MOUNTED_SHOOTING) && mShootCell != NOTHING_SELECTED) {
        oldcell = mShootCell;
      } else {
        oldcell = mActive;
      }
      mCursorHelper.setPosition(oldcell);
      final boolean accepted = mCursorHelper.update(keyCode);
      if (!accepted) {
        return false;
      }
      final int cell = mCursorHelper.getPosition();
      if (cell != mOldCell) {
        updateInfoDisplay(mWorld.actor(cell), 1);
        mOldCell = cell;
      }
      if (mState == HumanEngineState.DISMOUNTING) {
        performDismount(cell);
      } else if (mState == HumanEngineState.SHOOTING || mState == HumanEngineState.MOUNTED_SHOOTING || mState == HumanEngineState.CAST_CLICK) {
        mShootCell = cell;
        notify(new ShootIconEvent(cell));
      } else if (isActiveFlying()) {
        mWingsCell = cell;
        notify(new WingsIconEvent(cell));
      } else {
        performMoveInProgress(cell);
      }
      return true;
    }
    return false;
  }

  private void performShot(final int cell) {
    mScreen.setCursor(CursorName.BLANK);
    notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
    if (mMoveMaster.shoot(mWizard, mActive, cell) == MoveMaster.OK) {
      updateInfoDisplay(mWorld.actor(cell), 1);
      cleanUp();
      shooting(); // Allow for quickshot power up
    } else {
      mScreen.setCursor(CursorName.SHOOT);
    }
  }

  private void performMountedShot(final int cell) {
    mScreen.setCursor(CursorName.BLANK);
    notify(new CellEffectEvent(NOTHING_SELECTED, CellEffectType.HIGHLIGHT_EVENT));
    if (mMoveMaster.shootFromConveyance(mWizard, mActive, cell) == MoveMaster.OK) {
      updateInfoDisplay(mWorld.actor(cell), 1);
      cleanUp();
      mountedShooting(); // Allow for quickshot power up
    } else {
      mScreen.setCursor(CursorName.SHOOT);
    }
  }

  private void performCast(final int cell) {
    if (mCastable instanceof FreeCastable) {
      mScreen.setCursor(CursorName.BLANK);
      ((FreeCastable) mCastable).cast(mWorld, mCaster, mCasterCell);
    } else if (!mCastMaster.isLegalCast(mWorld.getWizardManager().getWizard(mCaster), mCastable, mCasterCell.getCellNumber(), cell)) {
      notify(new TextEvent("Cannot cast in that cell"));
      return;
    } else {
      mScreen.setCursor(CursorName.BLANK);
      notify(new HighlightEvent(null));
      mCastable.cast(mWorld, mCaster, mWorld.getCell(cell), mCasterCell);
    }
    mState = HumanEngineState.IDLE;
    mDone.setValue(true);
    cleanUp();
  }

  private void prepareToDismount() {
    mState = HumanEngineState.DISMOUNTING;
    cleanUp();
    mScreen.setCursor(CursorName.DISMOUNT);
    notify(new TextEvent("Dismounting"));
  }

  private boolean checkForDismount(final int cell) {
    final Actor mount = mWorld.getCell(cell).getMount();
    if (mount != null && !mount.isMoved() && mount.getOwner() == mWizard.getOwner()) {
      mActive = cell;
      prepareToDismount();
      return true;
    }
    return false;
  }

  private void handleButton1(final MouseEvent e) {
    if (mScreen.isPositionInContinueGadget(e.getX(), e.getY())) {
      mState = HumanEngineState.IDLE;
      mDone.setValue(true);
      cleanUp();
      e.consume();
    } else {
      final int cell = pixelToCell(e);
      if (cell >= 0) {
        switch (mState) {
        case AWAITING_SELECT:
          // Order of conditions in next inequality is critcal
          if (!updateSelection(cell) && !checkForDismount(cell)) {
            notify(new TextEvent("Cannot move that object"));
          }
          break;
        case SHOOTING:
          performShot(cell);
          break;
        case MOUNTED_SHOOTING:
          performMountedShot(cell);
          updateAutoSelection();
          break;
        case MOVE_IN_PROGRESS:
          if (cell != mActive || !checkForDismount(cell)) {
            performMoveInProgress(cell);
          }
          break;
        case DISMOUNTING:
          cleanUp();
          performDismount(cell);
          break;
        case CAST_CLICK:
          performCast(cell);
          break;
        default:
          break;
        }
        e.consume();
      }
    }
  }

  @Override
  public void mouseClicked(final MouseEvent e) {
    if (mInfoMode.isTrue()) {
      e.consume();
      mInfoMode.setValue(false);
    } else if (e.getButton() == MouseEvent.BUTTON1) {
      handleButton1(e);
    } else if (e.getButton() == MouseEvent.BUTTON3) {
      handleButton3();
    }
  }

  @Override
  public void mouseMoved(final MouseEvent e) {
    final int c = pixelToCell(e);
    if (c == NOTHING_SELECTED && mScreen.isPositionInContinueGadget(e.getX(), e.getY())) {
      if (mOldCell != MOUSE_IS_OVER_CONTINUE_BUTTON) {
        mOldCell = MOUSE_IS_OVER_CONTINUE_BUTTON;      // we are in continue
        mScreen.highlight((Realm) null);     // reset pentagram
        mScreen.highlightContinue(true);
      }
    } else {
      if (c != mOldCell) {
   //     new Thread(() -> {
          updateInfoDisplay(mWorld.actor(c), 1);
          mOldCell = c;
     //   }).start();
      }
      if (mState != HumanEngineState.MOVE_IN_PROGRESS && mState != HumanEngineState.SHOOTING && mState != HumanEngineState.MOUNTED_SHOOTING) {
        mScreen.highlightContinue(false); // unhighlight continue
      }
    }
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    if (mInfoMode.isTrue()) {
      mInfoMode.setValue(false);
      e.consume();
      return;
    }
    final int ch = e.getKeyCode();
    if (ch == KeyEvent.VK_ENTER) {
      e.consume();
      // Casting free castables with return
      if (mState == HumanEngineState.CAST_CLICK) {
        performCast(mCastable instanceof FreeCastable ? 0 : mShootCell);
      } else {
        // end-of-turn
        mState = HumanEngineState.IDLE;
        mDone.setValue(true);
        cleanUp();
      }
    } else if (ch == KeyEvent.VK_L) { // Automatically complete the move
      e.consume();
      mAiEngine.moveAll(mWizard);
      mState = HumanEngineState.IDLE;
      mDone.setValue(true);
      cleanUp();
    }
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (mInfoMode.isFalse()) {
      final int code = e.getKeyCode();
      if (updateKeyboardControlledCursorAndMoveIfAppropriate(code)) {
        e.consume();
        if (code == KeyEvent.VK_PERIOD && mState != HumanEngineState.CAST_CLICK && mUseAutoSelect) {
          mUseAutoSelect = false;
          cleanUp();
          mState = HumanEngineState.AWAITING_SELECT;
        }
      } else {
        switch (code) {
          case KeyEvent.VK_M: // dismount request
            e.consume();
            final Actor mount;
            if ((mState == HumanEngineState.MOVE_IN_PROGRESS || mState == HumanEngineState.MOUNTED_SHOOTING) && (mount = mWorld.getCell(mActive).getMount()) != null && !mount.isMoved() && mount.getOwner() == mWizard.getOwner()) {
              prepareToDismount();
            }
            break;
          case KeyEvent.VK_S:
          case KeyEvent.VK_NUMPAD5:
          case KeyEvent.VK_BEGIN:
            e.consume();
            if (mState == HumanEngineState.CAST_CLICK) {
              performCast(mCastable instanceof FreeCastable ? 0 : mShootCell);
              break;
            }
            mUseAutoSelect = true;
            if (mState == HumanEngineState.MOVE_IN_PROGRESS) {
              if (isActiveFlying()) {
                if (!performMoveInProgress(mWingsCell)) {
                  break; // Do not update selection for partial moves or already updated.
                }
              } else {
                HumanEngineHelper.setMoved(mWorld.actor(mActive));
              }
            } else if (mState == HumanEngineState.SHOOTING) {
              if (mShootCell != mActive) {
                performShot(mShootCell);
              } else {
                HumanEngineHelper.setShot(mWorld.actor(mActive));
              }
            } else if (mState == HumanEngineState.MOUNTED_SHOOTING) {
              if (mShootCell != mActive) {
                performMountedShot(mShootCell);
              } else {
                HumanEngineHelper.setShot(((Conveyance) mWorld.actor(mActive)).getMount());
              }
            } else if (mState == HumanEngineState.DISMOUNTING) {
              final Actor a = mWorld.actor(mActive);
              if (a instanceof Conveyance) {
                HumanEngineHelper.setMoved(((Conveyance) a).getMount());
              }
            }
            updateAutoSelection();
            break;
          case KeyEvent.VK_SPACE:
            e.consume();
            if (mState != HumanEngineState.CAST_CLICK) {
              mUseAutoSelect = true;
              updateAutoSelection();
            }
            break;
          case KeyEvent.VK_P:
            e.consume();
            if (mOldCell != NOTHING_SELECTED) {
              // Don't hold up the event handling queue when doing the information
              // display.  This is critical not only for performance, but because
              // we rely on this very queue to terminate the information window!
              final Thread t = new Thread(() -> informationDisplay(mOldCell));
              t.start();
            }
            break;
          default:
            break;
        }
      }
    }
  }

  private void handleTheHuman() {
    mDone.setValue(false);
    mScreen.addMouseListener(this);
    mScreen.addMouseMotionListener(this);
    mScreen.addKeyListener(this);
    mScreen.setContinueGadget(true);
    try {
      mDone.waitUntilTrue(1000000);
    } catch (final InterruptedException e) {
      // ok
    }
    mScreen.setContinueGadget(false);
    mScreen.removeKeyListener(this);
    mScreen.removeMouseMotionListener(this);
    mScreen.removeMouseListener(this);
    synchronized (mInfoDisplay) {
      mScreen.blankRight();
    }
    mScreen.highlight((Realm) null);
  }

  private Set<Integer> computeValidTargets(final int range) {
    if (mCastable instanceof FreeCastable) {
      return null;
    }
    final Set<Integer> targets = new HashSet<>();
    final int cc = mCasterCell.getCellNumber();
    final Wizard cw = mWorld.getWizardManager().getWizard(mCaster);
    for (final Cell c : mWorld.getCells(cc, 1, range, false)) {
      final int cn = c.getCellNumber();
      if (mCastMaster.isLegalCast(cw, mCastable, cc, cn)) {
        targets.add(cn);
      }
    }
    return targets;
  }

  @Override
  public boolean cast(final Caster caster, final Castable castable, final Cell cell) {
    if (cell == null) {
      return false;
    }
    mCastable = castable;
    mCaster = caster;
    mCasterCell = cell;
    final int range = HumanEngineHelper.getCastRange(castable, caster);
    notify(new HighlightEvent(computeValidTargets(range)));
    mActive = cell.getCellNumber();
    mShootCell = cell.getCellNumber();
    mState = HumanEngineState.CAST_CLICK;
    mCastAborted = false;
    mScreen.setCursor(CursorName.CAST);
    notify(new CellEffectEvent(cell, CellEffectType.HIGHLIGHT_EVENT));
    notify(new TextEvent(NameUtils.getTextName(castable) + " [" + range + "]"));
    handleTheHuman();
    cleanUp();
    return !mCastAborted;
  }

  @Override
  public void moveAll(final Wizard wizard) {
    mWizard = wizard;
    mState = HumanEngineState.AWAITING_SELECT;
    mUseAutoSelect = false;
    mScreen.setCursor(CursorName.CROSS);
    handleTheHuman();
  }
}
