package chaos.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import chaos.util.BooleanLock;

/**
 * Handle requests to exit the game.
 * @author Sean A. Irvine
 */
final class HandleExit extends KeyAdapter {

  private static final String QUIT_MESSAGE = "REALLY QUIT? (Y/N)";

  private final BooleanLock mExitLock;
  private final ChaosScreen mScreen;
  private final int mMainWidth;
  private final int mMainHeight;

  HandleExit(final ChaosScreen screen, final int width, final int height) {
    super();
    mScreen = screen;
    mExitLock = new BooleanLock(false);
    mMainWidth = width;
    mMainHeight = height;
  }

  @Override
  public void keyPressed(final KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      e.consume();
      quitDialog(mScreen.getGraphics());
    }
  }

  /**
   * Check for Y/N.
   * @param e key event
   */
  @Override
  public void keyTyped(final KeyEvent e) {
    if (mExitLock.isTrue()) {
      final int ch = Character.toLowerCase(e.getKeyChar());
      if (ch == 'y' || ch == '\n') {
        e.consume();
        try {
          mScreen.close();
        } finally {
          System.exit(0);
        }
      } else if (ch == 'n') {
        mExitLock.setValue(false);
        e.consume();
      }
    }
  }

  void quitDialog(final Graphics graphics) {
    new Thread(() -> {
      synchronized (mScreen.lock()) {
        synchronized (graphics) {
          graphics.setFont(mScreen.getPhaseFont());
          final FontMetrics fm = graphics.getFontMetrics();
          final int mw = fm == null ? 0 : fm.stringWidth(QUIT_MESSAGE);
          final int hy = fm == null ? 0 : fm.getHeight();
          final int rx = mScreen.getXOffset() + (mMainWidth - mw) / 2;
          final int ry = mScreen.getYOffset() + mMainHeight / 2;
          graphics.setColor(Color.BLUE);
          graphics.fillRect(rx - 7, ry - hy - 5, mw + 14, hy + 14);
          graphics.setColor(Color.YELLOW);
          graphics.drawString(QUIT_MESSAGE, rx, ry);
          graphics.drawRect(rx - 5, ry - hy - 3, mw + 10, hy + 10);
          graphics.drawRect(rx - 7, ry - hy - 5, mw + 14, hy + 14);
        }
        mExitLock.setValue(true);
        // If there is no response after 10 seconds, assume the user didn't really want to quit
        try {
          mExitLock.waitUntilFalse(10000);
        } catch (final InterruptedException ex) {
        }
        mExitLock.setValue(false);
      }
    }).start();
  }

  BooleanLock getLock() {
    return mExitLock;
  }
}
