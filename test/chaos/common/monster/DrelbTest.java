package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class DrelbTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Drelb();
  }

  public void test() {
    assertTrue(new Drelb().is(PowerUps.FLYING));
  }
}
