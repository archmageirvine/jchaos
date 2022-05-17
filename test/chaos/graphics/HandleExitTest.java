package chaos.graphics;

import java.awt.Container;
import java.awt.event.KeyEvent;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class HandleExitTest extends TestCase {

  private static final Container DUMMY = new Container();

  private KeyEvent keyPress(final int c, final char ch) {
    return new KeyEvent(DUMMY, KeyEvent.KEY_PRESSED, 0, 0, c, ch);
  }

  public void test() {
    final MockScreen screen = new MockScreen();
    final HandleExit exit = new HandleExit(screen, 1000, 500);
    final KeyEvent no = keyPress(KeyEvent.VK_N, 'n');
    exit.keyTyped(no);
    assertFalse(no.isConsumed());
    final KeyEvent escape = keyPress(KeyEvent.VK_ESCAPE, '\0');
    exit.keyPressed(escape);
    assertTrue(escape.isConsumed());
    try {
      exit.getLock().waitUntilTrue(10000);
    } catch (final InterruptedException e) {
      fail();
    }
    final KeyEvent kKey = keyPress(KeyEvent.VK_K, 'k');
    exit.keyTyped(kKey);
    assertFalse(kKey.isConsumed());
    exit.keyTyped(no);
    assertTrue(no.isConsumed());
    try {
      exit.getLock().waitUntilFalse(10000);
    } catch (final InterruptedException e) {
      fail();
    }
    assertEquals("|setFont()#getFontMetrics()#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(493,245,14,14)#"
      + "setColor(java.awt.Color[r=255,g=255,b=0])#drawString(REALLY QUIT? (Y/N),500,250)#drawRect(495,247,10,10)#drawRect(493,245,14,14)#", screen.toString());
  }

}
