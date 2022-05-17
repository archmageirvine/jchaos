package chaos.util;

import java.io.Serializable;
import java.util.Comparator;

import chaos.common.Castable;

/**
 * Comparator for ranking castables.
 *
 * @author Sean A. Irvine
 */
public final class RankingComparator implements Comparator<Castable>, Serializable {

  /** Comparator ranking touch of god higher than a horse. */
  public static final RankingComparator FORWARD_COMPARATOR = new RankingComparator(false);
  /** Comparator ranking a horse higher than touch of god. */
  public static final RankingComparator REVERSE_COMPARATOR = new RankingComparator(true);

  private final boolean mDescending;

  /**
   * Construct a new comparator for castables.
   * @param descending true if the worst spells have the highest value
   */
  private RankingComparator(final boolean descending) {
    mDescending = descending;
  }

  @Override
  public int compare(final Castable a, final Castable b) {
    final int v = RankingTable.getRanking(a) - RankingTable.getRanking(b);
    return mDescending ? -v : v;
  }
}
