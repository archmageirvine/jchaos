package chaos.graphics;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Graphics for testing.
 *
 * @author Sean A. Irvine
 */
public class MockGraphics extends Graphics2D {

  protected final StringBuilder mHistory = new StringBuilder();

  @Override
  public void dispose() { }

  @Override
  public void clearRect(final int x, final int y, final int width, final int height) {
    mHistory.append("clearRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void clipRect(final int x, final int y, final int width, final int height) {
    mHistory.append("clipRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void copyArea(final int x, final int y, final int width, final int height, final int dx, final int dy) {
    mHistory.append("copyArea(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(dx)
      .append(',')
      .append(dy)
      .append(")#");
  }

  @Override
  public void draw3DRect(final int x, final int y, final int width, final int height, final boolean raised) {
    mHistory.append("draw3DRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(raised)
      .append(")#");
  }

  @Override
  public void fill3DRect(final int x, final int y, final int width, final int height, final boolean raised) {
    mHistory.append("fill3DRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(raised)
      .append(")#");
  }

  @Override
  public void draw(final Shape s) {

  }

  @Override
  public boolean drawImage(final Image img, final AffineTransform xform, final ImageObserver obs) {
    return false;
  }

  @Override
  public void drawImage(final BufferedImage img, final BufferedImageOp op, final int x, final int y) {

  }

  @Override
  public void drawRenderedImage(final RenderedImage img, final AffineTransform xform) {

  }

  @Override
  public void drawRenderableImage(final RenderableImage img, final AffineTransform xform) {

  }

  @Override
  public void drawArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    mHistory.append("drawArc(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(startAngle)
      .append(',')
      .append(arcAngle)
      .append(")#");
  }

  @Override
  public void fillArc(final int x, final int y, final int width, final int height, final int startAngle, final int arcAngle) {
    mHistory.append("fillArc(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(startAngle)
      .append(',')
      .append(arcAngle)
      .append(")#");
  }

  @Override
  public boolean drawImage(final Image img, final int x, final int y, final Color bgcolor, final ImageObserver observer) {
    mHistory.append("I(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(bgcolor)
      .append(")#");
    return true;
  }

  @Override
  public boolean drawImage(final Image img, final int x, final int y, final ImageObserver observer) {
    return drawImage(img, x, y, null, observer);
  }

  @Override
  public boolean drawImage(final Image img, final int x, final int y, final int width, final int height, final Color bgcolor, final ImageObserver observer) {
    mHistory.append("I(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(bgcolor)
      .append(")#");
    return true;
  }

  @Override
  public boolean drawImage(final Image img, final int x, final int y, final int width, final int height, final ImageObserver observer) {
    return drawImage(img, x, y, width, height, null, observer);
  }

  @Override
  public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2, final int dy2, final int sx1, final int sy1, final int sx2, final int sy2, final Color bgcolor, final ImageObserver observer) {
    mHistory.append("I(")
      .append(dx1)
      .append(',')
      .append(dy1)
      .append(',')
      .append(dx2)
      .append(',')
      .append(dy2)
      .append(',')
      .append(sx1)
      .append(',')
      .append(sy1)
      .append(',')
      .append(sx2)
      .append(',')
      .append(sy2)
      .append(',')
      .append(bgcolor)
      .append(")#");
    return true;
  }

  @Override
  public boolean drawImage(final Image img, final int dx1, final int dy1, final int dx2, final int dy2, final int sx1, final int sy1, final int sx2, final int sy2, final ImageObserver observer) {
    return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
  }

  @Override
  public void drawLine(final int x1, final int y1, final int x2, final int y2) {
    mHistory.append("L(")
      .append(x1)
      .append(',')
      .append(y1)
      .append(',')
      .append(x2)
      .append(',')
      .append(y2)
      .append(")#");
  }

  @Override
  public void drawOval(final int x, final int y, final int width, final int height) {
    mHistory.append("drawOval(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void fillOval(final int x, final int y, final int width, final int height) {
    mHistory.append("fillOval(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void drawPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
    mHistory.append("drawPolygon(").append(nPoints).append(")#");
  }

  @Override
  public void fillPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
    mHistory.append("fillPolygon(").append(nPoints).append(")#");
  }

  @Override
  public void drawPolyline(final int[] xPoints, final int[] yPoints, final int nPoints) {
    mHistory.append("drawPolyline(").append(nPoints).append(")#");
  }

  @Override
  public void drawRect(final int x, final int y, final int width, final int height) {
    mHistory.append("drawRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void drawRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    mHistory.append("drawRoundRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(arcWidth)
      .append(',')
      .append(arcHeight)
      .append(")#");
  }

  @Override
  public void fillRect(final int x, final int y, final int width, final int height) {
    mHistory.append("fillRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }

  @Override
  public void fillRoundRect(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight) {
    mHistory.append("fillRoundRect(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(',')
      .append(arcWidth)
      .append(',')
      .append(arcHeight)
      .append(")#");
  }

  @Override
  public void drawString(final AttributedCharacterIterator iterator, final int x, final int y) {
    mHistory.append("drawString(ACI")
      .append(x)
      .append(',')
      .append(y)
      .append(")#");
  }

  @Override
  public void drawString(final AttributedCharacterIterator iterator, final float x, final float y) {

  }

  @Override
  public void drawGlyphVector(final GlyphVector g, final float x, final float y) {

  }

  @Override
  public void fill(final Shape s) {

  }

  @Override
  public boolean hit(final Rectangle rect, final Shape s, final boolean onStroke) {
    return false;
  }

  @Override
  public GraphicsConfiguration getDeviceConfiguration() {
    return null;
  }

  @Override
  public void setComposite(final Composite comp) {

  }

  @Override
  public void setPaint(final Paint paint) {

  }

  @Override
  public void setStroke(final Stroke s) {

  }

  @Override
  public void setRenderingHint(final RenderingHints.Key hintKey, final Object hintValue) {

  }

  @Override
  public Object getRenderingHint(final RenderingHints.Key hintKey) {
    return null;
  }

  @Override
  public void setRenderingHints(final Map<?, ?> hints) {

  }

  @Override
  public void addRenderingHints(final Map<?, ?> hints) {

  }

  @Override
  public RenderingHints getRenderingHints() {
    return null;
  }

  @Override
  public void drawString(final String str, final int x, final int y) {
    mHistory.append("drawString(")
      .append(str)
      .append(',')
      .append(x)
      .append(',')
      .append(y)
      .append(")#");
  }

  @Override
  public void drawString(final String str, final float x, final float y) {

  }

  @Override
  public Graphics create(final int x, final int y, final int width, final int height) {
    mHistory.append("create(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
    return null;
  }

  @Override
  public Graphics create() {
    mHistory.append("create()#");
    return null;
  }

  @Override
  public Shape getClip() {
    mHistory.append("getClip()#");
    return null;
  }

  @Override
  public Rectangle getClipBounds() {
    mHistory.append("getClipBounds()#");
    return null;
  }

  private Color mColor = Color.BLACK;

  @Override
  public Color getColor() {
    mHistory.append("getColor()#");
    return mColor;
  }

  @Override
  public void setColor(final Color color) {
    mHistory.append("setColor(")
      .append(color)
      .append(")#");
    mColor = color;
  }

  private Font mFont = null;

  @Override
  public Font getFont() {
    mHistory.append("getFont()#");
    return mFont;
  }

  @Override
  public void setFont(final Font font) {
    mHistory.append("setFont()#");
    mFont = font;
  }

  @Override
  public FontMetrics getFontMetrics() {
    mHistory.append("getFontMetrics()#");
    return null;
  }

  @Override
  public FontMetrics getFontMetrics(final Font font) {
    mHistory.append("getFontMetrics(Font)#");
    return null;
  }

  @Override
  public boolean hitClip(final int x, final int y, final int width, final int height) {
    mHistory.append("hitClip(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
    return false;
  }

  @Override
  public void setClip(final int x, final int y, final int width, final int height) {
    mHistory.append("setClip(")
      .append(x)
      .append(',')
      .append(y)
      .append(',')
      .append(width)
      .append(',')
      .append(height)
      .append(")#");
  }
  
  @Override
  public void setClip(final Shape shape) {
    if (shape == null) {
      mHistory.append("setClip(null)#");
    } else {
      final Rectangle2D bounds = shape.getBounds2D();
      mHistory.append("setClip(c=").append(bounds.getCenterX()).append(',').append(bounds.getCenterY()).append(")#");
    }
  }

  @Override
  public void setPaintMode() {
    mHistory.append("setPaintMode()#");
  }

  @Override
  public void setXORMode(final Color color) {
    mHistory.append("setXORMode(")
      .append(color)
      .append(")#");
  }

  @Override
  public void translate(final int x, final int y) {
    mHistory.append("translate(")
      .append(x)
      .append(',')
      .append(y)
      .append(")#");
  }

  @Override
  public void translate(final double tx, final double ty) {
  }

  @Override
  public void rotate(final double theta) {
  }

  @Override
  public void rotate(final double theta, final double x, final double y) {
  }

  @Override
  public void scale(final double sx, final double sy) {
  }

  @Override
  public void shear(final double shx, final double shy) {
  }

  @Override
  public void transform(final AffineTransform tx) {
  }

  @Override
  public void setTransform(final AffineTransform tx) {
  }

  @Override
  public AffineTransform getTransform() {
    return null;
  }

  @Override
  public Paint getPaint() {
    return null;
  }

  @Override
  public Composite getComposite() {
    return null;
  }

  @Override
  public void setBackground(final Color color) {

  }

  @Override
  public Color getBackground() {
    return null;
  }

  @Override
  public Stroke getStroke() {
    return null;
  }

  @Override
  public void clip(final Shape s) {

  }

  @Override
  public FontRenderContext getFontRenderContext() {
    return null;
  }

  @Override
  public String toString() {
    return mHistory.toString();
  }

}

