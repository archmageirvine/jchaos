package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import irvine.math.r.Constants;
import irvine.util.graphics.Lightning;
import irvine.util.graphics.Thunderbolt;
import irvine.util.graphics.WavyLine;

/**
 * Run through a cycle of displaying graphical effects at various angles and distances.
 * @author Sean A. Irvine
 */
public final class DisplayEffect {

  private DisplayEffect() { }

  private static final int WIDTH = 400;
  private static final int HEIGHT = 400;

  /**
   * Test a graphical effect.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("DisplayEffect");
    SwingUtilities.invokeLater(() -> {
      f.setSize(WIDTH, HEIGHT);
      f.setVisible(true);
      final Graphics2D g = (Graphics2D) f.getGraphics();
      final int ox = WIDTH / 2;
      final int oy = HEIGHT / 2;
      final double inc = Constants.TAU / 20;
      final int r = WIDTH / 2 - 20;
      for (double theta = 0; theta < Constants.TAU; theta += inc) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        final int x = (int) (ox + r * Math.cos(theta));
        final int y = (int) (oy + r * Math.sin(theta));
        switch (args[0]) {
          case "wavyline":
            WavyLine.draw(g, ox, oy, x, y, 500, Color.RED);
            break;
          case "thunderbolt":
            Thunderbolt.draw(null, g, ox, oy, x, y, 500);
            break;
          default:
            Lightning.draw(g, ox, oy, x, y, 500);
            break;
        }
      }
      f.dispose();
    });
  }
}
