package irvine.world;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Set;

/**
 * Generate example cell selections.
 * @author Sean A. Irvine
 */
public final class GenerateExampleTeX {

  private GenerateExampleTeX() {
  }

  private static final String LS = System.lineSeparator();

  private static final String HEADER = "\\begin{center}" + LS
    + "\\tabcolsep=1.5mm" + LS
    + "\\def\\pp{\\phantom{$\\bullet$}}" + LS
    + "\\begin{tabular}{|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|c|}" + LS
    + "\\hline";

  private static final String FOOTER = "\\end{tabular}" + LS + "\\end{center}";


  private static void writeSelection(final World<Integer> w, final Set<Integer> s, final int o, final PrintStream p) {
    p.println(HEADER);
    final int c = w.width() - 1;
    for (int y = 0; y < w.height(); ++y) {
      for (int x = 0; x <= c; ++x) {
        final int n = w.getCellNumber(x, y);
        if (n == o) {
          p.print("$\\circ$");
        } else {
          p.print(s.contains(n) ? "$\\bullet$" : "\\pp");
        }
        p.print(x == c ? "\\\\" : "&");
      }
      p.println();
      p.println("\\hline");
    }
    p.println(FOOTER);
  }

  /**
   * Generate some examples for documentation.
   * @param args ignored
   * @throws Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    try (final PrintStream ps = new PrintStream(new FileOutputStream("gc1.tex"))) {
      final World<Integer> fw = new FlatWorld<>(25, 25);
      for (int i = 0; i < fw.size(); ++i) {
        fw.setCell(i, i);
      }
      writeSelection(fw, fw.getCells(262, 0, 7, null), 262, ps);
    }
    try (final PrintStream ps = new PrintStream(new FileOutputStream("gc2.tex"))) {
      final World<Integer> fw = new FlatWorld<>(25, 25);
      for (int i = 0; i < fw.size(); ++i) {
        fw.setCell(i, i);
      }
      writeSelection(fw, fw.getCells(269, 1, 11, null), 269, ps);
    }
    try (final PrintStream ps = new PrintStream(new FileOutputStream("gc3.tex"))) {
      final World<Integer> fw = new FlatWorld<>(25, 25);
      for (int i = 0; i < fw.size(); ++i) {
        fw.setCell(i, i);
      }
      writeSelection(fw, fw.getCells(262, 4, 7, null), 262, ps);
    }
    try (final PrintStream ps = new PrintStream(new FileOutputStream("gc4.tex"))) {
      final World<Integer> fw = new ToroidalWorld<>(25, 25);
      for (int i = 0; i < fw.size(); ++i) {
        fw.setCell(i, i);
      }
      writeSelection(fw, fw.getCells(244, 4, 11, c -> (c & 1) == 0), 244, ps);
    }
  }
}
