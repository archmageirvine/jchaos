package irvine.util.graphics;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Produce a histogram of the number of black pixels per row in a PBM image.
 * @author Sean A. Irvine
 */
public final class PbmRowHistogram {

  private PbmRowHistogram() {
  }

  /**
   * Construct a row histogram from a PBM stream.
   * @param is input stream
   * @return column histogram.
   * @throws IOException if an I/O error occurs.
   */
  public static int[] histogram(final InputStream is) throws IOException {
    PbmColumnHistogram.checkMagic(is);
    final int w = PbmColumnHistogram.readInt(is);
    final int h = PbmColumnHistogram.readInt(is);
    final int[] cnt = new int[h];
    int pixels = 0;
    for (int row = 0; row < h; ++row) {
      for (int col = 0; col < w; ++col) {
        pixels = (col & 7) == 0 ? is.read() : pixels << 1;
        if ((pixels & 128) != 0) {
          cnt[row]++;
        }
      }
    }
    return cnt;
  }

  /**
   * Produce a histogram for the supplied image.
   * @param args filename
   * @throws IOException if an I/O error occurs.
   */
  public static void main(final String[] args) throws IOException {
    try (final BufferedInputStream is = new BufferedInputStream(new FileInputStream(args[0]))) {
      final int[] cnt = histogram(is);
      for (int k = 0; k < cnt.length; ++k) {
        System.out.println(k + " " + cnt[k]);
      }
    }
  }

}
