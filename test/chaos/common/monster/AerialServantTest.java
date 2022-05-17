package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class AerialServantTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new AerialServant();
  }

  public void testFly() {
    assertTrue(new AerialServant().is(PowerUps.FLYING));
  }
}
