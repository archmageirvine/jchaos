package irvine.heraldry;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.PrintStream;

import irvine.math.r.Constants;
import irvine.util.string.PostScript;

/**
 * Base class for representing a shield.
 * @author Sean A. Irvine
 */
public abstract class Shape {

  protected static final double HEIGHT_SCALING_FACTOR = 4.0 / 3.0;
  private static final double THIRD = 1.0 / 3.0;
  private static final double SIXTH = 1.0 / 6.0;

  private Tincture mBorderTincture = Tincture.SABLE;
  private Tincture mFieldTincture = null;
  private Tincture mOrdinaryTincture = null;
  private Ordinary mOrdinary = null;

  protected abstract void renderShape(final Graphics g, final int w);

  protected abstract void renderShape(final Graphics g, final int w, final Tincture t);

  protected abstract void renderShapePS(final PrintStream out);

  protected abstract void renderShapePS(final PrintStream out, final Tincture t);

  protected int midHeight(final int w) {
    return (int) (height(w) * 0.5 + 0.5);
  }

  protected double midHeightPS() {
    return HEIGHT_SCALING_FACTOR * 0.5;
  }

  public void setFieldTincture(final Tincture tincture) {
    mFieldTincture = tincture;
  }

  public Tincture getFieldTincture() {
    return mFieldTincture;
  }

  public void setBorderTincture(final Tincture tincture) {
    mBorderTincture = tincture;
  }

  public Tincture getBorderTincture() {
    return mBorderTincture;
  }

  public void setOrdinary(final Ordinary ordinary) {
    mOrdinary = ordinary;
  }

  public Ordinary getOrdinary() {
    return mOrdinary;
  }

  public void setOrdinaryTincture(final Tincture tincture) {
    mOrdinaryTincture = tincture;
  }

  public Tincture getOrdinaryTincture() {
    return mOrdinaryTincture;
  }

  private void renderBillets(final Graphics g, final int w) {
    final int h = heightAsInt(w);
    final int bw = w / 6;
    final int bh = heightAsInt(w * 0.2);
    g.setColor(getOrdinaryTincture().color());
    g.fillRect((w - bw) / 2, (h + bw / 2) / 2, bw, bh);
    g.fillRect(w / 3 - bw / 2, (h - bw / 2) / 2 - bh, bw, bh);
    g.fillRect(2 * w / 3 - bw / 2, (h - bw / 2) / 2 - bh, bw, bh);
  }

  private void clipAndRender(final Graphics g, final int w, final Tincture t, final int[] x, final int[] y) {
    g.setClip(new Polygon(x, y, x.length));
    renderShape(g, w, t);
  }

  private void renderBendlets(final Graphics g, final int w, final Tincture t) {
    clipAndRender(g, w, t,
      new int[] {w / 16, 3 * w / 16, w, w},
      new int[] {0, 0, heightAsInt(13 * w / 16.0), heightAsInt(15 * w / 16.0)});
    clipAndRender(g, w, t,
      new int[] {0, 0, 15 * w / 16, 13 * w / 16},
      new int[] {heightAsInt(3 * w / 16.0), heightAsInt(w / 16.0), heightAsInt(w), heightAsInt(w)});
  }

  private void renderBendletsSinister(final Graphics g, final int w, final Tincture t) {
    clipAndRender(g, w, t,
      new int[] {15 * w / 16, 13 * w / 16, 0, 0},
      new int[] {0, 0, heightAsInt(13 * w / 16.0), heightAsInt(15 * w / 16.0)});
    clipAndRender(g, w, t,
      new int[] {w, w, w / 16, 3 * w / 16},
      new int[] {heightAsInt(3 * w / 16.0), heightAsInt(w / 16.0), heightAsInt(w), heightAsInt(w)});
  }

