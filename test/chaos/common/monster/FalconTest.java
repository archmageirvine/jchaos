package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class FalconTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Falcon();
  }

  public void test() {
    assertTrue(new Falcon().is(PowerUps.FLYING));
  }

}
