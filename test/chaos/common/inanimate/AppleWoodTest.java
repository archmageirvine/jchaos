package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class AppleWoodTest extends AbstractTreeTest {

  @Override
  public Castable getCastable() {
    return new AppleWood();
  }

  public void testPo() {
    final AppleWood a = new AppleWood();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, a.getCastFlags());
    assertEquals(Attribute.MAGICAL_RESISTANCE, a.getSpecialCombatApply());
  }

}