  private void renderChevronels(final Graphics g, final int w, final Tincture t) {
    final int w4 = w / 4;
    final int w8 = w / 8;
    final int h9 = heightAsInt(9 * w / 16.0);
    final int h15 = heightAsInt(15 * w / 16.0);
    final int[] chevX = {0, w / 2, w, w, w / 2, 0};
    clipAndRender(g, w, t, chevX,
      new int[] {h15 - w8, h9 - w8, h15 - w8, h15, h9, h15});
    clipAndRender(g, w, t, chevX,
      new int[] {h15 - w8 + w4, h9 - w8 + w4, h15 - w8 + w4, h15 + w4, h9 + w4, h15 + w4});
  }

  private void renderSaltire(final Graphics g, final int w, final Tincture t) {
    final int m = midHeight(w);
    final int w0 = w / 16;
    final int w2 = 15 * w / 16;
    final int w3 = 17 * w / 16;
    clipAndRender(g, w, t,
      new int[] {w0, w / 2, w2, w3, w / 2 + w0, w3, w2, w / 2, w0, -w0, w / 2 - w0, -w0},
      new int[] {0, 7 * m / 8, 0, 0, m, 2 * m, 2 * m, 9 * m / 8, 2 * m, 2 * m, m, 0});
  }

  private void renderFlanches(final Graphics g, final int w, final Tincture t) {
    final int oy = heightAsInt(w * 0.5);
    final int radius = heightAsInt(w / Constants.SQRT2);
    final int diameter = 2 * radius;
    final int r2 = radius * radius;
    final int[] fx = new int[diameter];
    final int[] fy = new int[diameter];
    for (int k = 0; k < diameter; ++k) {
      fy[k] = k;
      final int yp = k - oy;
      fx[k] = w + oy - (int) (Math.sqrt(r2 - yp * yp) + 0.5);
    }
    clipAndRender(g, w, t, fx, fy);
    for (int k = 0; k < diameter; ++k) {
      final int yp = k - oy;
      fx[k] = (int) (Math.sqrt(r2 - yp * yp) + 0.5) - oy;
    }
    clipAndRender(g, w, t, fx, fy);
  }

  private void renderBarry(final Graphics g, final int w, final Tincture t) {
    final int bh = heightAsInt(w * 0.125);
    for (int k = 0; k < 4; ++k) {
      g.setClip(0, heightAsInt(4 * k * w / 16.0), w, bh);
      renderShape(g, w, t);
    }
  }

  private void renderBendy(final Graphics g, final int w, final Tincture t) {
    final int[] x = {0, w, w, 0};
    for (int k = -4; k < 5; ++k) {
      final int z = -heightAsInt(k * w * 0.25);
      final int m = heightAsInt(w);
      final int[] y = {z, z + m, z + m + w / 8, z + w / 8};
      clipAndRender(g, w, t, x, y);
    }
  }

  private void renderPaly(final Graphics g, final int w, final Tincture t) {
    for (int k = 0; k < 3; ++k) {
      g.setClip(k * w / 3, 0, w / 6, heightAsInt(w));
      renderShape(g, w, t);
    }
  }

  protected abstract Shape fusil();

  private void renderFusil(final Graphics g, final int w, final Tincture t) {
    g.translate(w / 3, heightAsInt(w * THIRD));
    final Shape sub = fusil();
    sub.renderShape(g, w / 3, t);
    g.translate(-w / 3, -heightAsInt(w * THIRD));
  }

  private void renderRustre(final Graphics g, final int w, final Tincture t) {
    g.translate(w / 6, heightAsInt(w * SIXTH));
    final Shape sub = fusil();
    sub.renderShape(g, 2 * w / 3, t);
    final Shape sub2 = sub.fusil();
    g.translate(-w / 6, -heightAsInt(w * SIXTH));
    g.translate(w / 3, heightAsInt(w * THIRD));
    sub2.renderShape(g, w / 3, getFieldTincture());
    g.translate(-w / 3, -heightAsInt(w * THIRD));
  }

  private void renderAnnulet(final Graphics g, final int w, final Tincture t) {
    g.setColor(t.color());
    g.fillOval(w / 6, heightAsInt(0.5 * w) - w / 3, 2 * w / 3, 2 * w / 3);
    g.setColor(getFieldTincture().color());
    g.fillOval(w / 3, heightAsInt(0.5 * w) - w / 6, w / 3, w / 3);
  }

