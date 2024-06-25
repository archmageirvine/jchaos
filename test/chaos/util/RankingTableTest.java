package chaos.util;

import chaos.common.growth.Fire;
import chaos.common.monster.GoblinBomb;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;


/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class RankingTableTest extends TestCase {

  public void testMax() {
    assertTrue(RankingTable.getMaximumRanking() > 56);
  }

  public void testEntriesAndRankingLogic() {
    assertEquals(-1, RankingTable.getRanking(null));
    assertEquals(-1, RankingTable.getRanking(new Wizard1()));
    assertTrue(RankingTable.getRanking(new Fire()) < RankingTable.getRanking(new GoblinBomb()));
  }

}
