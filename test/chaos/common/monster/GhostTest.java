package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class GhostTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Ghost();
  }

  public void test() {
    assertTrue(new Ghost().is(PowerUps.FLYING));
  }
}