  private void renderBars(final Graphics g, final int w, final Tincture t) {
    g.setClip(0, heightAsInt(5 * w / 16.0), w, heightAsInt(w * 0.125));
    renderShape(g, w, t);
    g.setClip(0, heightAsInt(9 * w / 16.0), w, heightAsInt(w * 0.125));
    renderShape(g, w, t);
  }

  private void renderCross(final Graphics g, final int w, final Tincture t) {
    g.setClip(3 * w / 8, 0, w / 4, heightAsInt(w));
    renderShape(g, w, t);
    g.setClip(0, 3 * w / 8, w, w / 4);
    renderShape(g, w, t);
  }

  private void renderOrle(final Graphics g, final int w, final Tincture t) {
    g.translate(w / 8, heightAsInt(w * 0.125));
    renderShape(g, 3 * w / 4, t);
    g.translate(-w / 8, -heightAsInt(w * 0.125));
    g.translate(w / 4, heightAsInt(w * 0.25));
    renderShape(g, w / 2, getFieldTincture());
    g.translate(-w / 4, -heightAsInt(w * 0.25));
  }

  private void renderGyronny(final Graphics g, final int w, final Tincture t) {
    final int m = midHeight(w);
    final int z = heightAsInt(w);
    clipAndRender(g, w, t, new int[] {0, w / 2, 0}, new int[] {0, m, m});
    clipAndRender(g, w, t, new int[] {w / 2, w, w / 2}, new int[] {0, 0, m});
    clipAndRender(g, w, t, new int[] {0, w / 2, w / 2}, new int[] {z, m, z});
    clipAndRender(g, w, t, new int[] {w / 2, w, w}, new int[] {m, m, z});
  }

