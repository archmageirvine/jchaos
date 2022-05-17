package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.util.RankingTable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class NukedTest extends AbstractActorTest {


  @Override
  public Castable getCastable() {
    return new Nuked();
  }

  @Override
  public void testGetRanking() {
    assertEquals("Bad ranking found", -1, RankingTable.getRanking(mCastable));
    assertEquals(0, getCastable().getCastFlags());
    AbstractMonsterTest.checkAgainstSource(getActor());
    new Nuked().cast(null, null, null, null);
  }
}
