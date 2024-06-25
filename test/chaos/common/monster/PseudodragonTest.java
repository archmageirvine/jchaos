package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class PseudodragonTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Pseudodragon();
  }

  public void test() {
    assertTrue(new Pseudodragon().is(PowerUps.FLYING));
  }
}
