package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class WeakWallTest extends AbstractActorTest {


  @Override
  public Castable getCastable() {
    return new WeakWall();
  }

  public void test() {
    AbstractMonsterTest.checkAgainstSource(getActor());
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY, new WeakWall().getCastFlags());
  }
}
