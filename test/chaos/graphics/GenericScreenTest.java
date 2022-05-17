package chaos.graphics;

import java.awt.GraphicsEnvironment;

import chaos.Configuration;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class GenericScreenTest extends TestCase {

  public void testIcons() {
    assertNotNull(GenericScreen.BLANK);
    assertNotNull(GenericScreen.EXIT);
    assertNotNull(GenericScreen.CONT);
  }

  public void test() {
    if (!GraphicsEnvironment.isHeadless()) {
      final Configuration conf = new Configuration(10, 10, false, 1, 1) {
        @Override
        public int getPixelWidth() {
          return 480;
        }

        @Override
        public int getPixelHeight() {
          return 320;
        }
      };
      try (final GenericScreen screen = new GenericScreen(conf, false, 0)) {
        assertEquals(16, screen.getXOffset());
        assertEquals(16, screen.getYOffset());
        assertEquals(256, screen.getMainHeight());
        assertEquals(384, screen.getMainWidth());
        assertEquals(416, screen.getXRight());
        assertEquals(48, screen.getYRight());
        assertEquals(64, screen.getPhaseWidth());
        assertEquals(16, screen.getPowerUpXOffset());
        assertEquals(304, screen.getPowerUpYOffset());
        assertEquals(48, screen.getRightWidth());
        assertEquals(256, screen.getRightHeight());
        assertEquals(64, screen.getPhaseWidth());
        assertNotNull(screen.getPhaseFont());
        assertNotNull(screen.getMonospaceFont());
        assertNotNull(screen.getTextFont());
        assertNotNull(screen.getTitleFont());
        assertNotNull(screen.lock());
        assertFalse(screen.isPositionInContinueGadget(425, 273));
        assertFalse(screen.isPositionInContinueGadget(424, 274));
        assertTrue(screen.isPositionInContinueGadget(425, 274));
        assertTrue(screen.isPositionInContinueGadget(433, 283));
      }
    }
  }
}
