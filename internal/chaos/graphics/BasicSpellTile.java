package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * An empty tile with consistent borders.
 * @author Sean A. Irvine
 */
class BasicSpellTile {

  protected final int mWidthBits;
  protected final int mWidth;
  private final int mBackgroundColor;

  BasicSpellTile(final int widthBits, final int bgColor) {
    mWidthBits = widthBits;
    mWidth = 1 << mWidthBits;
    mBackgroundColor = bgColor;
  }

  BufferedImage image() {
    final BufferedImage image = new BufferedImage(mWidth, mWidth, BufferedImage.TYPE_INT_RGB);
    final Graphics g = image.getGraphics();
    try {
      final Color bg = new Color(mBackgroundColor);
      g.setColor(bg);
      g.fillRect(0, 0, mWidth, mWidth);
      g.setColor(bg.brighter());
      g.drawLine(0, 0, mWidth - 1, 0);
      g.drawLine(0, 0, 0, mWidth - 1);
      if (mWidthBits > 4) {
        g.drawLine(1, 1, mWidth - 2, 1);
        g.drawLine(1, 1, 1, mWidth - 2);
        if (mWidthBits > 5) {
          g.drawLine(2, 2, mWidth - 3, 2);
          g.drawLine(2, 2, 2, mWidth - 3);
        }
      }
      g.setColor(bg.darker());
      g.drawLine(mWidth, mWidth, 1, mWidth);
      g.drawLine(mWidth, mWidth, mWidth, 1);
      if (mWidthBits > 4) {
        g.drawLine(mWidth - 1, mWidth - 1, 2, mWidth - 1);
        g.drawLine(mWidth - 1, mWidth - 1, mWidth - 1, 2);
        if (mWidthBits > 5) {
          g.drawLine(mWidth - 2, mWidth - 2, 3, mWidth - 2);
          g.drawLine(mWidth - 2, mWidth - 2, mWidth - 2, 3);
        }
      }
    } finally {
      g.dispose();
    }
    return image;
  }
}

