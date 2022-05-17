package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class DreadElfTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new DreadElf();
  }

  public void test() {
    assertTrue(new DreadElf().is(PowerUps.ARCHERY));
  }
}