  protected void renderOrdinary(final Graphics g, final int w) {
    final Ordinary ord = getOrdinary();
    if (ord != null) {
      final Tincture t = getOrdinaryTincture();
      if (t != null && t != getFieldTincture()) {
        final Rectangle r = g.getClipBounds();
        switch (ord) {
          case ANNULET:
            renderAnnulet(g, w, t);
            break;
          case BARRY:
            renderBarry(g, w, t);
            break;
          case PALY:
            renderPaly(g, w, t);
            break;
          case BENDY:
            renderBendy(g, w, t);
            break;
          case FUSIL:
            renderFusil(g, w, t);
            break;
          case RUSTRE:
            renderRustre(g, w, t);
            break;
          case BARS:
            renderBars(g, w, t);
            break;
          case BEND:
            clipAndRender(g, w, t,
              new int[] {0, w / 8, w, w, 7 * w / 8, 0},
              new int[] {0, 0, heightAsInt(7 * w) / 8, heightAsInt(w), heightAsInt(w), heightAsInt(w * 0.125)});
            break;
          case BEND_SINISTER:
            clipAndRender(g, w, t,
              new int[] {0, w / 8, w, w, 7 * w / 8, 0},
              new int[] {heightAsInt(w), heightAsInt(w), heightAsInt(w * 0.125), 0, 0, heightAsInt(7 * w / 8.0)});
            break;
          case BENDLETS:
            renderBendlets(g, w, t);
            break;
          case BENDLETS_SINISTER:
            renderBendletsSinister(g, w, t);
            break;
          case BILLETS:
            renderBillets(g, w);
            break;
          case BORDURE:
            g.translate(w / 8, heightAsInt(w * 0.125));
            renderShape(g, 3 * w / 4, t);
            g.translate(-w / 8, -heightAsInt(w * 0.125));
            break;
          case CANTON:
            g.setClip(0, 0, w / 2, midHeight(w));
            renderShape(g, w, t);
            break;
          case CHIEF:
            g.setClip(0, 0, w, heightAsInt(0.25 * w));
            renderShape(g, w, t);
            break;
          case CHEVRONELS:
            renderChevronels(g, w, t);
            break;
          case CROSS:
            renderCross(g, w, t);
            break;
          case FESS:
            g.setClip(0, heightAsInt(w * 0.5), w, heightAsInt(w * 0.125));
            renderShape(g, w, t);
            break;
          case FLANCHES:
            renderFlanches(g, w, t);
            break;
          case ORLE:
            renderOrle(g, w, t);
            break;
          case GYRONNY:
            renderGyronny(g, w, t);
            break;
          case INESCUTCHEON:
            g.translate(w / 3, heightAsInt(w * THIRD));
            renderShape(g, w / 3, t);
            g.translate(-w / 3, -heightAsInt(w * THIRD));
            break;
          case PALE:
            g.setClip(w / 4, 0, w / 2, heightAsInt(w));
            renderShape(g, w, t);
            break;
          case PALLETS:
            g.setClip(w / 4, 0, w / 8, heightAsInt(w));
            renderShape(g, w, t);
            g.setClip(5 * w / 8, 0, w / 8, heightAsInt(w));
            renderShape(g, w, t);
            break;
          case PARTY_PER_BEND:
            clipAndRender(g, w, t, new int[] {0, w, 0}, new int[] {0, heightAsInt(w), heightAsInt(w)});
            break;
          case PARTY_PER_BEND_SINISTER:
            clipAndRender(g, w, t, new int[] {w, 0, w}, new int[] {0, heightAsInt(w), heightAsInt(w)});
            break;
          case PARTY_PER_CHEVRON:
            clipAndRender(g, w, t, new int[] {0, w / 2, w}, new int[] {heightAsInt(w), heightAsInt(w * 0.5), heightAsInt(w)});
            break;
          case PARTY_PER_SALTIRE:
            final int[] sy = {2 * midHeight(w), midHeight(w), 0};
            clipAndRender(g, w, t, new int[] {0, w / 2, 0}, sy);
            clipAndRender(g, w, t, new int[] {w, w / 2, w}, sy);
            break;
          case PARTY_PER_CROSS:
            g.setClip(0, heightAsInt(w * 0.5), w / 2, heightAsInt(w * 0.5) + 1);
            renderShape(g, w, t);
            g.setClip(w / 2, 0, w, heightAsInt(w * 0.5));
            renderShape(g, w, t);
            break;
          case PARTY_PER_FESS:
            g.setClip(0, heightAsInt(w * 0.5), w, heightAsInt(w * 0.5) + 1);
            renderShape(g, w, t);
            break;
          case PARTY_PER_PALE:
            g.setClip(0, 0, w / 2, heightAsInt(w));
            renderShape(g, w, t);
            break;
          case PILE:
            clipAndRender(g, w, t, new int[] {0, w, w / 2}, new int[] {0, 0, (int) height(0.5 * w)});
            break;
          case SALTIRE:
            renderSaltire(g, w, t);
            break;
          default:
            break; // too bad, don't know how to draw it
        }
        g.setClip(r);
      }
    }
  }

