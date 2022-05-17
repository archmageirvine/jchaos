package irvine.util.graphics;

import static irvine.math.r.Constants.DEGREES_TO_RADIANS;
import static irvine.math.r.Constants.TAU;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;

import java.awt.Graphics;
import java.io.PrintStream;

import irvine.math.IntegerUtils;
import irvine.util.string.PostScript;

/**
 * Draw regular stars.
 * @author Sean A. Irvine
 */
public final class Stars {

  private Stars() { }

  /**
   * Draw a star centered on <code>(x,y)</code> with radius <code>r</code>.
   * The star will have the specified number of points and initial point
   * at the specified angle (measured clockwise in degrees) from vertical.
   * The <code>skip</code> parameter controls the type of star drawn, it
   * should be between 1 and <code>skip-1</code>.
   *
   * @param g place to draw
   * @param x x-coordinate
   * @param y y-coordinate
   * @param r radius
   * @param points number of points on the star
   * @param skip skip value
   * @param theta angle from vertical
   * @exception IllegalArgumentException if <code>points</code> is less than
   * 2, <code>r</code> is nonpositive, or <code>skip</code> is not between 1
   * and <code>points-1</code>.
   */
  public static void drawStar(final Graphics g, final int x, final int y, final int r, final int points, final int skip, double theta) {
    if (r < 1 || points < 2 || skip < 1 || skip > points - 1) {
      throw new IllegalArgumentException();
    }
    if (g != null) {
      final double anglePerPoint = TAU / points;
      theta *= DEGREES_TO_RADIANS; // convert to radians
      final int d = IntegerUtils.gcd(points, skip);
      for (int p = 0; p < d; ++p) {
        // Draw all the lines of one connected part of the star
        int cp = p;
        final double angle = theta + p * anglePerPoint;
        int xp = (int) round(x + r * sin(angle));
        int yp = (int) round(y + r * cos(angle));
        do {
          cp += skip;
          cp %= points;
          final double a = theta + cp * anglePerPoint;
          final int xc = (int) round(x + r * sin(a));
          final int yc = (int) round(y + r * cos(a));
          g.drawLine(xp, yp, xc, yc);
          xp = xc;
          yp = yc;
        } while (cp != p);
      }
    }
  }

  /**
   * Draw a star centered on <code>(x,y)</code> with radius <code>r</code>.
   * The star will have the specified number of points with one point at
   * the top.
   * The <code>skip</code> parameter controls the type of star drawn, it
   * should be between 1 and <code>skip-1</code>.
   *
   * @param g place to draw
   * @param x x-coordinate
   * @param y y-coordinate
   * @param r radius
   * @param points number of points on the star
   * @param skip skip value
   * @exception IllegalArgumentException if <code>points</code> is less than
   * 2, <code>r</code> is nonpositive, or <code>skip</code> is not between 1
   * and <code>points-1</code>.
   */
  public static void drawStar(final Graphics g, final int x, final int y, final int r, final int points, final int skip) {
    drawStar(g, x, y, r, points, skip, 0);
  }

  /**
   * Draw a star centered on <code>(x,y)</code> with radius <code>r</code>.
   * The star will have the specified number of points and initial point
   * at the specified angle (measured clockwise in degrees) from vertical.
   * The <code>skip</code> parameter controls the type of star drawn, it
   * should be between 1 and <code>skip-1</code>.  This method produces
   * PostScript.
   *
   * @param ps place to draw
   * @param x x-coordinate
   * @param y y-coordinate
   * @param r radius
   * @param points number of points on the star
   * @param skip skip value
   * @param theta angle from vertical
   * @exception IllegalArgumentException if <code>points</code> is less than
   * 2, <code>r</code> is nonpositive, or <code>skip</code> is not between 1
   * and <code>points-1</code>.
   */
  public static void drawStar(final PrintStream ps, final int x, final int y, final int r, final int points, final int skip, double theta) {
    if (r < 1 || points < 2 || skip < 1 || skip > points - 1) {
      throw new IllegalArgumentException();
    }
    if (ps != null) {
      final double anglePerPoint = TAU / points;
      theta *= DEGREES_TO_RADIANS; // convert to radians
      final int d = IntegerUtils.gcd(points, skip);
      for (int p = 0; p < d; ++p) {
        // Draw all the lines of one connected part of the star
        int cp = p;
        final double angle = theta + p * anglePerPoint;
        final double xp = x + r * sin(angle);
        final double yp = y + r * cos(angle);
        final StringBuilder path = new StringBuilder("newpath ")
          .append(xp).append(' ').append(yp).append(" moveto ");
        do {
          cp += skip;
          cp %= points;
          final double a = theta + cp * anglePerPoint;
          final double xc = x + r * sin(a);
          final double yc = y + r * cos(a);
          path.append(xc).append(' ').append(yc).append(" lineto ");
        } while (cp != p);
        path.append("stroke");
        ps.println(path.toString());
      }
    }
  }

  /**
   * Draw a star centered on <code>(x,y)</code> with radius <code>r</code>.
   * The star will have the specified number of points with one points at
   * the top.
   * The <code>skip</code> parameter controls the type of star drawn, it
   * should be between 1 and <code>skip-1</code>.  This method produces
   * PostScript.
   *
   * @param ps place to draw
   * @param x x-coordinate
   * @param y y-coordinate
   * @param r radius
   * @param points number of points on the star
   * @param skip skip value
   * @exception IllegalArgumentException if <code>points</code> is less than
   * 2, <code>r</code> is nonpositive, or <code>skip</code> is not between 1
   * and <code>points-1</code>.
   */
  public static void drawStar(final PrintStream ps, final int x, final int y, final int r, final int points, final int skip) {
    drawStar(ps, x, y, r, points, skip, 0);
  }

  /**
   * Used in producing the documentation.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    if (args.length < 3) {
      System.err.println("Usage: Stars width points skip [circle]");
      return;
    }
    final int width = Integer.parseInt(args[0]);
    final int points = Integer.parseInt(args[1]);
    final int skip = Integer.parseInt(args[2]);
    PostScript.header(System.out, Stars.class, "image", width, width);
    System.out.println(".1 setlinewidth");
    final int ww = width / 2;
    final int r = (width - 2) / 2;
    drawStar(System.out, ww, ww, r, points, skip);
    if (args.length == 4 && "circle".equals(args[3])) {
      System.out.println(".5 setlinewidth");
      System.out.println("0 0 1 setrgbcolor");
      System.out.println("newpath " + ww + " " + ww + " " + r + " 0 360 arc stroke");
    }
    PostScript.trailer(System.out);
  }
}
