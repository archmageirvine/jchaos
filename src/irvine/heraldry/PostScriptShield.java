package irvine.heraldry;

import java.io.PrintStream;

import irvine.util.string.PostScript;

/**
 * Generate a PostScript image of a shield.
 *
 * @author Sean A. Irvine
 */
public final class PostScriptShield {

  private PostScriptShield() { }

  static void writeHeader(final PrintStream out) {
    PostScript.header(out, PostScriptShield.class, "image", 320, 430);
    out.println(1.0 / 300 + " setlinewidth");
    out.println("10 10 translate");
    out.println("300 300 scale");
  }

  /**
   * Generate specified shield as PostScript.
   *
   * @param args shield specification
   */
  public static void main(final String[] args) {
    final Shape s;
    if (args.length == 0 || "s".equals(args[0])) {
      s = new Shield();
    } else if ("l".equals(args[0])) {
      s = new Lozenge();
    } else {
      s = new Cartouche();
    }
    s.setFieldTincture(Tincture.MURREY);
    s.setOrdinary(Ordinary.ANNULET);
    s.setOrdinaryTincture(Tincture.GULES);
    writeHeader(System.out);
    s.renderPostScript(System.out);
    PostScript.trailer(System.out);
  }
}
