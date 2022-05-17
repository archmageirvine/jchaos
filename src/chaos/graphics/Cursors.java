package chaos.graphics;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Manage the game cursors.
 *
 * @author Sean A. Irvine
 */
public final class Cursors {

  /** Empty cursor. */
  private static Cursor sBlankCursor = null;
  /** Cursor for ranged combat. */
  private static Cursor sShootCursor = null;
  /** Cursor for flying movement. */
  private static Cursor sWingsCursor = null;
  /** Cursor for spell casting. */
  private static Cursor sCastCursor = null;
  /** Cursor for general movement. */
  private static Cursor sCrossCursor = null;
  /** Cursor for dismounting. */
  private static Cursor sDismountCursor = null;

  /** Toolkit. */
  private static final Toolkit TK = Toolkit.getDefaultToolkit();

  private Cursors() {
    // prevent instantiation
  }

  /**
   * Set the blank cursor.
   *
   * @param component component to set cursor on
   */
  public static void setBlankCursor(final Component component) {
    if (sBlankCursor == null) {
      final Dimension d = TK.getBestCursorSize(0, 0);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      sBlankCursor = TK.createCustomCursor(image, new Point(0, 0), "BLANK");
    }
    component.setCursor(sBlankCursor);
  }

  /**
   * Set the shooting cursor.
   *
   * @param component component to set cursor on
   */
  public static void setShootCursor(final Component component) {
    if (sShootCursor == null) {
      final int scSize = 11;
      /*
       * The shoot cursor looks like this:
       *
       *  0123456789A
       * 0.....*.....
       * 1...*****...
       * 2..*..*..*..
       * 3.*...*...*.
       * 4.*...*...*.
       * 5***********
       * 6.*...*...*.
       * 7.*...*...*.
       * 8..*..*..*..
       * 9...*****...
       * A.....*.....
       *
       * which is 11x11, but many systems won't support a cursor of exactly
       * this size, so we need to create our own image at a size the system
       * can handle.
       */
      final Dimension d = TK.getBestCursorSize(scSize, scSize);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      if (w < scSize || h < scSize) {
        throw new RuntimeException("Cannot produce shoot cursor at reasonable size.");
      }
      final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      for (int i = 0; i < scSize; ++i) {
        image.setRGB(5, i, ~0);
        image.setRGB(i, 5, ~0);
        if (i >= 3 && i <= 7) {
        image.setRGB(i, 1, ~0);
        image.setRGB(i, 9, ~0);
        image.setRGB(1, i, ~0);
        image.setRGB(9, i, ~0);
        }
      }
      image.setRGB(2, 2, ~0);
      image.setRGB(8, 2, ~0);
      image.setRGB(2, 8, ~0);
      image.setRGB(8, 8, ~0);
      sShootCursor = TK.createCustomCursor(image, new Point(5, 5), "SHOOT");
    }
    component.setCursor(sShootCursor);
  }

