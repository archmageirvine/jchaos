package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class RockTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Rock();
  }

  public void testAgainstSource() {
    AbstractMonsterTest.checkAgainstSource(getActor());
  }

  public void test() {
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS | Castable.CAST_DEAD, getCastable().getCastFlags());
    assertEquals(7, getCastable().getCastRange());
  }
}
