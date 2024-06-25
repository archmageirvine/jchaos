package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class EagleTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Eagle();
  }

  public void test() {
    assertTrue(new Eagle().is(PowerUps.FLYING));
  }
}
