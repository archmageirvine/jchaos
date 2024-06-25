package irvine.heraldry;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;

/**
 * Generate a Display image of a shield.
 * @author Sean A. Irvine
 */
public final class DisplayShield {

  private DisplayShield() {
  }

  /**
   * Generate specified shield.
   * @param args shield specification
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("Shield");
    final Shape[] ss = {new Shield(), new Lozenge(), new Cartouche()};
    if (args.length != 0) {
      final Random r = new Random();
      SwingUtilities.invokeLater(() -> {
        f.setSize(330, 460);
        f.setVisible(true);
        for (int k = 0; k < 200; ++k) {
          for (final Shape s : ss) {
            renderShape(f, r, s);
            Sleep.sleep(1000);
          }
        }
        f.dispose();
      });
    } else {
      SwingUtilities.invokeLater(() -> {
        f.setSize(330, 460);
        f.setVisible(true);
        for (int k = 0; k < 2; ++k) {
          for (final Shape s : ss) {
            s.setFieldTincture(Tincture.MURREY);
            s.setOrdinary(Ordinary.ANNULET);
            s.setOrdinaryTincture(Tincture.GULES);
            render(f.getGraphics(), s);
            Sleep.sleep(2000);
          }
        }
        f.dispose();
      });
    }
  }

  static void renderShape(final JFrame f, final Random r, final Shape s) {
    s.setFieldTincture(Tincture.values()[r.nextInt(Tincture.values().length)]);
    s.setOrdinary(Ordinary.values()[r.nextInt(Ordinary.values().length)]);
    s.setOrdinaryTincture(Tincture.values()[r.nextInt(Tincture.values().length)]);
    render(f.getGraphics(), s);
  }

  static void render(final Graphics g, final Shape s) {
    if (g != null) {
      g.setColor(Color.BLUE);
      g.fillRect(0, 0, 330, 460);
      s.render(g, 300, 20, 30);
      s.render(g, 32, 5, 400);
      g.dispose();
    }
  }
}
