package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class GryphonTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Gryphon();
  }

  public void test() {
    assertTrue(new Gryphon().is(PowerUps.FLYING));
  }
}