  private static final double HX1 = 0.0625 * HEIGHT_SCALING_FACTOR;
  private static final double HX2 = 0.1250 * HEIGHT_SCALING_FACTOR;
  private static final double HX3 = 0.1875 * HEIGHT_SCALING_FACTOR;
  private static final double HX4 = 0.2500 * HEIGHT_SCALING_FACTOR;
  private static final double HX5 = 0.3125 * HEIGHT_SCALING_FACTOR;
  private static final double HX6 = 0.3750 * HEIGHT_SCALING_FACTOR;
  private static final double HX7 = 0.4375 * HEIGHT_SCALING_FACTOR;
  private static final double HX8 = 0.5000 * HEIGHT_SCALING_FACTOR;
  private static final double HX9 = 0.5625 * HEIGHT_SCALING_FACTOR;
  private static final double HX12 = 0.7500 * HEIGHT_SCALING_FACTOR;
  private static final double HX13 = 0.8125 * HEIGHT_SCALING_FACTOR;
  private static final double HX14 = 0.8750 * HEIGHT_SCALING_FACTOR;
  private static final double HX15 = 0.9375 * HEIGHT_SCALING_FACTOR;
  private static final double[] PILE_X_PS = {0, 1, 0.5};
  private static final double[] PILE_Y_PS = {HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, HX8};
  private static final double[] PARTY_PER_BEND_X_PS = {0, 1, 0};
  private static final double[] PARTY_PER_BEND_Y_PS = {HEIGHT_SCALING_FACTOR, 0, 0};
  private static final double[] PARTY_PER_BEND_SINISTER_X_PS = {1, 0, 1};
  private static final double[] BEND_X_PS = {0, 0.125, 1, 1, 0.875, 0};
  private static final double[] BEND_Y_PS = {HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, HX2, 0, 0, HX14};
  private static final double[] BEND_YS_PS = {0, 0, HX14, HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, HX2};
  private static final double[] BENDLETS_X1_PS = {0.0625, 0.1875, 1, 1};
  private static final double[] BENDLETS_Y1_PS = {HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, HX3, HX1};
  private static final double[] BENDLETS_X1S_PS = {0.9375, 0.8125, 0, 0};
  private static final double[] BENDLETS_X2_PS = {0, 0, 0.9375, 0.8125};
  private static final double[] BENDLETS_Y2_PS = {HX13, HX15, 0, 0};
  private static final double[] BENDLETS_X2S_PS = {1, 1, 0.0625, 0.1875};
  private static final double[] CHEVRONELS_X_PS = {0, 0.5, 1, 1, 0.5, 0};
  private static final double[] CHEVRONELS_Y1_PS = {HX1 - 0.125, HX7 - 0.125, HX1 - 0.125, HX1, HX7, HX1};
  private static final double[] CHEVRONELS_Y2_PS = {HX1 - 0.125 + 0.25, HX7 - 0.125 + 0.25, HX1 - 0.125 + 0.25, HX1 + 0.25, HX7 + 0.25, HX1 + 0.25};
  private static final double[] SALTIRE_X = {0.0625, 0.5, 0.9375, 1.0625, 0.5625, 1.0625, 0.9375, 0.5, 0.0625, -0.0625, 0.4375, -0.0625};
  private static final double[][] GYRONNY_X = {{0, 0.5, 0}, {0.5, 1, 0.5}, {0, 0.5, 0.5}, {0.5, 1, 1}};
  private static final double[] PARTY_PER_CHEVRON_X_PS = {0, 0.5, 1};
  private static final double[] PARTY_PER_CHEVRON_Y_PS = {0, HX8, 0};
  private static final double[] PARTY_PER_SALTIRE_X1_PS = {0, 0.5, 0};
  private static final double[] PARTY_PER_SALTIRE_X2_PS = {1, 0.5, 1};
  private static final double[] BENDY_X = {0, 1, 1, 0};

