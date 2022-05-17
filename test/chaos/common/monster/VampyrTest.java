package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class VampyrTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Vampyr();
  }

  public void test() {
    assertTrue(new Vampyr().is(PowerUps.FLYING));
  }
}
