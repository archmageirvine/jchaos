package chaos.selector;

import java.util.Comparator;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.wizard.Wizard;
import chaos.util.RankingComparator;

/**
 * Selector that chooses lowest ranking spell that can be cast.
 * @author Sean A. Irvine
 */
public class ContraryRanker extends Ranker {

  /**
   * Construct a selector.
   * @param wizard the wizard
   * @param world the world
   * @param castMaster casting rules
   */
  public ContraryRanker(final Wizard wizard, final World world, final CastMaster castMaster) {
    super(wizard, world, castMaster);
  }

  @Override
  protected Comparator<Castable> comparator() {
    return RankingComparator.FORWARD_COMPARATOR;
  }

}
