package irvine.util.graphics;

import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import chaos.util.Sleep;
import irvine.util.RuntimeUtils;
import irvine.util.time.Delay;


/**
 * A generic framework for implementing a full-screen game with a given frame
 * rate.  It turns off most of the Swing garbage like menus and resizing.  It
 * effectively performs double-buffering because imagery is drawn into a
 * volatile image that is only copied to the screen when the <code>paintScreen()
 * </code> method is called.  In my experience this is actually better than
 * using a full page-flipping strategy.
 *
 * @author Sean A. Irvine
 */
public class FullScreen extends JFrame implements AutoCloseable {

  /** We use the sync method on the toolkit during active painting. */
  private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

  static DisplayMode getBestDisplayMode(final GraphicsDevice gd, final int width, final int height, final int depth) {
    // We examine all the available display modes, and try to pick the one
    // that best matches the requested mode.  In general, it will not pick a
    // mode of smaller size than requested, unless there is no other option
    DisplayMode bestMatch = gd.getDisplayMode();
    final long area = width * (long) height;
    for (final DisplayMode mode : gd.getDisplayModes()) {
      final int d = mode.getBitDepth();
      final int w = mode.getWidth();
      final int h = mode.getHeight();
      final long a = w * (long) h;
      if (w >= width && h >= height && (d == -1 || d >= depth) && a - area < Math.abs(bestMatch.getWidth() * bestMatch.getHeight() - area)) {
        // This mode is big enough to meet the request, but has less pixels
        // than the current best, thus it is a better option.
        bestMatch = mode;
      }
    }
    return bestMatch;
  }

