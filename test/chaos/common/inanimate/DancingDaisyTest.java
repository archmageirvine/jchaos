package chaos.common.inanimate;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.growth.Wheat;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class DancingDaisyTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new DancingDaisy();
  }

  @Override
  public void testReincarnation() {
    assertNull(new Wheat().reincarnation());
  }
}
