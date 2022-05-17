package irvine.heraldry;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

import irvine.util.string.PostScript;

/**
 * Generate a PostScript image of a shield for documentation.
 *
 * @author Sean A. Irvine
 */
public final class GenerateHeraldry {

  private GenerateHeraldry() { }

  private static final int ACROSS = 6;

  private static void generate(final Shape shape, final String prefix) throws IOException {
      System.out.print("\\begin{tabular}{");
      for (int k = 0; k < ACROSS; ++k) {
        System.out.print('c');
      }
      System.out.println('}');
      int j = 0;
      final StringBuilder comment = new StringBuilder();
      for (final Ordinary o : Ordinary.values()) {
        final String lc = o.toString().toLowerCase(Locale.getDefault());
        comment.append(lc.replace('_', ' '));
        System.out.print("\\epsfxsize=" + (1.0 / (ACROSS + 1)) + "\\textwidth\\epsffile{" + prefix + "_" + lc + ".eps}");
        if (++j == ACROSS) {
          System.out.println("\\\\");
          System.out.println(comment + "\\\\");
          comment.setLength(0);
          j = 0;
        } else {
          comment.append('&');
          System.out.print('&');
        }
        try (final PrintStream out = new PrintStream(new FileOutputStream(prefix + "_" + lc + ".eps"))) {
          shape.setOrdinary(o);
          shape.setOrdinaryTincture(Tincture.SABLE);
          shape.setFieldTincture(Tincture.ARGENT);
          PostScriptShield.writeHeader(out);
          shape.renderPostScript(out);
          PostScript.trailer(out);
        }
      }
      if (j != 0) {
        System.out.println("\\\\");
        System.out.println(comment);
      }
      System.out.println("\\end{tabular}");
  }

  /**
   * Used to generate shields used in documentation.
   *
   * @param args shield number
   * @exception Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    final Shape s;
    switch (Integer.parseInt(args[0])) {
    case 1:
      s = new Lozenge();
      break;
    case 2:
      s = new Cartouche();
      break;
    case 3:
      // shield ordinaries
      generate(new Shield(), "ordinary");
      return;
    case 4:
      // cartouche ordinaries
      generate(new Cartouche(), "cartouche");
      return;
    case 5:
      // lozenge ordinaries
      generate(new Lozenge(), "lozenge");
      return;
    default: // 0
      s = new Shield();
      break;
    }
    PostScriptShield.writeHeader(System.out);
    s.renderPostScript(System.out);
    PostScript.trailer(System.out);
  }
}
