package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class ManticoreTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Manticore();
  }

  public void test() {
    assertTrue(new Manticore().is(PowerUps.FLYING));
  }
}
