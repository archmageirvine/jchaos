package chaos.common.inanimate;

import chaos.common.AbstractTreeTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class MagicWoodTest extends AbstractTreeTest {


  @Override
  public Castable getCastable() {
    return new MagicWood();
  }

  public void testCollapse() {
    assertTrue(!new MagicWood().freeCollapse());
  }

}