  static void setDecoration(final JFrame frame) {
    // We are going to be doing our own painting and decoration
    frame.setIgnoreRepaint(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setUndecorated(true);
    frame.setResizable(false);
    frame.setBackground(Color.BLACK);
  }

  /**
   * Construct a new exclusive frame.  An attempt is made to get
   * a frame of specified size, but if this is not possible, then a frame in
   * the current default size is returned.
   *
   * @param name name for the frame, can be null
   * @param width desired width of screen in pixels
   * @param height desired height of screen in pixels
   * @param depth desired color depth
   * @param fullScreen true for full-screen
   * @param screenNumber screen number to use (-1 for default)
   * @exception UnsupportedOperationException if the underlying operating system
   * or JVM does not support full-screen exclusive mode.
   */
  public FullScreen(final String name, final int width, final int height, final int depth, final boolean fullScreen, final int screenNumber) {
    super(name);
    setDecoration(this);


    // In modern set up having multiple displays is not uncommon
//    final GraphicsDevice[] screenDevices = getLocalGraphicsEnvironment().getScreenDevices();
//    for (final GraphicsDevice screen : screenDevices) {
//      System.err.println(screen.toString());
//    }

    // attempt to upgrade to full-screen exclusive mode
    final GraphicsDevice gd = screenNumber == -1 ? getLocalGraphicsEnvironment().getDefaultScreenDevice()
      : getLocalGraphicsEnvironment().getScreenDevices()[screenNumber];
    if (fullScreen) {
      if (gd.isFullScreenSupported()) {
        // Some sources report that calling setFullScreenWindow is asynchronous or
        // requires a small delay.  Thus, we add a brief sleep after this call.
        gd.setFullScreenWindow(this);
      } else {
        System.err.println("Full-screen exclusive mode not supported, using native");
      }
      Sleep.shortSleep();
    } else {
      setSize(new Dimension(width, height));
    }
    // Attempt to select the requested display modes, if no change is possible
    // then just leave the window as is and the caller can decide if it is usable
    if (fullScreen && gd.isDisplayChangeSupported()) {
      final DisplayMode bestMatch = getBestDisplayMode(gd, width, height, depth);
      // Check that the current mode is not already the one we want.
      if (bestMatch != gd.getDisplayMode()) {
        // i.e. the select mode is different from the current mode
        // accept whatever refresh rate we are given
        final DisplayMode dm = new DisplayMode(bestMatch.getWidth(), bestMatch.getHeight(), bestMatch.getBitDepth(), DisplayMode.REFRESH_RATE_UNKNOWN);
        try {
          gd.setDisplayMode(dm);
          // Some sources report setDisplayMode to be asynchronous, so we wait
          // a while trying to check that the call has succeeded.  See Sun
          // Bug Parade 4500063.
          // Commented out on 2018-10-04, probably fixed in any reasonable JVM by now
          //waitForDisplay(width, height, gd);
        } catch (final InternalError | IllegalArgumentException e) {
          // Although this is not declared, I've seen it happen in FedoraCore3
          // Just try to continue afterwards
          System.err.println("INTERNAL ERROR: " + e.getMessage());
        }
      }
    } else {
      setSize(width, height);
    }

    // No matter what size we eventually ended up with, we attempt to make it
    // the maximum size the preferred size.  This should help prevent the user
    // resize the frame.
    setPreferredSize(getMaximumSize());

    // Attempt to make this frame the one having current focus.  This is a good
    // idea because this frame will likely cover the entire screen and obscure
    // anything else currently having focus.
    requestFocus();

    // Try to make sure the frame is visible.
    if (!RuntimeUtils.isTest()) {
      setVisible(true);
    }
  }

//  static void waitForDisplay(final int width, final int height, final GraphicsDevice gd) {
//    int n = 0;
//    while (n++ < 5) {
//      Sleep.sleep(500);
//      final DisplayMode cdm = gd.getDisplayMode();
//      if (cdm.getHeight() == height && cdm.getWidth() == width) {
//        break;
//      }
//    }
//  }

  /**
   * Construct a new full-screen exclusive frame.  An attempt is made to get
   * a frame of specified size, but if this is not possible, then a frame in
   * the current default size is returned.
   *
   * @param name name for the frame, can be null
   * @param width desired width of screen in pixels
   * @param height desired height of screen in pixels
   * @param depth desired color depth
   * @exception UnsupportedOperationException if the underlying operating system
   * or JVM does not support full-screen exclusive mode.
   */
  public FullScreen(final String name, final int width, final int height, final int depth) {
    this(name, width, height, depth, true, -1);
  }

  /** The image that will be drawn on the next frame display. */
  private transient Image mImage = null;

  /**
   * Get the Graphics objects.
   *
   * @return the Graphics object
   */
  public Graphics getImageGraphics() {
    if (mImage == null) {
      mImage = createVolatileImage(getWidth(), getHeight());
    }
    return mImage.getGraphics();
  }

  /**
   * Render the current contents of the image buffer to the screen.
   */
  public void paintScreen() {
    if (mImage != null) {
      // If the frame has been resized, has it z-position on screen changed,
      // or has disappeared because it is in the processing closing, then the
      // graphics context may have changed.  Therefore, for each paint the
      // graphics context is retrieved afresh.  Because we have tried hard
      // to fix the size of the frame, this is most likely to be necessary
      // when the frame has been closed.
      final Graphics gr = getGraphics();
      if (gr != null) {
        gr.drawImage(mImage, 0, 0, null);
        // Sync the display (on Linux this fixes event queue problems)
        TOOLKIT.sync();
        gr.dispose();
      }
    }
  }

  /**
   * Animate this screen at the requested frame rate.  This method blocks
   * until the animation is interrupted, thus it is best to call this
   * method from a separate thread.  If the requested frame-rate is too
   * high, the system will still do the best it can.
   *
   * @param fps desired frame rate
   * @exception IllegalArgumentException if <code>fps</code> is less than 1.
   */
  public void animate(final int fps) {
    if (fps < 1) {
      throw new IllegalArgumentException();
    }
    // Interval between frames in nanoseconds.
    final Delay delay = new Delay(1000000000L / fps);
    while (!Thread.interrupted()) {
      final long beforeTime = System.nanoTime();
      paintScreen();
      if (delay.delay(beforeTime)) {
        // If interrupted, it means someone has requested the animation stop
        break;
      }
    }
  }

  @Override
  public void close() {
    try {
      dispose();
      getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
    } catch (final InternalError e) {
      // Have observed this failure on at least one Linux box, due to an
      // underlying internal error when trying to set the display mode.
    }
  }

  @Override
  protected void finalize() {
    close();
  }
}
