package irvine.util.graphics;

import java.awt.Graphics;

/**
 * Very light weight button.
 * @author Sean A. Irvine
 */
public abstract class AbstractButton {

  private final int mX0;
  private final int mY0;
  private final int mX1;
  private final int mY1;

  /**
   * Construct a new button with the specified target area.
   * @param x screen x-coordinate
   * @param y screen y-coordinate
   * @param w width of button
   * @param h height of button
   * @throws NullPointerException if <code>image</code> is null.
   */
  public AbstractButton(final int x, final int y, final int w, final int h) {
    mX0 = x;
    mY0 = y;
    mX1 = x + w;
    mY1 = y + h;
  }

  /**
   * Test if this button contains the given point.
   * @param x x-coordinate
   * @param y y-coordinate
   * @return true if (x,y) is within button
   */
  public boolean contains(final int x, final int y) {
    return x >= mX0 && x < mX1 && y >= mY0 && y < mY1;
  }

  /**
   * Draw this button.
   * @param g place to draw
   */
  public abstract void paint(final Graphics g);

  /**
   * Called whenever the button is activated.
   */
  public abstract void act();

  public int getX() {
    return mX0;
  }

  public int getY() {
    return mY0;
  }

  public int getWidth() {
    return mX1 - mX0;
  }

  public int getHeight() {
    return mY1 - mY0;
  }
}
