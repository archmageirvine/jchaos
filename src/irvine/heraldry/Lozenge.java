package irvine.heraldry;

import java.awt.Graphics;
import java.io.PrintStream;

import irvine.util.string.PostScript;

/**
 * Lozenge.
 * @author Sean A. Irvine
 */
class Lozenge extends Shape {

  private static final double[] LOZENGE_X_COORDS = {0.5, 1, 0.5, 0};
  private static final double[] LOZENGE_Y_COORDS = {0, 0.5 * HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, 0.5 * HEIGHT_SCALING_FACTOR};

  @Override
  protected Shape fusil() {
    return new Cartouche();
  }

  @Override
  protected void renderShape(final Graphics g, final int w, final Tincture t) {
    final int[] x = new int[LOZENGE_X_COORDS.length];
    final int[] y = new int[LOZENGE_Y_COORDS.length];
    for (int k = 0; k < x.length; ++k) {
      x[k] = (int) (LOZENGE_X_COORDS[k] * w + 0.5);
      y[k] = (int) (LOZENGE_Y_COORDS[k] * w + 0.5);
    }
    if (t != null) {
      g.setColor(t.color());
      g.fillPolygon(x, y, x.length);
    }
  }

  @Override
  protected void renderShape(final Graphics g, final int w) {
    renderShape(g, w, getFieldTincture());
    final int[] x = new int[LOZENGE_X_COORDS.length];
    final int[] y = new int[LOZENGE_Y_COORDS.length];
    for (int k = 0; k < x.length; ++k) {
      x[k] = (int) (LOZENGE_X_COORDS[k] * w + 0.5);
      y[k] = (int) (LOZENGE_Y_COORDS[k] * w + 0.5);
    }
    renderOrdinary(g, w);
    if (w >= 16 && getBorderTincture() != null) {
      g.setColor(getBorderTincture().color());
      g.drawPolygon(x, y, x.length);
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out, final Tincture t) {
    if (t != null) {
      out.println(PostScript.setRgbColor(t.color()));
      out.println(PostScriptHelper.fill(LOZENGE_X_COORDS, LOZENGE_Y_COORDS));
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out) {
    renderShapePS(out, getFieldTincture());
    renderOrdinaryPS(out);
    if (getBorderTincture() != null) {
      out.println(PostScript.setRgbColor(getBorderTincture().color()));
      out.println(PostScriptHelper.stroke(LOZENGE_X_COORDS, LOZENGE_Y_COORDS));
    }
  }
}
