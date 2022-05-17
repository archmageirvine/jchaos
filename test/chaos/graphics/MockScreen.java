package chaos.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Shape;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Realm;

/**
 * A screen for testing.
 *
 * @author Sean A. Irvine
 */
public class MockScreen implements ChaosScreen {

  private final StringBuilder mHistory = new StringBuilder();
  private MockGraphics mGraphics;

  /**
   * Construct a new mock screen with specified mock graphics.
   *
   * @param g mock graphics
   */
  public MockScreen(final MockGraphics g) {
    mGraphics = g;
  }

  /** Construct a new mock screen with a new mock graphics. */
  public MockScreen() {
    this(new MockGraphics());
  }

  void setGraphics(final MockGraphics g) {
    mGraphics = g;
  }

  @Override
  public Object lock() {
    return this;
  }

  @Override
  public void blank() {
    synchronized (mHistory) {
      mHistory.append("blank()#");
    }
  }

  @Override
  public void blankRight() {
    synchronized (mHistory) {
      mHistory.append("blankRight()#");
    }
  }

  @Override
  public void blankCenter() {
  }

  @Override
  public int getXOffset() {
    return 0;
  }

  @Override
  public int getYOffset() {
    return 0;
  }

  @Override
  public int getXRight() {
    return 500;
  }

  @Override
  public int getYRight() {
    return 20;
  }

  @Override
  public int getMainWidth() {
    return 500;
  }

  @Override
  public int getMainHeight() {
    return 500;
  }

  @Override
  public int getRightWidth() {
    return 100;
  }

  @Override
  public int getRightHeight() {
    return 500;
  }

  @Override
  public int getPowerUpXOffset() {
    return 33;
  }

  @Override
  public int getPowerUpYOffset() {
    return 37;
  }

  @Override
  public void fillMain(final Color color) {
    synchronized (mHistory) {
      mHistory.append("fillMain(").append(color).append(")#");
    }
  }

  @Override
  public void drawCell(final Image image, final int x, final int y) {
    synchronized (mHistory) {
      mHistory.append("drawCell(")
        .append(x)
        .append(',')
        .append(y)
        .append(")#");
    }
  }

  @Override
  public void writeMessage(final String message) {
    synchronized (mHistory) {
      mHistory.append("writeMessage(").append(message).append(")#");
    }
  }

  @Override
  public void writeMessage(final Caster caster, final Castable castable, final TileManager tm) {
    synchronized (mHistory) {
      mHistory.append("writeMessage(")
        .append(caster.getClass().getName())
        .append(',')
        .append(castable.getClass().getName())
        .append(")#");
    }
  }

  @Override
  public void writePhase(final String phase, final String player, final BufferedImage image) {
    synchronized (mHistory) {
      mHistory.append("writePhase(")
        .append(phase)
        .append(',')
        .append(player)
        .append(")#");
    }
  }

  @Override
  public void writePhase(final String phase, final String player) {
    writePhase(phase, player, null);
  }

  @Override
  public void setCursor(final CursorName name) {
    synchronized (mHistory) {
      mHistory.append("setCursor(").append(name).append(")#");
    }
  }

  @Override
  public void highlight(final int x, final int y, final int w, final int h) {
    synchronized (mHistory) {
      mHistory.append("highlight(")
        .append(x)
        .append(',')
        .append(y)
        .append(',')
        .append(w)
        .append(',')
        .append(h)
        .append(")#");
    }
  }

  @Override
  public void highlight(final int x, final int y, final int w) {
    highlight(x, y, w, w);
  }

  @Override
  public void lightHighlight(final int x, final int y, final int w) {
    synchronized (mHistory) {
      mHistory.append("lightHighlight(")
        .append(x)
        .append(',')
        .append(y)
        .append(',')
        .append(w)
        .append(")#");
    }
  }

  @Override
  public void highlight(final Realm realm) {
    synchronized (mHistory) {
      mHistory.append("highlight(").append(realm).append(")#");
    }
  }

  @Override
  public void highlight(final Realm[] realm) {
    synchronized (mHistory) {
      mHistory.append("highlight(realms)#");
    }
  }

  @Override
  public void setContinueGadget(final boolean state) {
    synchronized (mHistory) {
      mHistory.append("setContinueGadget(").append(state).append(")#");
    }
  }

  /** Position of continue gadget used in mocking. */
  public static final int CONTINUE_X = 580;
  /** Position of continue gadget used in mocking. */
  public static final int CONTINUE_Y = 448;

  @Override
  public boolean isPositionInContinueGadget(final int x, final int y) {
    return x == CONTINUE_X && y == CONTINUE_Y;
  }

  @Override
  public void highlightContinue(final boolean state) {
    synchronized (mHistory) {
      mHistory.append("highlightContinue(").append(state).append(")#");
    }
  }

  @Override
  public Font getTitleFont() {
    return null;
  }

  @Override
  public Font getTextFont() {
    return null;
  }

  @Override
  public Font getMonospaceFont() {
    return null;
  }

  @Override
  public Font getPhaseFont() {
    return null;
  }

  @Override
  public void close() {
    synchronized (mHistory) {
      mHistory.append("close()#");
    }
  }

  private final HashSet<MouseListener> mMouseListeners = new HashSet<>();

  /**
   * Return the set of registered mouse listeners.
   * @return mouse listeners
   */
  public Set<MouseListener> getMouseListeners() {
    return mMouseListeners;
  }

  @Override
  public void addMouseListener(final MouseListener mouseListener) {
    synchronized (mHistory) {
      mMouseListeners.add(mouseListener);
      mHistory.append("addMouseListener(.)#");
    }
  }

  @Override
  public void addMouseMotionListener(final MouseMotionListener mouseMotionListener) {
    synchronized (mHistory) {
      mHistory.append("addMouseMotionListener(.)#");
    }
  }

  @Override
  public void addKeyListener(final KeyListener keyListener) {
    synchronized (mHistory) {
      mHistory.append("addKeyListener(.)#");
    }
  }

  @Override
  public void removeMouseListener(final MouseListener mouseListener) {
    synchronized (mHistory) {
      mMouseListeners.remove(mouseListener);
      mHistory.append("removeMouseListener(.)#");
    }
  }

  @Override
  public void removeMouseMotionListener(final MouseMotionListener mouseMotionListener) {
    synchronized (mHistory) {
      mHistory.append("removeMouseMotionListener(.)#");
    }
  }

  @Override
  public void removeKeyListener(final KeyListener keyListener) {
    synchronized (mHistory) {
      mHistory.append("removeKeyListener(.)#");
    }
  }

  @Override
  public Graphics getGraphics() {
    return mGraphics;
  }

  @Override
  public String toString() {
    synchronized (mHistory) {
      final String mg = mGraphics.toString();
      if (!mg.isEmpty()) {
        mHistory.append('|').append(mg);
      }
      return mHistory.toString();
    }
  }

  @Override
  public int getXPhaseOffset() {
    return 300;
  }

  @Override
  public int getPhaseWidth() {
    return 60;
  }

  @Override
  public Shape clipToArena(final Graphics graphics) {
    synchronized (mHistory) {
      mHistory.append("clipToArena");
    }
    return graphics.getClip();
  }
}
