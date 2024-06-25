package chaos;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import chaos.graphics.MockScreen;
import chaos.util.Sleep;
import irvine.TestUtils;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class GenericSetUpTest extends TestCase {

  private static final Container DUMMY = new Container();

  public void test() throws InterruptedException {
    final MockScreen screen = new MockScreen();
    final Configuration config = new Configuration(640, 320, true, 10, 10);
    final GenericSetUp setUp = new GenericSetUp(config, screen, new Chaos(config, false), false);
    final Thread t = new Thread(() -> {
      Sleep.sleep(100);
      final KeyEvent e = new KeyEvent(DUMMY, KeyEvent.KEY_TYPED, 0, 0, 0, '\n');
      setUp.keyTyped(e);
      Assert.assertTrue(e.isConsumed());
    });
    t.start();
    assertFalse(setUp.setUp()[0]);
    t.join();
    final Graphics g = screen.getGraphics();
    final String s = g.toString();
    TestUtils.containsAll(s, "getFontMetrics",
      "Java Chaos",
      "Sean A. Irvine",
      "Inspired by Julian Gollop's Chaos",
      "fillOval",
      "setColor",
      "I(",
      "gmail.com"
    );
  }
}
