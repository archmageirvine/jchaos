package chaos.engine;

import java.awt.Container;
import java.awt.event.KeyEvent;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CursorHelperTest extends TestCase {

  private static final Container DUMMY = new Container();

  private KeyEvent key(final int c) {
    return new KeyEvent(DUMMY, KeyEvent.KEY_PRESSED, 0, 0, c, '\0');
  }

  public void testCursor() {
    final CursorHelper c = new CursorHelper(3, 5);
    assertEquals(CursorHelper.NONE, c.getPosition());
    c.setPosition(5);
    assertEquals(5, c.getPosition());
    c.setPosition(CursorHelper.NONE);
    assertEquals(CursorHelper.NONE, c.getPosition());
    c.setPosition(3);
    c.update(key(KeyEvent.VK_D));
    c.setPosition(4);
    c.update(key(KeyEvent.VK_RIGHT));
    c.update(key(KeyEvent.VK_KP_RIGHT));
    c.update(key(KeyEvent.VK_NUMPAD6));
    c.update(key(KeyEvent.VK_E));
    c.update(key(KeyEvent.VK_NUMPAD9));
    c.update(key(KeyEvent.VK_PAGE_UP));
    c.update(key(KeyEvent.VK_W));
    c.update(key(KeyEvent.VK_UP));
    c.update(key(KeyEvent.VK_KP_UP));
    c.update(key(KeyEvent.VK_NUMPAD8));
    c.update(key(KeyEvent.VK_Q));
    c.update(key(KeyEvent.VK_NUMPAD7));
    c.update(key(KeyEvent.VK_HOME));
    c.update(key(KeyEvent.VK_A));
    c.update(key(KeyEvent.VK_LEFT));
    c.update(key(KeyEvent.VK_KP_LEFT));
    c.update(key(KeyEvent.VK_NUMPAD4));
    c.update(key(KeyEvent.VK_Z));
    c.update(key(KeyEvent.VK_NUMPAD1));
    c.update(key(KeyEvent.VK_END));
    c.update(key(KeyEvent.VK_X));
    c.update(key(KeyEvent.VK_DOWN));
    c.update(key(KeyEvent.VK_KP_DOWN));
    c.update(key(KeyEvent.VK_NUMPAD2));
    c.update(key(KeyEvent.VK_C));
    c.update(key(KeyEvent.VK_NUMPAD3));
    c.update(key(KeyEvent.VK_PAGE_DOWN));
    assertEquals(3, c.getPosition());
    c.update(key(KeyEvent.VK_PERIOD));
    assertEquals(CursorHelper.NONE, c.getPosition());
  }
}
