package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class FloatingEyeTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new FloatingEye();
  }

  public void test() {
    assertTrue(new FloatingEye().is(PowerUps.FLYING));
  }
}
