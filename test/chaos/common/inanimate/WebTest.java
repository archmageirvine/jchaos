package chaos.common.inanimate;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class WebTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Web();
  }

  @Override
  public void testReincarnation() {
    final Web w = new Web();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, w.getCastFlags());
    assertNull(w.reincarnation());
    assertEquals(Attribute.AGILITY, w.getSpecialCombatApply());
  }
}
