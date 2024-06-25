package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class GrayElfTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new GrayElf();
  }

  public void test() {
    assertTrue(new GrayElf().is(PowerUps.ARCHERY));
  }
}
