package chaos.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;

/**
 * Text in a tile.
 * @author Sean A. Irvine
 */
class TextSpellTile extends BasicSpellTile {

  private final String mImageText;
  private final int mImageTextColor;
  private final int mImageTextShadowColor;
  private final String mWordText;
  private final int mWordColor;

  TextSpellTile(final int widthBits, final int bgColor, final String imageText, final int imageTextColor, final int imageTextShadowColor, final String wordText, final int wordColor) {
    super(widthBits, bgColor);
    mImageText = imageText;
    mImageTextColor = imageTextColor;
    mImageTextShadowColor = imageTextShadowColor;
    mWordText = wordText;
    mWordColor = wordColor;
  }

  private double textWidth(final String s, final Graphics g) {
    final Rectangle2D r = g.getFontMetrics().getStringBounds(s, g);
    return Math.max(r.getWidth(), r.getHeight());
  }

  private void setFont(final Graphics g, final int size) {
    g.setFont(new Font("TimesRoman", Font.PLAIN, size));
  }

  private int selectFont(final String s, final Graphics g) {
    final double targetMaxTextSize = mWidth - (mWidthBits > 4 ? 8 : 3);
    int minPt = 5;
    setFont(g, minPt);
    if (textWidth(s, g) > targetMaxTextSize) {
      return 0;
    }
    int maxPt = mWidth;
    setFont(g, maxPt);
    // binary search
    while (minPt <= maxPt) {
      final int mid = (minPt + maxPt) / 2;
      setFont(g, mid);
      final double w = textWidth(s, g);
      //System.err.println("[" + minPt + "," + maxPt + "] current=" + mid + " cf. " + targetMaxTextSize + " :: " + w);
      if (w > targetMaxTextSize) {
        maxPt = mid - 1;
      } else if (w < targetMaxTextSize) {
        minPt = mid + 1;
      } else {
        return mid;
      }
    }
    return minPt;
  }

  private void drawImageText(final Graphics g) {
    final int size = selectFont(mImageText, g);
    if (size > 0) {
      final Rectangle2D r = g.getFontMetrics().getStringBounds(mImageText, g);
      final double w = r.getWidth();
      final double h = r.getHeight();
      final double d = (mWidth - w) / 2;
      // This y-offset still might not be perfect
      final double y;
      if (mWordText == null || PixelFont.width(mWordText) > mWidth - 2) {
        y = mWidth + mWidthBits - h + 1;
      } else {
        y = mWidthBits < 5 ? mWidth - h : mWidth + 3 * mWidthBits - h;
      }
      final int dx = (int) Math.round(d);
      final int dy = (int) Math.round(mWidth - y / 2);
      g.setColor(new Color(mImageTextShadowColor));
      g.drawString(mImageText, dx - 1, dy - 1);
      g.setColor(new Color(mImageTextColor));
      g.drawString(mImageText, dx, dy);
    } else {
      throw new RuntimeException();
    }
  }

  private void drawWordText(final BufferedImage image) {
    if (mWordText != null) {
      final int w = PixelFont.width(mWordText);
      if (w <= mWidth - 2) {
        final int x = (mWidth - w) / 2;
        final int y = mWidth - 2 * (mWidthBits - 1);
        PixelFont.drawString(image, mWordColor, mWordText, x, y);
      }
    }
  }

  @Override
  BufferedImage image() {
    final BufferedImage image = super.image();
    final Graphics g = image.getGraphics();
    try {
      drawImageText(g);
    } finally {
      g.dispose();
    }
    drawWordText(image);
    return image;
  }

  /**
   * Used for testing.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("Shield");
    SwingUtilities.invokeLater(() -> {
      f.setSize(530, 560);
      f.setVisible(true);
      for (int k = 0; k < 200; ++k) {
        final Graphics g = f.getGraphics();
        if (g != null) {
          final TextSpellTile tile16 = new TextSpellTile(4, 0xFF636363, "M", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile16.image(), 50, 100, null);
          final TextSpellTile tile32 = new TextSpellTile(5, 0xFF636363, "M", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile32.image(), 100, 100, null);
          final TextSpellTile tile64 = new TextSpellTile(6, 0xFF636363, "M", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile64.image(), 150, 100, null);
          final TextSpellTile tile64b = new TextSpellTile(6, 0xFF636363, "M", 0xFFDD9800, 0xFFFFEE00, null, 0xFFFF0000);
          g.drawImage(tile64b.image(), 250, 100, null);
        }
        if (g != null) {
          final TextSpellTile tile16 = new TextSpellTile(4, 0xFF636363, "II", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile16.image(), 50, 200, null);
          final TextSpellTile tile32 = new TextSpellTile(5, 0xFF636363, "II", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile32.image(), 100, 200, null);
          final TextSpellTile tile64 = new TextSpellTile(6, 0xFF636363, "II", 0xFFDD9800, 0xFFFFEE00, "MERGE", 0xFFFF0000);
          g.drawImage(tile64.image(), 150, 200, null);
        }
        Sleep.sleep(1000);
      }
      f.dispose();
    });
  }
}

