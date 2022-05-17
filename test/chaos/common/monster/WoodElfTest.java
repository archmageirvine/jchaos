package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class WoodElfTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new WoodElf();
  }

  public void test() {
    assertTrue(new WoodElf().is(PowerUps.ARCHERY));
  }
}
