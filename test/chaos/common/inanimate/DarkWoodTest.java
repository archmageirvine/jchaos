package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class DarkWoodTest extends AbstractTreeTest {


  @Override
  public Castable getCastable() {
    return new DarkWood();
  }

  @Override
  public void testReincarnation() {
    final DarkWood dw = new DarkWood();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, dw.getCastFlags());
    assertEquals(null, dw.reincarnation());
  }
}
