package chaos.util;

/**
 * Generate a simple text grid.
 *
 * @author Sean A. Irvine
 */
public final class TextGrid {

  private TextGrid() { }

  /**
   * Draw a text grid.
   * @param args see usage
   */
  public static void main(final String[] args) {
    if (args.length != 3) {
      System.err.println("USAGE: TextGrid x y w");
    } else {
      final int x = Integer.parseInt(args[0]);
      final int y = Integer.parseInt(args[1]);
      final int w = Integer.parseInt(args[2]);

      // construct cell divider line
      StringBuilder s = new StringBuilder();
      for (int i = 0; i < x; ++i) {
        s.append('#');
        for (int j = 0; j < w - 1; ++j) {
          s.append("==");
        }
        s.append('=');
      }
      s.append('#');
      final String divider = s.toString();

      // construct internal divider line
      s = new StringBuilder();
      for (int i = 0; i < x; ++i) {
        s.append('+');
        for (int j = 0; j < w - 1; ++j) {
          s.append("-+");
        }
        s.append('-');
      }
      s.append('+');
      final String idivider = s.toString();

      // construct internal spacer line
      s = new StringBuilder();
      for (int i = 0; i < x; ++i) {
        s.append('"');
        for (int j = 0; j < w - 1; ++j) {
          s.append(" |");
        }
        s.append(' ');
      }
      s.append('"');
      final String ispace = s.toString();

      // print it
      for (int i = 0; i < y; ++i) {
        System.out.println(divider);
        for (int j = 0; j < w; ++j) {
          System.out.println(ispace);
          if (j != w - 1) {
            System.out.println(idivider);
          }
        }
      }
      System.out.println(divider);
    }
  }

}
