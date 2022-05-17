package irvine.heraldry;

import java.awt.Graphics;
import java.io.PrintStream;

import irvine.util.string.PostScript;

/**
 * Shield.
 * @author Sean A. Irvine
 */
class Shield extends Shape {

  @Override
  protected int midHeight(final int w) {
    return w / 2;
  }

  @Override
  protected double midHeightPS() {
    return 0.5;
  }

  @Override
  protected Shape fusil() {
    return new Lozenge();
  }

  @Override
  protected void renderShape(final Graphics g, final int w, final Tincture t) {
    if (t != null) {
      final double tau = 0.5 * w * Math.sqrt(3);
      final int oy = (int) (height(w) - tau + 0.5);
      g.setColor(t.color());
      g.fillArc(-w, oy - w, 2 * w, 2 * w, 0, -60);
      g.fillArc(0, oy - w, 2 * w, 2 * w, 180, 60);
      g.fillRect(0, 0, w, oy);
    }
  }

  @Override
  protected void renderShape(final Graphics g, final int w) {
    renderShape(g, w, getFieldTincture());
    final double tau = 0.5 * w * Math.sqrt(3);
    final int oy = (int) (height(w) - tau + 0.5);
    renderOrdinary(g, w);
    if (w >= 16 && getBorderTincture() != null) {
      g.setColor(getBorderTincture().color());
      g.drawArc(-w, oy - w, 2 * w, 2 * w, 0, -60);
      g.drawArc(0, oy - w, 2 * w, 2 * w, 180, 60);
      g.drawLine(0, oy, 0, 0);
      g.drawLine(0, 0, w, 0);
      g.drawLine(w, 0, w, oy);
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out, final Tincture t) {
    if (t != null) {
      final double tau = 0.5 * Math.sqrt(3);
      out.println(PostScript.setRgbColor(t.color()));
      out.println(PostScriptHelper.fillArc(0, tau, 1, -60, 0));
      out.println(PostScriptHelper.fillArc(1, tau, 1, 180, 240));
      out.println(PostScriptHelper.fillRect(0, tau, 1, HEIGHT_SCALING_FACTOR - tau));
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out) {
    renderShapePS(out, getFieldTincture());
    renderOrdinaryPS(out);
    if (getBorderTincture() != null) {
      final double tau = 0.5 * Math.sqrt(3);
      out.println(PostScript.setRgbColor(getBorderTincture().color()));
      out.println(PostScriptHelper.drawArc(0, tau, 1, -60, 0));
      out.println(PostScriptHelper.drawArc(1, tau, 1, 180, 240));
      out.println("newpath 0 " + tau + " moveto 0 " + HEIGHT_SCALING_FACTOR + " lineto 1 " + HEIGHT_SCALING_FACTOR + " lineto 1 " + tau + " lineto stroke");
    }
  }
}
