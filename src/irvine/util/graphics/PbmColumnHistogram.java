package irvine.util.graphics;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Produce a histogram of the number of black pixels per column in a PBM image.
 * @author Sean A. Irvine
 */
public final class PbmColumnHistogram {

  private PbmColumnHistogram() { }

  static void checkMagic(final InputStream is) throws IOException {
    if (is.read() != 'P' || is.read() != '4' || is.read() != '\n') {
      throw new IOException("Expected P4 PBM file");
    }
  }

  static int readInt(final InputStream is) throws IOException {
    int v = 0;
    while (true) {
      final int c = is.read();
      if (c == ' ' || c == '\n') {
        return v;
      }
      if (c == -1 || c < '0' || c > '9') {
        throw new IOException("Malformed header in PBM file");
      }
      v *= 10;
      v += c - '0';
    }
  }

  /**
   * Construct a column histogram from a PBM stream.
   * @param is input stream
   * @return column histogram.
   * @throws IOException if an I/O error occurs.
   */
  public static int[] histogram(final InputStream is) throws IOException {
    checkMagic(is);
    final int w = readInt(is);
    final int h = readInt(is);
    final int[] cnt = new int[w];
    int pixels = 0;
    for (int row = 0; row < h; ++row) {
      for (int col = 0; col < w; ++col) {
        pixels = (col & 7) == 0 ? is.read() : pixels << 1;
        if ((pixels & 128) != 0) {
          cnt[col]++;
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
