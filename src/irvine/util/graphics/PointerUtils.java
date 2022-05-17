package irvine.util.graphics;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;


/**
 * Utility functions for controlling the mouse pointer.
 *
 * @author Sean A. Irvine
 */
public final class PointerUtils {

  private PointerUtils() { }

  /** Empty cursor. */
  private static Cursor sBlankCursor = null;

  /**
   * Turn off the pointer for a given component.  When the pointer is moved over
   * this component, it becomes blank.
   *
   * @param component component to turn off pointer for
   * @exception NullPointerException if <code>component</code> is null.
   */
  public static void setBlankCursor(final Component component) {
    if (sBlankCursor == null) {
      final Toolkit tk = Toolkit.getDefaultToolkit();
      final Dimension d = tk.getBestCursorSize(0, 0);
      sBlankCursor = tk.createCustomCursor(new BufferedImage((int) d.getWidth(), (int) d.getHeight(), BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "BLANK");
    }
    component.setCursor(sBlankCursor);
  }

}
