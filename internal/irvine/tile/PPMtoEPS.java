package irvine.tile;

/**
 * Convert a PPM image to Encapsulate PostScript for documentation.
 *
 * @author Sean A. Irvine
 */
public final class PPMtoEPS {

  private PPMtoEPS() { }

  /**
   * Convert a PPM image to an Encapsulated PostScript.
   *
   * @param args files to convert
   * @exception Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    for (final String arg : args) {
      final String fn = arg.endsWith(".ppm") ? arg.substring(0, arg.length() - 4) : arg;
      GenerateEPSImages.ppmToEPS(arg, fn + ".eps");
    }
  }

}
