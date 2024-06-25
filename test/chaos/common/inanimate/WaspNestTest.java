package chaos.common.inanimate;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class WaspNestTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new WaspNest();
  }

  @Override
  public void testReincarnation() {
    final WaspNest w = new WaspNest();
    assertEquals(null, w.reincarnation());
  }
}
