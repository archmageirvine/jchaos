package chaos.graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;

import chaos.common.Castable;
import chaos.util.NameUtils;

/**
 * Information displays about a castable.
 * @author Sean A. Irvine
 */
public final class InformationPanel {

  private InformationPanel() {
  }

  private static int stringWidth(final FontMetrics fm, final String s) {
    return fm == null ? 10 : fm.stringWidth(s);
  }

  private static final Image SCROLL = ImageLoader.getImage("chaos/resources/backdrops/scroll.png");
  private static final HashMap<ChaosScreen, Image> SCALED_SCROLL = new HashMap<>();

  private static Image getScrollImage(final ChaosScreen screen) {
    final Image cache = SCALED_SCROLL.get(screen);
    if (cache != null) {
      return cache;
    }
    final int w = screen.getMainWidth();
    final int h = screen.getMainHeight();
    final Image scaled = SCROLL.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    SCALED_SCROLL.put(screen, scaled);
    return scaled;
  }


  /**
   * Display information screens about a castable.
   * @param screen screen to display on
   * @param c castable to display information for
   * @param graphics graphics to draw into
   */
  public static void informationDisplay(final ChaosScreen screen, final Castable c, final Graphics graphics) {
    if (c == null || screen == null) {
      return;
    }
    final Image baseImage = ImageLoader.getImage(NameUtils.getBackdropResource(c));
    // Need to halt the animator for this step, because we are going to
    // be drawing in the main play area.
    final int mw = screen.getMainWidth();
    final int mh = screen.getMainHeight();
    synchronized (graphics) {
      // draw the scroll and spell image
      Render.renderImage(graphics, getScrollImage(screen), screen.getXOffset(), screen.getYOffset());
      final int iy = screen.getYOffset() + mh / 6;
      Render.renderImage(graphics, baseImage, screen.getXOffset() + (mw - Render.getWidthOfImage(baseImage)) / 2, iy);

      // write the spell name
      graphics.setColor(new Color(0x951010));
      graphics.setFont(screen.getTitleFont());
      final FontMetrics fm = graphics.getFontMetrics();
      final String name = NameUtils.getTextName(c);
      graphics.drawString(name, screen.getXOffset() + (mw - stringWidth(fm, name)) / 2, mh / 6);

      // write the description with word wrapping in the current font
      graphics.setColor(Color.BLACK);
      graphics.setFont(screen.getTextFont());
      // reget metrics because we changed the font
      final FontMetrics fm2 = graphics.getFontMetrics();
      final int sp = stringWidth(fm2, " ");
      final String[] tokens = c.getDescription().split("\\s+");
      int x = 0;
      int y = 0;
      final int lineSpacing = mh * 7 / 240;
      final int fuzzyEdge = (int) Math.round(0.644 * mw);
      final int ox = screen.getXOffset() + (int) Math.round(0.166 * mw);
      final int oy = screen.getYOffset() + iy + baseImage.getHeight(null) + 2 * lineSpacing;
      for (final String token : tokens) {
        final int w = stringWidth(fm2, token);
        if (x + w > fuzzyEdge) {
          // move to next line
          x = 0;
          y += lineSpacing;
        }
        graphics.drawString(token, ox + x, oy + y);
        x += w;
        x += sp;
        // add a few more pixels for a sentence break
        if (token.endsWith(".")) {
          x += 3 * sp / 2;
        }
      }
    }
  }
}
