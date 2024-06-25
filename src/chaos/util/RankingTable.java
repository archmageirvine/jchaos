package chaos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import chaos.common.Castable;
import irvine.util.io.IOUtils;


/**
 * Ranking of all spells which can appear on a CastableList.  Used
 * by the AI has part of the spell selection process.
 * @author Sean A. Irvine
 */
public final class RankingTable {

  private RankingTable() {
  }

  /** Maps class names to ranking number. */
  private static final HashMap<Class<? extends Castable>, Integer> RANKING = new HashMap<>();
  /** Largest rank allocated. */
  private static final int MAX_RANK;

  static {
    try {
      try (final BufferedReader is = IOUtils.reader("chaos/resources/ranking.txt")) {
        String line;
        int rank = 0;
        while ((line = is.readLine()) != null) {
          if (!line.isEmpty() && line.charAt(0) != '#') {
            final StringTokenizer st = new StringTokenizer(line, ",");
            final Integer r = ++rank;
            while (st.hasMoreTokens()) {
              final Class<? extends Castable> clazz;
              final String c = st.nextToken();
              try {
                clazz = Class.forName(c).asSubclass(Castable.class);
              } catch (final Exception e) {
                throw new RuntimeException("Could not find or loadModel " + c, e);
              }
              RANKING.put(clazz, r);
            }
          }
        }
        MAX_RANK = rank;
        assert MAX_RANK != 0;
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return the ranking for the specified castable.  If no such
   * ranking is available then -1 is returned.
   * @param c castable to get ranking for
   * @return ranking
   */
  public static int getRanking(final Castable c) {
    try {
      return c == null ? -1 : RANKING.get(c.getClass());
    } catch (final NullPointerException e) {
      return -1;
    }
  }

  /**
   * Return the maximum rank of any castable.
   * @return maximum rank
   */
  public static int getMaximumRanking() {
    return MAX_RANK;
  }

}
