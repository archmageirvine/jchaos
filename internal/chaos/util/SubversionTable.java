package chaos.util;

import java.util.ArrayList;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.common.ReincarnationTree;
import chaos.common.wizard.Wizard;
import irvine.world.FlatWorld;

/**
 * Table of subversion scores used by the AI.
 *
 * @author Sean A. Irvine
 */
public final class SubversionTable {

  private SubversionTable() { }

  private static double prob(final int v) {
    return (120 - v) / 120.0;
  }

  private static int max(final ArrayList<Integer> a) {
    int m = 0;
    for (final int v : a) {
      if (v > m) {
        m = v;
      }
    }
    return m;
  }

  private static String epsName(final Castable c) {
    final String name = c.getName();
    final StringBuilder f = new StringBuilder();
    for (int k = 0; k < name.length(); ++k) {
      final char cc = name.charAt(k);
      if (cc != ' ' && cc != '-') {
        f.append(cc);
      }
    }
    return f.toString();
  }

  private static boolean insertCell(final FlatWorld<Integer> pic, final int x, final int y, final int v, final boolean retry) {
    if (x < 0 || y < 0 || x >= pic.width() || y >= pic.height()) {
      return false;
    }
    //    System.err.println("ic x=" + x + " y=" + y + " v=" + v);
    final int cn = pic.getCellNumber(x, y);
    if (pic.getCell(cn) == null) {
      pic.setCell(cn, v);
      return true;
    }
    if (!retry) {
      return false;
    }
    return insertCell(pic, x, y + 1, v, false)
      || insertCell(pic, x, y - 1, v, false)
      || insertCell(pic, x + 1, y, v, false)
      || insertCell(pic, x - 1, y, v, false)
      || insertCell(pic, x - 1, y - 1, v, false)
      || insertCell(pic, x + 1, y - 1, v, false)
      || insertCell(pic, x - 1, y + 1, v, false)
      || insertCell(pic, x + 1, y + 1, v, false)
      || insertCell(pic, x, y + 2, v, false)
      || insertCell(pic, x, y - 2, v, false)
      || insertCell(pic, x + 2, y, v, false)
      || insertCell(pic, x - 2, y, v, false)
      || insertCell(pic, x - 2, y - 1, v, false)
      || insertCell(pic, x + 2, y - 1, v, false)
      || insertCell(pic, x - 2, y + 1, v, false)
      || insertCell(pic, x + 2, y + 1, v, false)
      || insertCell(pic, x - 1, y - 2, v, false)
      || insertCell(pic, x + 1, y - 2, v, false)
      || insertCell(pic, x - 1, y + 2, v, false)
      || insertCell(pic, x + 1, y + 2, v, false)
      ;
  }

  private static String picture(final int width, final int height, final ArrayList<Integer> x, final ArrayList<Integer> y, final ArrayList<Castable> icon, final double mmUnit) {
    final StringBuilder sb = new StringBuilder();
    final FlatWorld<Integer> pic = new FlatWorld<>(width + 1, height + 1);
    final double xs = width / (double) max(x);
    final double ys = height / (double) max(y);

    for (int k = 0; k < x.size(); ++k) {
      if (!insertCell(pic, (int) (xs * x.get(k)), (int) (ys * y.get(k)), k, true)) {
        System.err.println("Failed to insert: " + icon.get(k).getName());
      }
    }

    sb.append("\\def\\ufile#1{\\epsfxsize=")
      .append(mmUnit)
      .append("mm\\epsffile{cicons/#1.eps}}\n\\setlength{\\unitlength}{")
      .append(mmUnit)
      .append("mm}\n\\begin{picture}(")
      .append(width + 1)
      .append(',')
      .append(height + 1)
      .append(")\n")
      .append("\\put(-0.3,-0.3){\\vector(1,0){")
      .append(width + 1.3)
      .append("}}\n\\put(-0.3,-0.3){\\vector(0,1){")
      .append(height + 1.3)
      .append("}}\n");

    for (int h = 0; h <= height; ++h) {
      for (int w = 0; w <= width; ++w) {
        final Integer i = pic.getCell(w, h);
        if (i != null) {
          sb.append("\\put(")
            .append(w)
            .append(',')
            .append(h)
            .append("){\\ufile{")
            .append(epsName(icon.get(i)))
            .append("}}\n");
        }
      }
    }

    sb.append("\\end{picture}");

    return sb.toString();
  }


  /**
   * Construct the table.
   * @param args ignored
   */
  public static void main(final String[] args) {

    final Cell cell = new Cell(0);
    final ArrayList<Integer> x = new ArrayList<>();
    final ArrayList<Integer> y = new ArrayList<>();
    final ArrayList<Castable> v = new ArrayList<>();
    for (final Class<? extends Castable> clazz : ReincarnationTree.CASTABLES) {
      final Castable c = FrequencyTable.instantiate(clazz);
      if (c instanceof Monster && !(c instanceof Wizard)) {
        final Monster m = (Monster) c;
        // average score
        int s = 50;
        for (int k = 0; k < 100; ++k) {
          s += CastUtils.subversionScore(m);
        }
        cell.push(m);
        x.add(s / 100);
        y.add((int) (1000 * prob(m.get(Attribute.INTELLIGENCE))) - 90);
        v.add(m);
        cell.pop();
      }
    }
    System.out.println(picture(30, 25, x, y, v, 3.8));
  }
}