  /**
   * Set the flying movement cursor.
   *
   * @param component component to set cursor on
   */
  public static void setWingsCursor(final Component component) {
    if (sWingsCursor == null) {
      /*
       * The wings cursor looks like this:
       *
       *  0123456789ABCDEF
       * 0***..........***
       * 1.***........***.
       * 2..**........**..
       * 3................
       * 4...+++....+++...
       * 5..+++++..+++++..
       * 6.++...++++...++.
       * 7.......++.......
       * 8.......++.......
       * 9......+..+......
       * A..**........**..
       * B.***........***.
       * C***..........***
       *
       * which is 13x16, but many systems won't support a cursor of exactly
       * this size, so we need to create our own image at a size the system
       * can handle.
       */
      final int scW = 16;
      final int scH = 13;
      final Dimension d = TK.getBestCursorSize(scW, scH);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      if (w < scW || h < scH) {
        throw new RuntimeException("Cannot produce wings cursor at reasonable size.");
      }
      final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      // draw top left
      image.setRGB(0, 0, ~0);
      image.setRGB(1, 0, ~0);
      image.setRGB(2, 0, ~0);
      image.setRGB(1, 1, ~0);
      image.setRGB(2, 1, ~0);
      image.setRGB(3, 1, ~0);
      image.setRGB(2, 2, ~0);
      image.setRGB(3, 2, ~0);
      // copy to lower left
      for (int i = 0; i < 4; ++i) {
        for (int j = 0; j < 3; ++j) {
          image.setRGB(i, scH - j - 1, image.getRGB(i, j));
        }
      }
      // draw left bird, color below sometimes doesn't work
      final int color = 0xFF00FF00;
      image.setRGB(3, 4, color);
      image.setRGB(4, 4, color);
      image.setRGB(5, 4, color);
      image.setRGB(2, 5, color);
      image.setRGB(3, 5, color);
      image.setRGB(4, 5, color);
      image.setRGB(5, 5, color);
      image.setRGB(6, 5, color);
      image.setRGB(1, 6, color);
      image.setRGB(2, 6, color);
      image.setRGB(6, 6, color);
      image.setRGB(7, 6, color);
      image.setRGB(7, 7, color);
      image.setRGB(7, 8, color);
      image.setRGB(6, 9, color);
      // reflect left to right
      for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < scH; ++j) {
          image.setRGB(scW - i - 1, j, image.getRGB(i, j));
        }
      }
      sWingsCursor = TK.createCustomCursor(image, new Point(7, 7), "WINGS");
    }
    component.setCursor(sWingsCursor);
  }

  /**
   * Set the spell casting cursor.
   *
   * @param component component to set cursor on
   */
  public static void setCastCursor(final Component component) {
    if (sCastCursor == null) {
      /*
       * The cast cursor looks like this:
       *
       *  0123456789ABCDEF
       * 0...........*....
       * 1........#..*..#.
       * 2.........#...#..
       * 3...........#....
       * 4.......**.###.**
       * 5...........#....
       * 6.......+*#...#..
       * 7......+*#..*..#.
       * 8.....+*+...*....
       * 9....+*+.........
       * A...+*+..........
       * B..+*+...........
       * C.+*+............
       * D+*+.............
       *
       * which is 14x16, but many systems won't support a cursor of exactly
       * this size, so we need to create our own image at a size the system
       * can handle.
       */
      final int scW = 16;
      final int scH = 14;
      final Dimension d = TK.getBestCursorSize(scW, scH);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      if (w < scW || h < scH) {
        throw new RuntimeException("Cannot produce cast cursor at reasonable size.");
      }
      final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      // draw stem
      final int color = 0xFF00FF00;
      for (int y = 0xD, x = 0; y >= 6; --y, ++x) {
        image.setRGB(x, y, color);
        image.setRGB(x + 1, y, ~0);
        image.setRGB(x + 2, y, color);
      }
      // draw halo
      final int red = 0xFFFF0000;
      image.setRGB(11, 4, red);
      image.setRGB(11, 5, red);
      image.setRGB(11, 3, red);
      image.setRGB(12, 4, red);
      image.setRGB(10, 4, red);
      image.setRGB(9, 2, red);
      image.setRGB(8, 1, red);
      image.setRGB(13, 2, red);
      image.setRGB(14, 1, red);
      image.setRGB(13, 6, red);
      image.setRGB(14, 7, red);
      image.setRGB(9, 6, red);
      image.setRGB(8, 7, red);
      image.setRGB(11, 0, ~0);
      image.setRGB(11, 1, ~0);
      image.setRGB(11, 7, ~0);
      image.setRGB(11, 8, ~0);
      image.setRGB(7, 4, ~0);
      image.setRGB(8, 4, ~0);
      image.setRGB(14, 4, ~0);
      image.setRGB(15, 4, ~0);
      sCastCursor = TK.createCustomCursor(image, new Point(11, 5), "CAST");
    }
    component.setCursor(sCastCursor);
  }

  /**
   * Set the movement and selection cursor.
   *
   * @param component component to set cursor on
   */
  public static void setCrossCursor(final Component component) {
    if (sCrossCursor == null) {
      /*
       * The cross cursor looks like this:
       *
       *  0123456789ABC
       * 0......A......
       * 1......B......
       * 2......C......
       * 3......D......
       * 4......E......
       * 5.............
       * 6ABCDE.C.EDCBA
       * 7.............
       * 8......E......
       * 9......D......
       * A......C......
       * B......B......
       * C......A......
       *
       * which is 13x13.
       */
      final int scW = 13;
      final int scH = 13;
      final Dimension d = TK.getBestCursorSize(scW, scH);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      if (w < scW || h < scH) {
        throw new RuntimeException("Cannot produce cross cursor at reasonable size.");
      }
      final BufferedImage image = crossImage(w, h);
      sCrossCursor = TK.createCustomCursor(image, new Point(6, 6), "CROSS");
    }
    component.setCursor(sCrossCursor);
  }

  static BufferedImage crossImage(final int w, final int h) {
    final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    final int grey = 0xFF808080;
    image.setRGB(6, 6, grey);

    image.setRGB(6, 4, ~0);
    image.setRGB(6, 8, ~0);
    image.setRGB(4, 6, ~0);
    image.setRGB(8, 6, ~0);

    image.setRGB(5, 4, grey);
    image.setRGB(5, 8, grey);
    image.setRGB(4, 5, grey);
    image.setRGB(8, 5, grey);
    image.setRGB(7, 4, grey);
    image.setRGB(7, 8, grey);
    image.setRGB(4, 7, grey);
    image.setRGB(8, 7, grey);

    image.setRGB(6, 3, ~0);
    image.setRGB(6, 9, ~0);
    image.setRGB(3, 6, ~0);
    image.setRGB(9, 6, ~0);

    image.setRGB(5, 3, grey);
    image.setRGB(5, 9, grey);
    image.setRGB(3, 5, grey);
    image.setRGB(9, 5, grey);
    image.setRGB(7, 3, grey);
    image.setRGB(7, 9, grey);
    image.setRGB(3, 7, grey);
    image.setRGB(9, 7, grey);

    image.setRGB(6, 2, ~0);
    image.setRGB(6, 10, ~0);
    image.setRGB(2, 6, ~0);
    image.setRGB(10, 6, ~0);

    image.setRGB(6, 1, grey);
    image.setRGB(6, 11, grey);
    image.setRGB(1, 6, grey);
    image.setRGB(11, 6, grey);

    image.setRGB(6, 0, grey);
    image.setRGB(6, 12, grey);
    image.setRGB(0, 6, grey);
    image.setRGB(12, 6, grey);
    return image;
  }

  static BufferedImage dismountImage(final int scSize, final int w, final int h) {
    final int grey = 0xFF808080;
    final BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < scSize; ++i) {
      image.setRGB(0, i, ~0);
      image.setRGB(10, i, ~0);
      image.setRGB(i, 0, ~0);
      image.setRGB(i, 10, ~0);
    }
    image.setRGB(3, 2, grey);
    image.setRGB(3, 3, grey);
    image.setRGB(3, 4, grey);
    image.setRGB(3, 5, grey);
    image.setRGB(3, 6, grey);
    image.setRGB(3, 7, grey);
    image.setRGB(3, 8, grey);
    image.setRGB(4, 2, grey);
    image.setRGB(5, 2, grey);
    image.setRGB(4, 8, grey);
    image.setRGB(5, 8, grey);
    image.setRGB(6, 7, grey);
    image.setRGB(6, 3, grey);
    image.setRGB(7, 4, grey);
    image.setRGB(7, 5, grey);
    image.setRGB(7, 6, grey);
    return image;
  }

  /**
   * Set the dismounting cursor.
   *
   * @param component component to set cursor on
   */
  public static void setDismountCursor(final Component component) {
    if (sDismountCursor == null) {
      final int scSize = 11;
      /*
       * The dismount cursor looks like this:
       *
       *  0123456789A
       * 0***********
       * 1*.........*
       * 2*..###....*
       * 3*..#..#...*
       * 4*..#...#..*
       * 5*..#...#..*
       * 6*..#...#..*
       * 7*..#..#...*
       * 8*..###....*
       * 9*.........*
       * A***********
       *
       * which is 11x11, but many systems won't support a cursor of exactly
       * this size, so we need to create our own image at a size the system
       * can handle.
       */
      final Dimension d = TK.getBestCursorSize(scSize, scSize);
      final int w = (int) d.getWidth();
      final int h = (int) d.getHeight();
      if (w < scSize || h < scSize) {
        throw new RuntimeException("Cannot produce shoot cursor at reasonable size.");
      }
      final BufferedImage image = dismountImage(scSize, w, h);
      sDismountCursor = TK.createCustomCursor(image, new Point(5, 5), "DISMOUNT");
    }
    component.setCursor(sDismountCursor);
  }

}
