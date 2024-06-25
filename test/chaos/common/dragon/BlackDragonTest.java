package chaos.common.dragon;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class BlackDragonTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new BlueDragon();
  }

  public void test() {
    assertTrue(new BlackDragon().is(PowerUps.FLYING));
  }
}
