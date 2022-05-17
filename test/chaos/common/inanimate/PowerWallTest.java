package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class PowerWallTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new PowerWall();
  }

  public void test() {
    AbstractMonsterTest.checkAgainstSource(getActor());
  }
}
