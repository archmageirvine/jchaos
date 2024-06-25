package irvine.util.graphics;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Compute estimate of skew (assumed near 0) in horizontal text image.
 * Makes ugly assumptions like image is large.
 * @author Sean A. Irvine
 */
public final class EstimatePbmSkew {

  private EstimatePbmSkew() {
  }

  private static int getBit(final byte[] image, final int w, final int x, final int y) {
    final int cw = (w | 7) >>> 3;
    final int bit = x & 7;
    final int k = x >>> 3;
    return (image[y * cw + k] >>> (8 - bit)) & 1;
  }

  /**
   * Find best skew angle.
   * @param is input stream
   * @return column histogram.
   * @throws IOException if an I/O error occurs.
   */
  public static double skew(final InputStream is) throws IOException {
    PbmColumnHistogram.checkMagic(is);
    final int w = PbmColumnHistogram.readInt(is);
    final int h = PbmColumnHistogram.readInt(is);
    final byte[] image = new byte[(w | 7) * h];
    for (int row = 0, k = 0; row < h; ++row) {
      for (int col = 0; col < (w | 7) / 8; ++col) {
        final int c = is.read();
        if (c < 0) {
          throw new IOException();
        }
        image[k++] = (byte) c;
      }
    }
    double bestTheta = Double.NaN;
    int bestSum = Integer.MAX_VALUE;
    for (double theta = -5.0; theta < 5.0; theta += 0.25) {
      final double sin = Math.sin(Math.toRadians(theta));
      int min = Integer.MAX_VALUE;
      for (int row = h / 2 - 40; row < h / 2 + 40; ++row) {
        int sum = 0;
        for (int x = 0; x < w; ++x) {
          if (getBit(image, w, x, row + (int) Math.round(x * sin)) > 0) {
            ++sum;
          }
        }
        if (sum < min) {
          min = sum;
        }
      }
      if (min < bestSum) {
        bestSum = min;
        bestTheta = theta;
      }
    }
    return bestTheta;
  }

  /**
   * Compute skew.
   * @param args filename
   * @throws IOException if an I/O error occurs.
   */
  public static void main(final String[] args) throws IOException {
    try (final BufferedInputStream is = new BufferedInputStream(new FileInputStream(args[0]))) {
      final double skew = skew(is);
      System.out.println(Math.abs(skew) <= 0.5 ? 0 : -skew);
    }
  }

}
