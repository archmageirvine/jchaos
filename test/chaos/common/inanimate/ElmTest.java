package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ElmTest extends AbstractTreeTest {

  @Override
  public Castable getCastable() {
    return new Elm();
  }

  @Override
  public void testReincarnation() {
    final Elm dw = new Elm();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, dw.getCastFlags());
    assertEquals(null, dw.reincarnation());
    assertEquals(Attribute.INTELLIGENCE, dw.getSpecialCombatApply());
  }
}
