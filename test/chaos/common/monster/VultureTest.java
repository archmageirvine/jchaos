package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class VultureTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Vulture();
  }

  public void test() {
    assertTrue(new Vulture().is(PowerUps.FLYING));
  }
}
