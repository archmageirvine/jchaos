package irvine.util.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Very light weight button with a image.
 *
 * @author Sean A. Irvine
 */
public class Button extends AbstractButton {

  private BufferedImage mImage;
  /**
   * Construct a new button with the specified image and position.
   *
   * @param image the image
   * @param x screen x-coordinate
   * @param y screen y-coordinate
   * @exception NullPointerException if <code>image</code> is null.
   */
  public Button(final BufferedImage image, final int x, final int y) {
    super(x, y, image.getWidth(), image.getHeight());
    mImage = image;
  }

  @Override
  public void paint(final Graphics g) {
    if (g != null) {
      g.drawImage(mImage, getX(), getY(), null);
    }
  }

  /**
   * Placeholder method to perform an action when this button
   * is activated.
   */
  @Override
  public void act() {
  }
}
