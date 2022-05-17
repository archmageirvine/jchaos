package irvine.util.graphics;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class FullScreenTest extends TestCase {

  public void testGetDisplayMode() {
    final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    if (!GraphicsEnvironment.isHeadless()) {
      final DisplayMode dm = FullScreen.getBestDisplayMode(ge.getDefaultScreenDevice(), 120, 100, 8);
      assertNotNull(dm);
      assertTrue(dm.getWidth() >= 120);
      assertTrue(dm.getHeight() >= 100);
      final int depth = dm.getBitDepth();
      assertTrue(depth == -1 || depth >= 8);
    }
  }

  private GraphicsDevice getMockDisplayMode(final DisplayMode... modes) {
    return new GraphicsDevice() {
      @Override
      public int getType() {
        return 0;
      }

      @Override
      public String getIDstring() {
        return null;
      }

      @Override
      public GraphicsConfiguration[] getConfigurations() {
        return new GraphicsConfiguration[0];
      }

      @Override
      public GraphicsConfiguration getDefaultConfiguration() {
        return null;
      }

      @Override
      public DisplayMode[] getDisplayModes() {
        return modes;
      }

      @Override
      public DisplayMode getDisplayMode() {
        return modes.length == 0 ? null : modes[0];
      }
    };
  }

  public void testGetDisplayModeNone() {
    final GraphicsDevice gd = getMockDisplayMode();
    assertNull(FullScreen.getBestDisplayMode(gd, 120, 100, 8));
  }

  public void testGetDisplayMode2() {
    final DisplayMode dm0 = new DisplayMode(130, 100, 8, 60);
    final DisplayMode dm1 = new DisplayMode(120, 90, 8, 60);
    final DisplayMode dm2 = new DisplayMode(120, 100, 2, 60);
    final DisplayMode dm5 = new DisplayMode(130, 130, 12, 60);
    final DisplayMode dm3 = new DisplayMode(120, 100, 8, 60);
    final DisplayMode dm4 = new DisplayMode(90, 120, 12, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1, dm2, dm5, dm3, dm4);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm3, dm);
  }

  public void testGetDisplayMode3() {
    final DisplayMode dm0 = new DisplayMode(10, 10, 1, 10);
    final DisplayMode dm1 = new DisplayMode(120, 90, 8, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm0, dm);
  }

  public void testGetDisplayMode4() {
    final DisplayMode dm0 = new DisplayMode(10, 10, 1, 10);
    final DisplayMode dm1 = new DisplayMode(110, 100, 8, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm0, dm);
  }

  public void testGetDisplayMode5() {
    final DisplayMode dm0 = new DisplayMode(10, 10, 1, 10);
    final DisplayMode dm1 = new DisplayMode(120, 100, 5, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm0, dm);
  }

  public void testGetDisplayMode6() {
    final DisplayMode dm0 = new DisplayMode(10, 10, 1, 10);
    final DisplayMode dm1 = new DisplayMode(120, 100, 0, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm0, dm);
  }

  public void testGetDisplayMode7() {
    final DisplayMode dm0 = new DisplayMode(10, 10, 1, 10);
    final DisplayMode dm1 = new DisplayMode(120, 100, -1, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 120, 100, 8);
    assertEquals(dm1, dm);
  }

  public void testGetDisplayMode8() {
    final DisplayMode dm0 = new DisplayMode(30, 10, 8, 60);
    final DisplayMode dm1 = new DisplayMode(10, 10, 8, 60);
    final DisplayMode dm2 = new DisplayMode(2, 100, 8, 60);
    final GraphicsDevice gd = getMockDisplayMode(dm0, dm1, dm2);
    final DisplayMode dm = FullScreen.getBestDisplayMode(gd, 1, 100, 8);
    assertEquals(dm2, dm);
  }

  public void testDecoration() {
    if (!GraphicsEnvironment.isHeadless()) {
      final JFrame frame = new JFrame();
      FullScreen.setDecoration(frame);
      assertFalse(frame.isResizable());
      assertTrue(frame.isUndecorated());
      assertTrue(frame.getIgnoreRepaint());
      assertEquals(WindowConstants.EXIT_ON_CLOSE, frame.getDefaultCloseOperation());
      assertEquals(Color.BLACK, frame.getBackground());
    }
  }
}
