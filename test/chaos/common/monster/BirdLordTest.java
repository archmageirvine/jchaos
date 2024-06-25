package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class BirdLordTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new BirdLord();
  }

  public void test() {
    assertTrue(new BirdLord().is(PowerUps.FLYING));
  }
}
