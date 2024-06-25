package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class PowerWallVerticalTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new PowerWallVertical();
  }

  @Override
  public void testHasBackdropImage() {
  }
}
