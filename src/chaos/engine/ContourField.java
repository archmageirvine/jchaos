package chaos.engine;

import java.util.HashMap;

/**
 * A field which is 0 at the centre and increases to a maximum in the corners.
 * @author Sean A. Irvine
 */
public final class ContourField implements ScalarField {

  private static final HashMap<String, ContourField> CACHE = new HashMap<>();

  static ContourField createField(final int width, final int height, final double maximum, final double lambda) {
    final String key = width + "_" + height + "_" + maximum + "_" + lambda;
    final ContourField f = CACHE.get(key);
    if (f != null) {
      return f;
    }
    final ContourField r = new ContourField(width, height, maximum, lambda);
    CACHE.put(key, r);
    return r;
  }

  private final double[] mWeight;

  private ContourField(final int width, final int height, final double maximum, final double lambda) {
    mWeight = new double[width * height];
    final double ox = 0.5 * width;
    final double oy = 0.5 * height;
    final double r = Math.sqrt(ox * ox + oy * oy);
    final double invr = 1 / r;
    for (int y = 0, c = 0; y < height; ++y) {
      final double cy = y + 0.5 - oy;
      final double cs = cy * cy;
      for (int x = 0; x < width; ++x, ++c) {
        final double cx = x + 0.5 - ox;
        final double dr = Math.sqrt(cs + cx * cx);
        mWeight[c] = maximum * Math.pow(dr * invr, lambda);
      }
    }
  }

  @Override
  public double weight(final int cell) {
    return mWeight[cell];
  }

  /**
   * Generate a PGM file of a contour field (useful for testing).
   * @param args ignored
   */
  public static void main(final String[] args) {
    final int w = Integer.parseInt(args[0]);
    final int h = Integer.parseInt(args[1]);
    final double l = Double.parseDouble(args[2]);
    final ContourField field = new ContourField(w, h, 1, l);
    System.out.println("P5 " + w + " " + h + " 255");
    for (int y = 0; y < h; ++y) {
      for (int x = 0; x < w; ++x) {
        System.out.write((int) (255 * field.weight(y * w + x)));
      }
    }
    System.out.flush();
  }
}
