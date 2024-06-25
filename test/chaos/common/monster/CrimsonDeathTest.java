package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class CrimsonDeathTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new CrimsonDeath();
  }

  public void test() {
    assertTrue(new CrimsonDeath().is(PowerUps.FLYING));
  }
}
