package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class WillyTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Willy();
  }

  public void test() {
    AbstractMonsterTest.checkAgainstSource(new Willy());
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS | Castable.CAST_DEAD, new Willy().getCastFlags());
  }
}
