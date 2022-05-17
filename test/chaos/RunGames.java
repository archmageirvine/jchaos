package chaos;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import irvine.math.r.DoubleUtils;

/**
 * Run a series of games recording whether or not spells were useful.
 *
 * @author Sean A. Irvine
 */
public final class RunGames {

  private RunGames() { }

  private static final String CAST_LINE = "CastWizard: ";
  private static final int CAST_LINE_LENGTH = CAST_LINE.length();

  private static class Balance {
    int mPos = 0;
    int mNeg = 0;

    void increment(final boolean side) {
      if (side) {
        ++mPos;
      } else {
        ++mNeg;
      }
    }

    double utility() {
      return (mPos + 0.5) / (mPos + mNeg + 1);
    }

    @Override
    public String toString() {
      return mPos + " " + mNeg + " " + DoubleUtils.NF4.format(utility());
    }
  }

  private static void write(final HashMap<String, Balance> map, final String name) throws IOException {
    try (final PrintStream w = new PrintStream(new FileOutputStream(name))) {
      for (final Map.Entry<String, Balance> e : map.entrySet()) {
        w.println(e.getKey() + " " + e.getValue());
      }
    }
  }

  private static void update(final HashMap<String, Balance> map, final String s, final boolean side) {
    final Balance b = map.computeIfAbsent(s, k -> new Balance());
    b.increment(side);
  }

  private static void load(final HashMap<String, Balance> map, final String name) throws IOException {
    try (final BufferedReader r = new BufferedReader(new FileReader(name))) {
      String line;
      while ((line = r.readLine()) != null) {
        final String[] p = line.split(" ");
        final Balance b = new Balance();
        b.mPos = Integer.parseInt(p[1]);
        b.mNeg = Integer.parseInt(p[2]);
        map.put(p[0], b);
      }
    }
  }

  /**
   * Run a series of AI games with the aim of computing the best spells.
   * The first agument is total number of games to run.  Second argument
   * is how often to dump complete spell table to a file.
   *
   * @param args see description above
   * @exception Exception if an error occurs
   */
  public static void main(final String... args) throws Exception {
    final int games = Integer.parseInt(args[0]);
    final int dumpFrequency = Integer.parseInt(args[1]);
    final HashMap<String, Balance> map = new HashMap<>();
    if (args.length == 3) {
      load(map, args[2]);
    }
    final int wizards = 1;
    final int generators = 10;
    final PrintStream oldOut = System.out;
    try {
      for (int k = 0; k < games; ++k) {
        oldOut.println("Playing game " + k);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
          try (final PrintStream ps = new PrintStream(bos)) {
            System.setOut(ps);
            Chaos.main("-s", "-v", "-t", "120", "--Xwizards", String.valueOf(wizards), "--Xgenerators", String.valueOf(generators), "--Xleech");
          } catch (final RuntimeException e) {
            write(map, "dumpStateAtCrash.log");
            throw e;
          }
        } finally {
          bos.close();
        }
        final String[] lines = bos.toString().split("\n");
        final boolean[] alive = new boolean[wizards + 1];
        for (int j = lines.length - 1; j >= 0; --j) {
          final String s = lines[j];
          if (s.startsWith(CAST_LINE)) {
            final int player = Integer.parseInt(s.substring(CAST_LINE_LENGTH, s.indexOf(' ', CAST_LINE_LENGTH + 1)));
            if (player >= 0 && player < alive.length) {
              update(map, s.substring(s.lastIndexOf(' ') + 1), alive[player]);
            }
          } else if (s.contains(" ACTIVE ")) {
            final String t = s.trim();
            final int player = Integer.parseInt(t.substring(0, t.indexOf(' ')));
            if (player >= 0) {
              alive[player] = true;
            }
          } else if (s.contains("failed")) {
            oldOut.println(s);
          }
        }
        final String[] keys = map.keySet().toArray(new String[0]);
        Arrays.sort(keys, (a, b) -> {
          if (a.equals(b)) {
            return 0;
          }
          final double sa = map.get(a).utility();
          final double sb = map.get(b).utility();
          return Double.compare(sa, sb);
        });
        final int lim = Math.min(30, map.size());
        for (int j = 0, i = keys.length - lim; j < lim; ++j, ++i) {
          final StringBuilder sb = new StringBuilder();
          sb.append(keys[j]).append(' ').append(DoubleUtils.NF2.format(map.get(keys[j]).utility()));
          while (sb.length() < 55) {
            sb.append(' ');
          }
          sb.append(keys[i]).append(' ').append(DoubleUtils.NF2.format(map.get(keys[i]).utility()));
          oldOut.println(sb.toString());
        }
        if (k % dumpFrequency == 0) {
          write(map, "dump" + k + ".log");
        }
      }
      write(map, "dumpFinal.log");
    } finally {
      System.setOut(oldOut);
    }
  }
}
