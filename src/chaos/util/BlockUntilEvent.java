package chaos.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chaos.graphics.ChaosScreen;

/**
 * Simple block until a key is pressed or the mouse if clicked.
 * @author Sean A. Irvine
 */
public class BlockUntilEvent implements MouseListener, KeyListener {

  private final BooleanLock mLock = new BooleanLock();

  private BlockUntilEvent(final ChaosScreen screen, final int timeout) {
    mLock.setValue(false);
    screen.addMouseListener(this);
    screen.addKeyListener(this);
    try {
      mLock.waitUntilTrue(timeout);
    } catch (final InterruptedException e) {
      // too bad
    }
    screen.removeKeyListener(this);
    screen.removeMouseListener(this);
  }

  public static void blockUntilEvent(final ChaosScreen screen, final int timeout) {
    new BlockUntilEvent(screen, timeout);
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
  public void keyPressed(final KeyEvent e) {
  }

  @Override
  public void keyTyped(final KeyEvent e) {
  }

  @Override
  public void keyReleased(final KeyEvent e) {
    mLock.setValue(true);
    e.consume();
  }


  @Override
  public void mouseClicked(final MouseEvent e) {
    mLock.setValue(true);
    e.consume();
  }
}
