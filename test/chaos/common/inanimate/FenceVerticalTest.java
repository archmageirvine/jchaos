package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class FenceVerticalTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new FenceVertical();
  }

  @Override
  public void testHasBackdropImage() {
  }
}
