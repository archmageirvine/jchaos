package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class FirTest extends AbstractTreeTest {

  @Override
  public Castable getCastable() {
    return new Fir();
  }

  @Override
  public void testReincarnation() {
    final Fir dw = new Fir();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, dw.getCastFlags());
    assertEquals(null, dw.reincarnation());
    assertEquals(Attribute.MOVEMENT, dw.getSpecialCombatApply());
  }
}
