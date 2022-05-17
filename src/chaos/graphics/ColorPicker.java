package chaos.graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class to select distinct colors.
 * @author Sean A. Irvine
 */
public final class ColorPicker {

  private ColorPicker() { }

  /**
   * Choose the specified number of colors
   * @param n number of colors
   * @return list of colors
   */
  public static List<Color> pick(final int n) {
    final Random r = new Random();
    final List<Color> colors = new ArrayList<>();
    if (n > 0) {
      for (float theta = 0; theta < 1; theta += 1.0 / n) {
        colors.add(Color.getHSBColor(theta, 0.9F + r.nextFloat() * 0.1F, 0.5F + r.nextFloat() * 0.1F));
      }
    }
    return colors;
  }
}
