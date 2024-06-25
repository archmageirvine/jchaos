package irvine.heraldry;

import java.awt.Graphics;
import java.io.PrintStream;

import irvine.util.string.PostScript;

/**
 * Cartouche.
 * @author Sean A. Irvine
 */
class Cartouche extends Shape {

  @Override
  protected Shape fusil() {
    return new Shield();
  }

  @Override
  protected void renderShape(final Graphics g, final int w, final Tincture t) {
    if (t != null) {
      g.setColor(t.color());
      g.fillOval(0, 0, w, heightAsInt(w));
    }
  }

  @Override
  protected void renderShape(final Graphics g, final int w) {
    renderShape(g, w, getFieldTincture());
    renderOrdinary(g, w);
    if (w >= 16 && getBorderTincture() != null) {
      g.setColor(getBorderTincture().color());
      g.drawOval(0, 0, w, heightAsInt(w));
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out, final Tincture t) {
    if (t != null) {
      out.println(PostScript.setRgbColor(t.color()));
      out.println(PostScriptHelper.fillOval(1, HEIGHT_SCALING_FACTOR));
    }
  }

  @Override
  protected void renderShapePS(final PrintStream out) {
    renderShapePS(out, getFieldTincture());
    renderOrdinaryPS(out);
    if (getBorderTincture() != null) {
      out.println(PostScript.setRgbColor(getBorderTincture().color()));
      out.println(PostScriptHelper.drawOval(1, HEIGHT_SCALING_FACTOR));
    }
  }
}
