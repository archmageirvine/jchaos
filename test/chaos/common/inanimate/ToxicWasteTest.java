package chaos.common.inanimate;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class ToxicWasteTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new ToxicWaste();
  }

  @Override
  public void testReincarnation() {
    assertEquals(Castable.CAST_EMPTY | Castable.CAST_LOS, getCastable().getCastFlags());
    assertEquals(null, new ToxicWaste().reincarnation());
  }

}