  private void renderFlanchesPS(final PrintStream out, final Tincture t) {
    final double radius = HEIGHT_SCALING_FACTOR / Constants.SQRT2;
    out.println("gsave");
    out.println(PostScriptHelper.clipArc(1 + HX8, HX8, radius, 0, 360));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clipArc(-HX8, HX8, radius, 0, 360));
    renderShapePS(out, t);
  }

  private void renderGyronnyPS(final PrintStream out, final Tincture t) {
    final double mg = HEIGHT_SCALING_FACTOR - midHeightPS();
    final double[][] gy = {{mg, mg, HEIGHT_SCALING_FACTOR}, {HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, mg}, {0, mg, 0}, {mg, mg, 0}};
    for (int k = 0; k < GYRONNY_X.length; ++k) {
      out.println("gsave");
      out.println(PostScriptHelper.clip(GYRONNY_X[k], gy[k]));
      renderShapePS(out, t);
      out.println("grestore");
    }
  }

  private void renderPartyPerSaltirePS(final PrintStream out, final Tincture t) {
    final double h = midHeightPS();
    final double[] sy = {HEIGHT_SCALING_FACTOR - 2 * h, HEIGHT_SCALING_FACTOR - h, HEIGHT_SCALING_FACTOR};
    out.println("gsave");
    out.println(PostScriptHelper.clip(PARTY_PER_SALTIRE_X1_PS, sy));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clip(PARTY_PER_SALTIRE_X2_PS, sy));
    renderShapePS(out, t);
  }

  private void renderPalletsPS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(PostScriptHelper.clipRect(0.25, 0, 0.125, HEIGHT_SCALING_FACTOR));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clipRect(0.625, 0, 0.125, HEIGHT_SCALING_FACTOR));
    renderShapePS(out, t);
  }

  private void renderBarryPS(final PrintStream out, final Tincture t) {
    for (int k = 0; k < 4; ++k) {
      out.println("gsave");
      out.println(PostScriptHelper.clipRect(0, HEIGHT_SCALING_FACTOR - HX2 - k * 0.25 * HEIGHT_SCALING_FACTOR, 1, HX2));
      renderShapePS(out, t);
      out.println("grestore");
    }
  }

  private void renderPalyPS(final PrintStream out, final Tincture t) {
    for (int k = 0; k < 3; ++k) {
      out.println("gsave");
      out.println(PostScriptHelper.clipRect(k / 3.0, 0, 1.0 / 6.0, HEIGHT_SCALING_FACTOR));
      renderShapePS(out, t);
      out.println("grestore");
    }
  }

  private void renderFusilPS(final PrintStream out, final Tincture t) {
    out.println(THIRD + " " + THIRD * HEIGHT_SCALING_FACTOR + " translate");
    out.println(THIRD + " " + THIRD + " scale");
    final Shape sub = fusil();
    sub.renderShapePS(out, t);
  }

  private void renderRustrePS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(SIXTH + " " + SIXTH * HEIGHT_SCALING_FACTOR + " translate");
    out.println(2 * THIRD + " " + 2 * THIRD + " scale");
    final Shape sub = fusil();
    sub.renderShapePS(out, t);
    out.println("grestore");
    final Shape sub2 = sub.fusil();
    out.println(THIRD + " " + THIRD * HEIGHT_SCALING_FACTOR + " translate");
    out.println(1 * THIRD + " " + 1 * THIRD + " scale");
    sub2.renderShapePS(out, getFieldTincture());
  }

  private void renderBendyPS(final PrintStream out, final Tincture t) {
    for (int k = -4; k < 5; ++k) {
      final double z = -k * HX4;
      final double[] y = {z + HEIGHT_SCALING_FACTOR, z, z - 0.125, z + HEIGHT_SCALING_FACTOR - 0.125};
      out.println("gsave");
      out.println(PostScriptHelper.clip(BENDY_X, y));
      renderShapePS(out, t);
      out.println("grestore");
    }
  }

  private void renderBilletsPS(final PrintStream out) {
    final double bw = 1.0 / 6.0;
    final double bh = height(0.2);
    out.println(PostScriptHelper.fillRect((1 - bw) * 0.5, (HEIGHT_SCALING_FACTOR - bw * 0.5) * 0.5 - bh, bw, bh));
    out.println(PostScriptHelper.fillRect(THIRD - bw * 0.5, (HEIGHT_SCALING_FACTOR + bw * 0.5) * 0.5, bw, bh));
    out.println(PostScriptHelper.fillRect(2.0 / 3 - bw * 0.5, (HEIGHT_SCALING_FACTOR + bw * 0.5) * 0.5, bw, bh));
  }

  private void renderAnnuletPS(final PrintStream out) {
    out.println("gsave");
    out.println(SIXTH + " " + (HX8 - THIRD) + " translate");
    out.println(PostScriptHelper.fillOval(2 * THIRD, 2 * THIRD));
    out.println("grestore");
    out.println(PostScript.setRgbColor(getFieldTincture().color()));
    out.println(THIRD + " " + (HX8 - SIXTH) + " translate");
    out.println(PostScriptHelper.fillOval(THIRD, THIRD));
  }

  private void renderBarsPS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(PostScriptHelper.clipRect(0, HX9, 1, HX2));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clipRect(0, HX5, 1, HX2));
    renderShapePS(out, t);
  }

  private void renderOrlePS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println("0.125 " + HX2 + " translate 0.75 0.75 scale");
    renderShapePS(out, t);
    out.println("grestore");
    out.println("0.25 " + HX4 + " translate 0.5 0.5 scale");
    renderShapePS(out, getFieldTincture());
  }

  private void renderCrossPS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(PostScriptHelper.clipRect(0.375, 0, 0.25, HEIGHT_SCALING_FACTOR));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clipRect(0, HEIGHT_SCALING_FACTOR - 0.625, 1, 0.25));
    renderShapePS(out, t);
  }

  private void renderBendletsPS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(PostScriptHelper.clip(BENDLETS_X1_PS, BENDLETS_Y1_PS));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clip(BENDLETS_X2_PS, BENDLETS_Y2_PS));
    renderShapePS(out, t);
  }

  private void renderBendletsSinisterPS(final PrintStream out, final Tincture t) {
    out.println("gsave");
    out.println(PostScriptHelper.clip(BENDLETS_X1S_PS, BENDLETS_Y1_PS));
    renderShapePS(out, t);
    out.println("grestore");
    out.println(PostScriptHelper.clip(BENDLETS_X2S_PS, BENDLETS_Y2_PS));
    renderShapePS(out, t);
  }

  protected void renderOrdinaryPS(final PrintStream out) {
    final Ordinary ord = getOrdinary();
    if (ord != null) {
      final Tincture t = getOrdinaryTincture();
      if (t != null && t != getFieldTincture()) {
        out.println("gsave");
        out.println(PostScript.setRgbColor(t.color()));
        switch (ord) {
          case ANNULET:
            renderAnnuletPS(out);
            break;
          case BARRY:
            renderBarryPS(out, t);
            break;
          case BENDY:
            renderBendyPS(out, t);
            break;
          case RUSTRE:
            renderRustrePS(out, t);
            break;
          case PALY:
            renderPalyPS(out, t);
            break;
          case FUSIL:
            renderFusilPS(out, t);
            break;
          case BARS:
            renderBarsPS(out, t);
            break;
          case BEND:
            out.println(PostScriptHelper.clip(BEND_X_PS, BEND_Y_PS));
            renderShapePS(out, t);
            break;
          case BEND_SINISTER:
            out.println(PostScriptHelper.clip(BEND_X_PS, BEND_YS_PS));
            renderShapePS(out, t);
            break;
          case BENDLETS:
            renderBendletsPS(out, t);
            break;
          case BENDLETS_SINISTER:
            renderBendletsSinisterPS(out, t);
            break;
          case BILLETS:
            renderBilletsPS(out);
            break;
          case BORDURE:
            out.println("0.125 " + HX2 + " translate");
            out.println("0.75 0.75 scale");
            renderShapePS(out, t);
            break;
          case CANTON:
            final double mh = midHeightPS();
            out.println(PostScriptHelper.clipRect(0, HEIGHT_SCALING_FACTOR - mh, 0.5, mh));
            renderShapePS(out, t);
            break;
          case CHEVRONELS:
            out.println("gsave");
            out.println(PostScriptHelper.clip(CHEVRONELS_X_PS, CHEVRONELS_Y1_PS));
            renderShapePS(out, t);
            out.println("grestore");
            out.println(PostScriptHelper.clip(CHEVRONELS_X_PS, CHEVRONELS_Y2_PS));
            renderShapePS(out, t);
            break;
          case CHIEF:
            out.println(PostScriptHelper.clipRect(0, HX12, 1, HEIGHT_SCALING_FACTOR));
            renderShapePS(out, t);
            break;
          case CROSS:
            renderCrossPS(out, t);
            break;
          case ORLE:
            renderOrlePS(out, t);
            break;
          case GYRONNY:
            renderGyronnyPS(out, t);
            break;
          case FESS:
            out.println(PostScriptHelper.clipRect(0, HX6, 1, HX2));
            renderShapePS(out, t);
            break;
          case FLANCHES:
            renderFlanchesPS(out, t);
            break;
          case INESCUTCHEON:
            out.println(THIRD + " " + THIRD * HEIGHT_SCALING_FACTOR + " translate");
            out.println(THIRD + " " + THIRD + " scale");
            renderShapePS(out, t);
            break;
          case PALE:
            out.println(PostScriptHelper.clipRect(0.25, 0, 0.5, HEIGHT_SCALING_FACTOR));
            renderShapePS(out, t);
            break;
          case PALLETS:
            renderPalletsPS(out, t);
            break;
          case PARTY_PER_FESS:
            out.println(PostScriptHelper.clipRect(0, 0, 1, HX8));
            renderShapePS(out, t);
            break;
          case PARTY_PER_PALE:
            out.println(PostScriptHelper.clipRect(0, 0, 0.5, HEIGHT_SCALING_FACTOR));
            renderShapePS(out, t);
            break;
          case PARTY_PER_BEND:
            out.println(PostScriptHelper.clip(PARTY_PER_BEND_X_PS, PARTY_PER_BEND_Y_PS));
            renderShapePS(out, t);
            break;
          case PARTY_PER_CHEVRON:
            out.println(PostScriptHelper.clip(PARTY_PER_CHEVRON_X_PS, PARTY_PER_CHEVRON_Y_PS));
            renderShapePS(out, t);
            break;
          case PARTY_PER_BEND_SINISTER:
            out.println(PostScriptHelper.clip(PARTY_PER_BEND_SINISTER_X_PS, PARTY_PER_BEND_Y_PS));
            renderShapePS(out, t);
            break;
          case PARTY_PER_CROSS:
            out.println("gsave");
            out.println(PostScriptHelper.clipRect(0, 0, 0.5, HX8));
            renderShapePS(out, t);
            out.println("grestore");
            out.println(PostScriptHelper.clipRect(0.5, HX8, 1, HX8));
            renderShapePS(out, t);
            break;
          case PILE:
            out.println(PostScriptHelper.clip(PILE_X_PS, PILE_Y_PS));
            renderShapePS(out, t);
            break;
          case SALTIRE:
            final double m = midHeightPS();
            out.println(PostScriptHelper.clip(SALTIRE_X, new double[] {HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR - 0.875 * m, HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR, HEIGHT_SCALING_FACTOR - m, HEIGHT_SCALING_FACTOR - 2 * m, HEIGHT_SCALING_FACTOR - 2 * m, HEIGHT_SCALING_FACTOR - 1.125 * m, HEIGHT_SCALING_FACTOR - 2 * m, HEIGHT_SCALING_FACTOR - 2 * m, HEIGHT_SCALING_FACTOR - m, HEIGHT_SCALING_FACTOR}));
            renderShapePS(out, t);
            break;
          case PARTY_PER_SALTIRE:
            renderPartyPerSaltirePS(out, t);
            break;
          default:
            break; // too bad, don't know how to draw it
        }
        out.println("grestore");
      }
    }
  }

  /**
   * Natural height of this shape for a given width.
   * @param width width
   * @return corresponding height
   */
  public static double height(final double width) {
    return HEIGHT_SCALING_FACTOR * width;
  }

  /**
   * Natural integer height of this shape for a given width.
   * @param width width
   * @return corresponding height
   */
  public static int heightAsInt(final double width) {
    return (int) (HEIGHT_SCALING_FACTOR * width + 0.5);
  }

  /**
   * Render this shape into the given graphics at specified size.
   * If the graphics is null or the width is too small then the
   * request is ignored.
   * @param g where to draw
   * @param w width to draw
   * @param dx x offset
   * @param dy y offset
   */
  public void render(final Graphics g, final int w, final int dx, final int dy) {
    if (g != null && w > 0) {
      g.translate(dx, dy);
      renderShape(g, w);
      g.translate(-dx, -dy);
    }
  }

  /**
   * Render this shape in PostScript.  Assumes rendering is to
   * occur with a unit width at indicated start position.
   * @param out output stream
   */
  public void renderPostScript(final PrintStream out) {
    if (out != null) {
      renderShapePS(out);
    }
  }
}
