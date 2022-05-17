package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class PegasusTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Pegasus();
  }

  public void test() {
    assertTrue(new Pegasus().is(PowerUps.FLYING));
  }
}
