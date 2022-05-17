package chaos.engine;

import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * Class for maintaining a cursor position and allowing it to move under keyboard
 * control on a grid
 * @author Sean A. Irvine
 */
public final class CursorHelper implements Serializable {

  /** Constant indicating that no position is currently selected. */
  public static final int NONE = -1;

  private final int mWidth;
  private final int mSize;
  private int mPosition = NONE;

  /**
   * Construct a new cursor on a grid.
   * @param width width of grid
   * @param height height of grid
   */
  public CursorHelper(final int width, final int height) {
    mWidth = width;
    mSize = width * height;
  }

  /**
   * Reset cursor to no cell selected.
   */
  public void reset() {
    mPosition = NONE;
  }

  /**
   * Get the current position of the cursor.
   * @return current position
   */
  public int getPosition() {
    return mPosition;
  }

  /**
   * Explicitly set the position of the cursor.
   * @param position new position (can be NONE)
   */
  public void setPosition(final int position) {
    mPosition = position % mSize;
  }

  boolean update(final int keyCode) {
    switch (keyCode) {
      case KeyEvent.VK_D:
      case KeyEvent.VK_RIGHT:
      case KeyEvent.VK_KP_RIGHT:
      case KeyEvent.VK_NUMPAD6:
        ++mPosition;
        break;
      case KeyEvent.VK_E:
      case KeyEvent.VK_NUMPAD9:
      case KeyEvent.VK_PAGE_UP:
        mPosition += 1 - mWidth;
        break;
      case KeyEvent.VK_W:
      case KeyEvent.VK_UP:
      case KeyEvent.VK_KP_UP:
      case KeyEvent.VK_NUMPAD8:
        mPosition -= mWidth;
        break;
      case KeyEvent.VK_Q:
      case KeyEvent.VK_NUMPAD7:
      case KeyEvent.VK_HOME:
        mPosition -= mWidth + 1;
        break;
      case KeyEvent.VK_A:
      case KeyEvent.VK_LEFT:
      case KeyEvent.VK_KP_LEFT:
      case KeyEvent.VK_NUMPAD4:
        mPosition -= 1;
        break;
      case KeyEvent.VK_Z:
      case KeyEvent.VK_NUMPAD1:
      case KeyEvent.VK_END:
        mPosition += mWidth - 1;
        break;
      case KeyEvent.VK_X:
      case KeyEvent.VK_DOWN:
      case KeyEvent.VK_KP_DOWN:
      case KeyEvent.VK_NUMPAD2:
        mPosition += mWidth;
        break;
      case KeyEvent.VK_C:
      case KeyEvent.VK_NUMPAD3:
      case KeyEvent.VK_PAGE_DOWN:
        mPosition += mWidth + 1;
        break;
      case KeyEvent.VK_PERIOD:
        reset();
        return true;
      default:
        return false;
    }
    if (mPosition < 0) {
      mPosition += mSize;
    } else if (mPosition >= mSize) {
      mPosition -= mSize;
    }
    return true;
  }

  /**
   * Update cursor with keyboard event.
   * @param event the event
   * @return true if the cursor understood the event
   */
  public boolean update(final KeyEvent event) {
    return update(event.getKeyCode());
  }
}
