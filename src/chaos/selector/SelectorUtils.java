package chaos.selector;

import java.util.Arrays;

import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.util.RankingComparator;
import chaos.util.RankingTable;

/**
 * Utility functions to assist during spell selection.
 * @author Sean A. Irvine
 */
final class SelectorUtils {

  private SelectorUtils() {
  }

  static final Castable[] NO_SELECTION = new Castable[2];

  static Castable[] best(final Castable[] castables, final int count) {
    Arrays.sort(castables, RankingComparator.REVERSE_COMPARATOR);
    final Castable[] res = new Castable[count];
    System.arraycopy(castables, 0, res, 0, count);
    return res;
  }

  static Castable worst(final Castable[] castables, final Castable exclude) {
    Castable worst = null;
    int worstsc = Integer.MAX_VALUE;
    for (final Castable c : castables) {
      if (c != exclude) {
        final int sc = RankingTable.getRanking(c);
        if (sc < worstsc) {
          worstsc = sc;
          worst = c;
        }
      }
    }
    return worst;
  }

  static boolean noSelectionPossible(final Castable[] castables, final Caster caster) {
    return castables == null || castables.length == 0 || caster == null || caster.getState() != State.ACTIVE;
  }


}
