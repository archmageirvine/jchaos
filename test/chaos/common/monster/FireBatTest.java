package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class FireBatTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new FireBat();
  }

  public void test() {
    assertTrue(new FireBat().is(PowerUps.FLYING));
  }
}
