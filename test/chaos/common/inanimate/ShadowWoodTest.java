package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class ShadowWoodTest extends AbstractTreeTest {


  @Override
  public Castable getCastable() {
    return new ShadowWood();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new ShadowWood().reincarnation());
  }
}
