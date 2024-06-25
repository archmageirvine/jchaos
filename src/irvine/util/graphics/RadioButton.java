package irvine.util.graphics;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Very light weight radio button.
 * @author Sean A. Irvine
 */
public class RadioButton extends AbstractButton {

  private final int mRadius;
  private boolean mState;
  private final RadioButton[] mSiblings;

  /**
   * Construct a new button with the specified image and position.
   * @param x screen x-coordinate
   * @param y screen y-coordinate
   * @param radius width of button
   * @param state initial state
   * @param siblings sibling buttons (i.e. only one of these siblings should be set at a time)
   * @throws NullPointerException if <code>image</code> is null.
   */
  public RadioButton(final int x, final int y, final int radius, final boolean state, final RadioButton[] siblings) {
    super(x, y, 2 * radius, 2 * radius);
    mState = state;
    mRadius = radius;
    mSiblings = siblings;
  }

  @Override
  public void paint(final Graphics g) {
    if (g != null) {
      final int x = getX();
      final int y = getY();
      final int h = mRadius - 1;
      // Draw empty radio buttons
      g.setColor(Color.BLACK);
      g.fillOval(x, y + h - mRadius / 2, mRadius, mRadius);
      g.setColor(Color.WHITE);
      g.drawOval(x, y + h - mRadius / 2, mRadius, mRadius);
      if (mState) {
        // Fill selected button
        g.setColor(Color.RED);
        g.fillOval(x + 1, y + mRadius - h / 2, h - 1, h - 1);
      }
    }
  }

  /**
   * Placeholder method to perform an action when this button
   * is activated.
   */
  @Override
  public void act() {
    if (!mState) {
      for (final RadioButton b : mSiblings) {
        mState = true;
        if (b != null && b != this) {
          b.mState = false;
        }
      }
    }
  }

  /**
   * Test if this button is activated.
   * @return true iff button is activated
   */
  public boolean isSet() {
    return mState;
  }
}
