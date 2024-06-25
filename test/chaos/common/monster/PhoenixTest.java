package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class PhoenixTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Phoenix();
  }

  public void test() {
    assertTrue(new Phoenix().is(PowerUps.FLYING));
  }
}
