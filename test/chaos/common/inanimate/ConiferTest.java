package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ConiferTest extends AbstractTreeTest {

  @Override
  public Castable getCastable() {
    return new Conifer();
  }

  @Override
  public void testReincarnation() {
    final Conifer dw = new Conifer();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, dw.getCastFlags());
    assertNull(dw.reincarnation());
    assertEquals(Attribute.MAGICAL_RESISTANCE, dw.getSpecialCombatApply());
  }
}
