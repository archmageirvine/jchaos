package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class BatTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Bat();
  }

  public void testFly() {
    assertTrue(new Bat().is(PowerUps.FLYING));
  }
}
