package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import chaos.util.Sleep;

/**
 * Convenience for rendering images.
 *
 * @author Sean A. Irvine
 */
public final class Render {

  private Render() { }

  /**
   * Try to render an image for up to two seconds.  If the image or graphics is
   * null then nothing is done.
   * @param g graphics
   * @param image image to render
   * @param x <i>x</i>-coordinate
   * @param y <i>y</i>-coordinate
   */
  public static void renderImage(final Graphics g, final Image image, final int x, final int y) {
    if (image != null && g != null) {
      int k = 0;
      while (!g.drawImage(image, x, y, null) && k++ < 20) {
        Sleep.shortSleep();
      }
    }
  }

  /**
   * Get the width of the specified image, waiting up to two seconds for the
   * image to load if the width is not immediately available.
   * @param image image to get with of
   * @return width of image
   */
  public static int getWidthOfImage(final Image image) {
    if (image == null) {
      return -1;
    }
    int k = 0;
    while (image.getWidth(null) == -1 && k++ < 20) {
      Sleep.shortSleep();
    }
    return image.getWidth(null);
  }

  /**
   * Render horizontal radio buttons at origin.
   *
   * @param g where to render
   * @param r button radius
   * @param sep separation between buttons
   * @param numButtons number of buttons
   * @param selected currently selected button
   */
  public static void radio(final Graphics g, final int r, final int sep, final int numButtons, final int selected) {
    final int h = r - 1;
    // Draw empty radio buttons
    g.setColor(Color.BLACK);
    for (int k = 0; k < numButtons; ++k) {
      g.fillOval(k * sep, h - r / 2, r, r);
    }
    g.setColor(Color.WHITE);
    for (int k = 0; k < numButtons; ++k) {
      g.drawOval(k * sep, h - r / 2, r, r);
    }
    // Fill selected button
    g.setColor(Color.RED);
    g.fillOval(selected * sep + 1, r - h / 2, h - 1, h - 1);
  }

}
