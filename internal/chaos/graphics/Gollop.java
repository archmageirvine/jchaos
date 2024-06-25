package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;

/**
 * Draw the graphics for the Gollop object.
 * @author Sean A. Irvine
 */
public final class Gollop {

  private Gollop() {
  }

  private static final class RoundGradientContext implements PaintContext {
    final Point2D mPoint;
    final Point2D mRadius;
    final Color mC1, mC2;

    private RoundGradientContext(final Point2D p, final Color c1, final Point2D r, final Color c2) {
      mPoint = p;
      mC1 = c1;
      mRadius = r;
      mC2 = c2;
    }

    @Override
    public void dispose() {
    }

    @Override
    public ColorModel getColorModel() {
      return ColorModel.getRGBdefault();
    }

    @Override
    public Raster getRaster(final int x, final int y, final int w, final int h) {
      final WritableRaster raster = getColorModel().createCompatibleWritableRaster(w, h);
      final int[] data = new int[w * h * 4];
      for (int j = 0; j < h; ++j) {
        for (int i = 0; i < w; ++i) {
          final double distance = mPoint.distance(x + i, y + j);
          final double radius = mRadius.distance(0, 0);
          final double ratio = Math.min(1.0, distance / radius);
          final int base = (j * w + i) * 4;
          data[base] = (int) (mC1.getRed() + ratio * (mC2.getRed() - mC1.getRed()));
          data[base + 1] = (int) (mC1.getGreen() + ratio * (mC2.getGreen() - mC1.getGreen()));
          data[base + 2] = (int) (mC1.getBlue() + ratio * (mC2.getBlue() - mC1.getBlue()));
          data[base + 3] = (int) (mC1.getAlpha() + ratio * (mC2.getAlpha() - mC1.getAlpha()));
        }
      }
      raster.setPixels(0, 0, w, h, data);
      return raster;
    }
  }

  private static final class RoundGradientPaint implements Paint {
    final Point2D mPoint;
    final Point2D mRadius;
    final Color mPointColor, mBackgroundColor;

    private RoundGradientPaint(final double x, final double y, final Color pointColor, final Point2D radius, final Color backgroundColor) {
      assert radius.distance(0, 0) <= 0;
      mPoint = new Point2D.Double(x, y);
      mPointColor = pointColor;
      mRadius = radius;
      mBackgroundColor = backgroundColor;
    }

    @Override
    public PaintContext createContext(final ColorModel cm, final Rectangle deviceBounds, final Rectangle2D userBounds, final AffineTransform xform, final RenderingHints hints) {
      final Point2D transformedPoint = xform.transform(mPoint, null);
      final Point2D transformedRadius = xform.deltaTransform(mRadius, null);
      return new RoundGradientContext(transformedPoint, mPointColor, transformedRadius, mBackgroundColor);
    }

    @Override
    public int getTransparency() {
      final int a1 = mPointColor.getAlpha();
      final int a2 = mBackgroundColor.getAlpha();
      return (a1 & a2) == 0xFF ? OPAQUE : TRANSLUCENT;
    }
  }

  static void drawGollopSphere(final Graphics2D g2, final Color c1, final Color c2, final int x, final int y, final int radius, final double theta) {
    final Ellipse2D e = new Ellipse2D.Float(x, y, radius, radius);
    final double sy = y + radius * Math.cos(theta);
    final double sx = x + radius * Math.sin(theta);
    final RoundGradientPaint rgp = new RoundGradientPaint(sx, sy, c1, new Point2D.Double(radius, radius), c2);
    g2.setPaint(rgp);
    g2.fill(e);
  }

  /**
   * Used for testing.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("Test");
    final Color c1 = Color.BLUE;
    final Color c2 = Color.CYAN;
    SwingUtilities.invokeLater(() -> {
      f.setSize(530, 560);
      f.setVisible(true);
      for (int k = 0; k < 200; ++k) {
        final Graphics2D g = (Graphics2D) f.getGraphics();
        if (g != null) {
          final double theta = (5 * k) % (2 * Math.PI);
          drawGollopSphere(g, c1, c2, 150, 100, 32, theta);
          drawGollopSphere(g, c1, c2, 250, 100, 16, theta);
          g.dispose();
        }
        Sleep.sleep(100);
      }
      f.dispose();
    });
  }
}
