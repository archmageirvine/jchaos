package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class HarpyTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Harpy();
  }

  public void test() {
    assertTrue(new Harpy().is(PowerUps.FLYING));
  }
}
