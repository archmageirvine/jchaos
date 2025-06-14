package chaos.common.growth;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ThistleTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new DancingDaisy();
  }

  @Override
  public void testReincarnation() {
    assertNull(new Thistle().reincarnation());
  }
}
